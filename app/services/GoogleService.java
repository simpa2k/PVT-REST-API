package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.accommodation.Address;

import java.io.IOException;

import play.Logger;
import play.libs.Json;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Gathers data from API's and saves them to the database.
 */
public class GoogleService{
//	private ObjectMapper mapper=new ObjectMapper();
	private Map<String, String> nextPageTokens=new HashMap<>();
	private final static String PLACES_URL="https://maps.googleapis.com/maps/api/place/";
	private final String GEOCODE_URL="https://maps.googleapis.com/maps/api/geocode/";
	private final String PLACES_KEY;
	private final String NEARBY_SEARCH="nearbysearch/";
	private final static String TEXT_SEARCH="textsearch/";
	
	private final String[] types={"gym", "grocery_or_supermarket", "convenience_store", "subway_station", "bar", "restaurant"};
	
	private final static String JSON="json?";
	private final String LOCATION="location=";
	private final String RADIUS="radius=1000";
	
	public GoogleService(String apiKey){
		this.PLACES_KEY="key="+apiKey;
	}
	
	//=====================================GENERIC METHODS
	
	/**
	 * Finds the Coordinates, from Google Places of a particular address
	 * @param address - Must contain streetname, number and area
	 * @return - the address passed in with it's coordinates added.
	 */
	public static Address getCoordinates(Address address, String key){
		String query="query="+address.streetName+"+"+address.streetNumber+"+"+address.area;
		String urlString=PLACES_URL+TEXT_SEARCH+JSON+query+"&key="+key;
		JsonNode node;

		Logger.debug(urlString);
		
		node=gatherData(urlString);
		address.latitude=node.findValue("lat").asDouble();
		address.longitude=node.findValue("lng").asDouble();
		Logger.debug(address.longitude+" "+address.latitude);
		return address;
	}
	
	/**
	 * - Gathers data from a @url and returns a jsonNode with that data
	 * @param url - The API-URL to gather data from
	 * @return - the data gathered from the api
	 */
	private static JsonNode doApiCall(URL url) throws IOException{
		return Json.mapper().readTree(url);
	}
	
	/**
	 * - Gathers data from a @urlString and returns a jsonNode with that data
	 * @param urlString - String that must represent a rul
	 * @return - JsonNode with the data from the url
	 */
	public static JsonNode gatherData(String urlString){
		//Logger.debug("in gatherData");
		try{
			return doApiCall(new URL(urlString));
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gathers the nearby points of interest to a address.
	 * @param address - The address from which to find nearby interests.
	 * @return - All the points of interests close to the specified address.
	 */
	public ObjectNode gatherNearbyData(Address address){
		ObjectNode allData=Json.newObject();
		
		for(String type : types){
			ArrayNode typeData=allData.putArray(type);
			ArrayNode tempData=getAllNearbyInterests(type, address);
			for(JsonNode tempNode : tempData.get(0)){
				typeData.add(tempNode);
			}
		}
		allData=findAdditionalData(allData);
		for(String type : types){
			Logger.debug("There are "+allData.get(type).size()+" "+type+'s');
		}
		return allData;
	}
	
	/**
	 * Executes gathering of data.
	 */
	public JsonNode gather(){
		return gatherData(PLACES_URL+TEXT_SEARCH+JSON+LOCATION+"&"+PLACES_KEY);
	}
	//==================================ADDITIONALDATAMETHODS
	
	/**
	 * Finds the next page data from from the first nextPageToken, if there is any
	 * @param allData - the Root node the additional data is going to be added to
	 * @return - @allData with the new data added
	 */
	private ObjectNode findAdditionalData(ObjectNode allData){
		JsonNode tempDataNode=null;
		String status="INVALID_REQUEST";
		
		int i=0;
		do{
			String nextPageToken=null;
			String type=types[i];
			while(nextPageToken==null){
				nextPageToken=nextPageTokens.remove(type);
				if(nextPageToken==null&&i<5){
					i++;
					type=types[i];
				}else if(i==5)break;
			}
			
			try{
				while(status.contentEquals("INVALID_REQUEST")|tempDataNode==null&&nextPageToken!=null){
					TimeUnit.MILLISECONDS.sleep(500);
					tempDataNode=gatherData(PLACES_URL+NEARBY_SEARCH+JSON+"pagetoken="+nextPageToken+"&"+PLACES_KEY);
					status=tempDataNode.findValue("status").asText();
				}
			}catch(NullPointerException|InterruptedException e){Logger.error("THROW");}
			
			for(JsonNode tempNode:tempDataNode.findValues("results").get(0)){
				ArrayNode typeList = (ArrayNode) allData.findValue(type);
				typeList.add(tempNode);
			}
			if(isNextPageTrue(tempDataNode)){
				String newNextPageToken=tempDataNode.findValue("next_page_token").textValue();
				nextPageTokens.put(type,newNextPageToken);
			}
			if(i<types.length-1)i++;
			else i=0;
		}while(!nextPageTokens.isEmpty());
		return allData;
	}
	
	private ArrayNode getAllNearbyInterests(String type, Address address){
		String urlString = PLACES_URL+NEARBY_SEARCH+JSON+LOCATION+address.latitude+","+address.longitude+"&"+RADIUS+"&"+"type="+type+"&"+PLACES_KEY;
		Logger.debug("Gathering "+type+"'s");
		ArrayNode nearbyInterestList=Json.newArray();
		
		JsonNode dataOfType=gatherData(urlString);
		for(JsonNode tempNode:dataOfType.findValues("results")){
			nearbyInterestList.add(tempNode);
		}
		if(isNextPageTrue(dataOfType)){
			Logger.debug("NextPageToken of "+type+" added to list.");
			nextPageTokens.put(type,dataOfType.findValue("next_page_token").textValue());
		}
		
		return nearbyInterestList;
	}
	//==================================HELPERMETHODS
	
	private boolean isNextPageTrue(JsonNode data){
		String npt;
		try{
			npt=data.findValue("next_page_token").toString();
		}catch(NullPointerException e){
			Logger.debug("NextPageToken not present");
			return false;
		}
		Logger.debug("NextPageToken Present");
		return true;
	}
	
	//==================================EXTRAS
	
	private void printList(ArrayNode l){
		Logger.debug("Print List:");
		Logger.debug("Size: "+l.size());
		for(JsonNode n : l){
			Logger.debug(n.toString());
		}
	}
	
	private void logInterests(ObjectNode nearbyInterests){
		Logger.debug("number of gyms:                   "+nearbyInterests.findValue("gym").findValue("results").size());
		Logger.debug("number of grocery_or_supermarkets:"+nearbyInterests.findValue("grocery_or_supermarket").findValue("results").size());
		Logger.debug("number of convenience_stores:     "+nearbyInterests.findValue("convenience_store").findValue("results").size());
		Logger.debug("number of subway_stations:        "+nearbyInterests.findValue("subway_station").findValue("results").size());
		Logger.debug("number of bars:                   "+nearbyInterests.findValue("bar").findValue("results").size());
		Logger.debug("number of restaurants:            "+nearbyInterests.findValue("restaurant").findValue("results").size());
	}
	
}
