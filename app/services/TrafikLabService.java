package services;

import com.fasterxml.jackson.databind.JsonNode;
import models.accommodation.Address;
import play.Logger;
import play.libs.Json;

/**
 * Created by SimonSchwieler on 2017-05-16.
 */
public class TrafikLabService {

    private static final String NO_TRIP_FOUND = "H980";

    private final String TRAFIKLAB_TRIP="http://api.sl.se/api2/TravelplannerV2/trip.Json?";
    
    private final String TRAFIKLAB_KEY="key=cd8b0846848440649bda73e42406dc61&";
    private final double originCoordLat = 59.7605294;
    private final double originCoordLong = 18.7123039;
    private final String originCoordName = "Vegagatan";
    private final double destCoordLat = 59.3048662;
    private final double destCoordLong = 18.0930015;
    private final String destCoordName = "Lumaparksvägen";
    private Address tCentralen = new Address("Vasagatan", 20, 'T', "Stockholm");


    public TrafikLabService(String apiKey){
        tCentralen = GoogleService.getCoordinates(tCentralen,apiKey);
    }

    /**
     * The method takes two addresses and returns the time taken for the travel as well as the distance in meters to the closest station.
     * @param address1 - Origin address. Starting point.
     * @param address2 - The point of travel. Endpoint.
     * @return - JsonNode containing distance (to startpoint) and duration (total).
     */
    public JsonNode getPath(Address address1, Address address2){

        String streetName1 = escapeWhitespace(address1.streetName);
        String streetName2 = escapeWhitespace(address2.streetName);

        String query = "originCoordLat=" + address1.latitude + "&originCoordLong=" + address1.longitude + "&originCoordName=" + streetName1 + "5&destCoordLat=" + address2.latitude + "&destCoordLong=" + address2.longitude +"&destCoordName=" + streetName2;
        String urlString=TRAFIKLAB_TRIP+TRAFIKLAB_KEY+query;
        JsonNode node;
        node=GoogleService.gatherData(urlString);
        Logger.debug(urlString);

        if (node == null) {
            return null;
        }

        JsonNode error = node.findValue("errorCode");

        if (error != null) {

            if (error.asText().equals(NO_TRIP_FOUND)) {

                Short nullDistance = null;
                Short nullDuration = null;

                return Json.newObject().put("distance", nullDistance).put("duration", nullDuration);

            }
            return Json.newObject().put("distance", 0).put("duration", 0);

        }
        return Json.newObject().put("distance", node.findValue("dist").asInt()).put("duration", node.findValue("dur").asInt());

    }

    private String escapeWhitespace(String stringToEscape) {

        if(stringToEscape != null) {
            return stringToEscape.replaceAll(" ", "+");
        }
        return null;

    }

    /**
     * Gets the travel duration from the startingpoint to TCentralen, as well as the distance in meters to the closest station.
     * @param address - Origin address. Starting point.
     * @return - JsonNode containing distance (to starting point) and duration (total).
     */
    public JsonNode getDistanceToCentralen(Address address){
        Logger.debug("in GetdistfromCent: "+address.streetName+", cent: "+tCentralen.streetName);
        return getPath(address, tCentralen);
    }

    public JsonNode fixa(){
        Address a = new Address(originCoordName, originCoordLat, originCoordLong);
        Address b = new Address(destCoordName, destCoordLat, destCoordLong);
        return getPath(a, b);

    }
    
    
    






}
