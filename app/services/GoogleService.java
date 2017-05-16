package services;

import akka.dispatch.Foreach;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.RawValue;
import models.accommodation.Address;

import java.io.BufferedReader;

import java.io.IOException;

import play.Logger;
import play.libs.Json;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Gathers data from API's and saves them to the database.
 */
public class GoogleService{
//	private ObjectMapper mapper=new ObjectMapper();
	private Map<String, String> nextPageTokens=new HashMap<>();
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
	 * @return - the data gathered from the api
	 */
	private JsonNode doApiCall(URL url) throws IOException{
		return Json.mapper().readTree(url);
	}
	
	
	
	
	
	private ArrayNode findAdditionalResults(String nextPageToken){
		Logger.debug("Gathering Additional DATA");
		//Logger.debug("NXTPGETOKN: "+nextPageToken);
		String urlString=PLACES_URL+NEARBY_SEARCH+JSON+"pagetoken="+nextPageToken+"&"+PLACES_KEY;
		//Logger.debug("URL: "+urlString);
		JsonNode tempNode=null;
		ArrayNode test=Json.newArray();
		
		tempNode=gatherData(urlString);
		Logger.debug(tempNode.toString());
		List<JsonNode>tempList=tempNode.findValues("results");
		Logger.debug("size: "+tempList.size());
		for(JsonNode tem:tempList){
			Logger.debug("adding:"+ tem.get("name")+" to the array");
			test.add(tem);
		}
		
		if(isNextPageTrue(tempNode)){
			test.addAll(findAdditionalResults(tempNode.findValue("next_page_token").toString()));
		}
		
		Logger.debug("returning data gathered: "+test.size());
		return test;
	}
	
	
	
	
	private JsonNode gatherData(String s){
		//Logger.debug("in gatherData");
		try{
			return doApiCall(new URL(s));
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	public ObjectNode gatherNearbyData(){
		Logger.debug("In GATHERNEARBYDATA");
		ObjectNode d=Json.newObject();
		Map<String, String> nextPageTokens=new HashMap<>();
		
		for(String type : types){
			Logger.debug("Getting all "+type+"-data");
			ArrayNode temp=d.putArray(type);
			ArrayNode dd=getAllNearbyInterests(type);
			for(JsonNode jn : dd.get(0)){
				temp.add(jn);
			}
		}
		int i=0;
		do{
			Logger.debug("in do/while i="+i);
			String type=types[i];
			String npt=nextPageTokens.remove(type);
			JsonNode temp = gatherData(PLACES_URL+NEARBY_SEARCH+JSON+"pagetoken="+npt+"&"+PLACES_KEY);
			for(JsonNode nodde:temp.findValue("results")){
				Logger.debug("adding results: "+nodde.toString());
				
				d.withArray(type).add(nodde);
			}
			if(isNextPageTrue(temp)){
				String s=temp.findValue("next_page_token").toString();
				nextPageTokens.put(type,s);
			}
			if(i<types.length-1)i++;
			else i=0;
		}while(!nextPageTokens.isEmpty());
		return d;
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
	
	
	
	private ObjectNode addToObject(ArrayList<String> list,ObjectNode obj,String type){
		ObjectNode typeObj=Json.newObject();
		ArrayList<JsonNode> tList=new ArrayList<>();
		
		for(String s : list){
				tList.add(typeObj.set(type,Json.parse(s)));
		}
		
		return obj;
	}
	
	private ArrayList<String> makeListFromJsonNode(JsonNode node){
		ArrayList<String> typeData=new ArrayList<>();
		Iterator<JsonNode> iter=node.findValues("results").iterator();
		while(iter.hasNext())
			typeData.add(iter.next().toString());
		return typeData;
	}
	
	private ArrayNode getAllNearbyInterests(String type){
		String urlString = PLACES_URL+NEARBY_SEARCH+JSON+LOCATION+"&"+RADIUS+"&"+"type="+type+"&"+PLACES_KEY;
		Logger.debug("Gathering "+type+"'s");
		ArrayNode arno=Json.newArray();
		
		JsonNode dataOfType=gatherData(urlString);
		for(JsonNode d:dataOfType.findValues("results")){
			arno.add(d);
		}
		if(isNextPageTrue(dataOfType)){
			Logger.debug("NextPageToken of: "+type+" added to list.");
			nextPageTokens.put(type,dataOfType.findValue("next_page_token").toString());
		}
		
		return arno;
	}
	
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
	
	private String makeString(ArrayList<String> stringList){
		String total="";
		for(String s:stringList){
			total+=s;
		}
		return total;
	}
	
	private void printList(ArrayNode l){
		Logger.debug("Print List:");
		Logger.debug("Size: "+l.size());
		for(JsonNode n : l){
			Logger.debug(n.toString());
		}
	}
	
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
	
	
	
	
	//=================================DEPRECATED
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
