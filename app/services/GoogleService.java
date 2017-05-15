package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.accommodation.Address;

import java.io.BufferedReader;

import java.io.IOException;

import play.Logger;
import play.libs.Json;

import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Gathers data from API's and saves them to the database.
 */
public class GoogleService{
	private ObjectMapper mapper=new ObjectMapper();
	
	private final String PLACES_URL="https://maps.googleapis.com/maps/api/place/";
	private final String GEOCODE_URL="https://maps.googleapis.com/maps/api/geocode/";
	private final String PLACES_KEY="key=AIzaSyDSNr7q3oRHvttkSfK85MYQnN3DSoRg_tg";
	private final String NEARBY_SEARCH="nearbysearch/";
	private final String TEXT_SEARCH="textsearch/";
	
	
	private final String[] types={"gym", "grocery_or_supermarket", "convenience_store", "subway_station", "bar", "restaurant"};
	
	
	private final String JSON="json?";
	private final String LOCATION="location=59.3525362,17.9796305";
	private final String RADIUS="radius=1000";
	
	
	private String DATA="NODATA";
	
	/**
	 *
	 * @param url - The API-URL to gather data from
	 * @return data - The data gathered from the API-call.
	 * @throws IOException
	 */
	private JsonNode doApiCall(URL url) throws IOException{
		Logger.debug("in doApiCall");
		StringBuilder content=new StringBuilder();
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(url.openStream())); //reads all data from the url to a reader
		String line;
		while((line=bufferedReader.readLine())!=null) //appending every line of data that the reader has gathered to a string.
		{
			content.append(line);
		}
		bufferedReader.close();
		
		JsonNode data=mapper.readTree(content.toString());
		
		return data;
	}
	
	
	
	
	
	private ArrayNode findAdditionalResults(String nextPageToken){
		Logger.debug("Gathering Additional DATA");
		String urlString=PLACES_URL+NEARBY_SEARCH+JSON+"pagetoken="+nextPageToken+"&"+PLACES_KEY;
		JsonNode tempNode=null;
		
		ArrayNode test=Json.newArray();
		
		tempNode=gatherData(urlString);
		Logger.debug("ADD all results");
		test.addAll(tempNode.findValues("results"));
		if(checkNextPageToken(tempNode)){
			Logger.debug("Nextpagetoken is present, doing this again");
			test.addAll(findAdditionalResults(tempNode.findValue("next_page_token").toString()));
		}
		Logger.debug("returing data gathered: "+test.size());
		return test;
	}
	
	
	
	
	private JsonNode gatherData(String s){
		Logger.debug("in gatherData");
		try{
			return doApiCall(new URL(s));
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayNode gatherNearbyData(){
		Logger.debug("In GATHERNEARBYDATA");
		ArrayNode data=Json.newArray();
		for(String type : types){
			data.addAll(getAllNearbyInterests(type));
		}
		return data;
	}
	
	private JsonNode api(){
		String urlString=PLACES_URL+NEARBY_SEARCH+JSON+LOCATION+"&"+RADIUS+"&"+PLACES_KEY;
		Logger.debug("The apiurl is: "+urlString);
		gatherData(urlString);
		return null;
	}
	
	
	
	/**
	 * Executes gathering of data.
	 */
	public JsonNode gather(){
		Logger.debug("STARTING API DATA GATHERING");
		JsonNode data=api();
		Logger.debug(data.findValue("name").toString());
		return data;
	}
	
	
	public Address getCoordinates(Address address){
		
		String query="query="+address.streetName+"+"+address.streetNumber+"+"+address.area;
		String urlString=PLACES_URL+TEXT_SEARCH+JSON+query+"&"+PLACES_KEY;
		JsonNode node;
		
		node=gatherData(urlString);
		Logger.debug(node.toString());
		address.latitude=node.findValue("lat").asDouble();
		address.longitude=node.findValue("lng").asDouble();
		Logger.debug(address.longitude+" "+address.latitude);
		return address;
	}
	
	//==================================HELPERMETHODS
	
	private ArrayNode getAllNearbyInterests(String type){
		String urlString = PLACES_URL+NEARBY_SEARCH+JSON+LOCATION+"&"+RADIUS+"&"+"type="+type+"&"+PLACES_KEY;
		ArrayNode node=Json.newArray();
		JsonNode dataOfType=gatherData(urlString);
		node.addAll(dataOfType.findValues("results"));
		
		
		if(checkNextPageToken(dataOfType)){
			node.addAll(findAdditionalResults(dataOfType.findValue("next_page_token").toString()));
		}
		return node;
	}
	
	private boolean checkNextPageToken(JsonNode data){
		String npt=null;
		try{
			npt=data.findValue("next_page_token").toString();
		}catch(NullPointerException e){
			npt=null;
		}
		return (npt!=null);
	}
	
	//==================================EXTRAS
	
	public String getDATA(){
		return this.DATA;
	}
	
	private void logInterests(ObjectNode nearbyInterests){
		Logger.debug("number of gyms:                   "+nearbyInterests.findValue("gym").findValue("results").size());
		Logger.debug("number of grocery_or_supermarkets:"+nearbyInterests.findValue("grocery_or_supermarket").findValue("results").size());
		Logger.debug("number of convenience_stores:     "+nearbyInterests.findValue("convenience_store").findValue("results").size());
		Logger.debug("number of subway_stations:        "+nearbyInterests.findValue("subway_station").findValue("results").size());
		Logger.debug("number of bars:                   "+nearbyInterests.findValue("bar").findValue("results").size());
		Logger.debug("number of restaurants:            "+nearbyInterests.findValue("restaurant").findValue("results").size());
	}
	
	
	@Deprecated
	private ArrayNode doApiCalll(URL url) throws IOException{
		Logger.debug("in doApiCalll");
		
		StringBuilder content=new StringBuilder();
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(url.openStream())); //reads all data from the url to a reader
		String line;
		while((line=bufferedReader.readLine())!=null) //appending every line of data that the reader has gathered to a string.
		{
			content.append(line);
		}
		bufferedReader.close();
		JsonNode n = Json.parse(content.toString());
		ArrayNode data= Json.newArray().addAll(n.findValues("results"));
		Logger.debug("DATANODEADADA:"+data.toString());
		return data;
	}
	
	
	
	@Deprecated
	private ArrayNode gatherDataa(String s){
		Logger.debug("in gatherData");
		try{
			return doApiCalll(new URL(s));
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Deprecated
	public JsonNode findNearbyInterests(){
		Logger.debug("In findNearby");
		String urlString;
		ObjectNode nearbyInterests=null;
		ArrayNode tempNode=null;
		for(String t : types){
			Logger.debug("finding "+t+"'s");
			urlString=PLACES_URL+NEARBY_SEARCH+JSON+LOCATION+"&"+RADIUS+"&"+"type="+t+"&"+PLACES_KEY;
			if(nearbyInterests==null){
				nearbyInterests=new ObjectNode(new JsonNodeFactory(false));
			}
			for(JsonNode o : gatherData(urlString)){
				tempNode.add(o);
			}
			
			if(!tempNode.findValue("next_page_token").isNull()){
				findAdditionalResults(tempNode.findValue("next_page_token").toString());
			}
			
			nearbyInterests.set(t, tempNode);
			Logger.debug(nearbyInterests.findValues(t).toString());
		}
		logInterests(nearbyInterests);
		return nearbyInterests;
	}
	
}
