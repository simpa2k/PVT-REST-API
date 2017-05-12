package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.accommodation.Address;
import play.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Gathers data from API's and saves them to the database.
 */
public class GoogleService{
    private final String PLACES_URL="https://maps.googleapis.com/maps/api/place/";
    private final String GEOCODE_URL="https://maps.googleapis.com/maps/api/geocode/";
    private final String PLACES_KEY="key=AIzaSyDSNr7q3oRHvttkSfK85MYQnN3DSoRg_tg";
    private final String NEARBY_SEARCH="nearbysearch/";
    private final String TEXT_SEARCH="textsearch/";

    private enum type {gym,grocery_or_supermarket,convenience_store,subway_station,bar,restaurant}

    private final String JSON="json?";
    private final String LOCATION="location=59.3525362,17.9796305";
    private final String RADIUS="radius=500";


    private String DATA="NODATA";

    private JsonNode buildString(URL url) throws IOException{
        ObjectMapper mapper=new ObjectMapper();

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






    public Address getCoordinates(Address address){

        String query = "query=" + address.streetName + "+" + address.streetNumber + "+" + address.area;
        String urlString=PLACES_URL+TEXT_SEARCH+JSON+query+"&"+PLACES_KEY;
        JsonNode node;

        try {
            node = buildString(new URL(urlString));
            Logger.debug(node.toString());
            address.latitude = node.findValue("lat").asDouble();
            address.longitude = node.findValue("lng").asDouble();
            Logger.debug(address.longitude + " " + address.latitude);

        }catch(Exception e){
            e.printStackTrace();
        }
        return address;
    }

   // https://maps.googleapis.com/maps/api/place/textsearch/json?query=dsv+nod&key=AIzaSyBZFhBxkJvAg4GfFxpKYRRaS_0JIchDwcw


    private void getData(){

    }

    private JsonNode api(){
        String urlString=PLACES_URL+NEARBY_SEARCH+JSON+LOCATION+"&"+RADIUS+"&"+PLACES_KEY;
        JsonNode ret;
        try{
            URL apiUrl=new URL(urlString);
            return buildString(apiUrl);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getDATA(){
        return this.DATA;
    }

    /**
     * Executes gathering of data.
     */
    public JsonNode gather(){
        JsonNode data=api();
        Logger.debug(data.findValue("name").toString());
        //Logger.debug(data.findValue("type").toString());
        return data;
    }
}
