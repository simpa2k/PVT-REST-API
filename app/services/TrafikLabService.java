package services;

import com.fasterxml.jackson.databind.JsonNode;
import models.accommodation.Address;
import play.Logger;
import play.libs.Json;

/**
 * Created by SimonSchwieler on 2017-05-16.
 */
public class TrafikLabService {


    private final String TRAFIKLAB_TRIP="http://api.sl.se/api2/TravelplannerV2/trip.Json?";

    private final String TRAFIKLAB_KEY="key=cd8b0846848440649bda73e42406dc61&";
    private final double originCoordLat = 59.7605294;
    private final double originCoordLong = 18.7123039;
    private final String originCoordName = "Vegagatan";
    private final double destCoordLat = 59.3048662;
    private final double destCoordLong = 18.0930015;
    private final String destCoordName = "Lumaparksvägen";


    // http://api.sl.se/api2/TravelplannerV2/trip.Json?
    // key=cd8b0846848440649bda73e42406dc61&
    // originCoordLat=59.7605294&originCoordLong=18.7123039&originCoordName=Vegagatan&
    // destCoordLat=59.3048662&destCoordLong=18.0930015&destCoordName=Lumaparksvägen3


   // http://api.sl.se/api2/TravelplannerV2/trip.Json?
    // key=cd8b0846848440649bda73e42406dc61&originCoordLat=18.7123039&
    // originCoordLong=59.7605294&originCoordName=Vegagatan&
    // destCoordLat=18.0930015&destCoordLong=59.3048662&destCoordName=Lumaparksvagen3


    public JsonNode getTrafiklab(Address address1, Address address2){

        String query = "originCoordLat=" + address1.latitude + "&originCoordLong=" + address1.longitude + "&originCoordName=" + address1.streetName + "5&destCoordLat=" + address2.latitude + "&destCoordLong=" + address2.longitude +"&destCoordName=" + address2.streetName;
     //   String query="query="+address.streetName+"+"+address.streetNumber+"+"+address.area;
        String urlString=TRAFIKLAB_TRIP+TRAFIKLAB_KEY+query;
        Logger.debug(urlString);
        JsonNode node;

        node=GoogleService.gatherData(urlString);
        Logger.debug(node.toString());
        JsonNode a = node.findValue("dist");
        JsonNode b = node.findValue("dur");

        //return node.findValue("dist");
        return Json.newObject().put("distance", node.findValue("dist").asInt()).put("duration", node.findValue("dur").asInt());

    }

    public JsonNode fixa(){

        Address a = new Address(originCoordName, originCoordLat, originCoordLong);
        Address b = new Address(destCoordName, destCoordLat, destCoordLong);
        return getTrafiklab(a, b);

    }








}
