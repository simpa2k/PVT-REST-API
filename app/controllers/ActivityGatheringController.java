package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.accommodation.Address;
import play.Configuration;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.GoogleService;
import services.TrafikLabService;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Endpoint controller for initiating gathering of data.
 *
 * @author Simon Olofsson
 */
@Security.Authenticated(AdminSecured.class)
public class ActivityGatheringController extends Controller {
	private final String apiKey;
	private final GoogleService googleService;
	@Inject
	public ActivityGatheringController(Configuration configuration){
		this.apiKey=configuration.getString("googleAPIKey");
		this.googleService=new GoogleService(apiKey);
	}

    public Result gather() {

        GoogleService gatherer = new GoogleService(apiKey);
        JsonNode n = gatherer.gather();

        return ok().sendJson(n);

    }

	public Result trafikLab(){

        TrafikLabService trafikLabService = new TrafikLabService(apiKey);
        JsonNode tr = trafikLabService.getDistanceToCentralen(GoogleService.getCoordinates(new Address("Vegagatan", 5, 'C', "Norrtälje"),apiKey));

        return ok().sendJson(tr);
    }
    
    
    public Result gather2() throws IOException{
        ObjectMapper objm =new ObjectMapper();
        GoogleService gatherer = new GoogleService(apiKey);
        Address a=new Address("Götgatan", 78, "Stockholm");
        a=gatherer.getCoordinates(a,apiKey);
        JsonNode n = objm.valueToTree(a);
        return ok().sendJson(n);

    }
	
}
