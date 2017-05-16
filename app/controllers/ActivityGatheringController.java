package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.accommodation.Address;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.GoogleService;
import utils.ActivityGatherer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Endpoint controller for initiating gathering of data.
 *
 * @author Simon Olofsson
 */
@Security.Authenticated(AdminSecured.class)
public class ActivityGatheringController extends Controller {

    public Result gather() {

        GoogleService gatherer = new GoogleService();
        JsonNode n = gatherer.gather();

        return ok().sendJson(n);

    }
	
	public Result nearby() {
		
		GoogleService gatherer = new GoogleService();
		ObjectNode n=gatherer.gatherNearbyData();
		
		return ok().sendJson(n);
	}
    
    
    public Result gather2() throws IOException{
        ObjectMapper objm =new ObjectMapper();
        GoogleService gatherer = new GoogleService();
        Address a=new Address("Götgatan", 78, "Stockholm");
        a=gatherer.getCoordinates(a);
        JsonNode n = objm.valueToTree(a);
        return ok().sendJson(n);

    }
	
}
