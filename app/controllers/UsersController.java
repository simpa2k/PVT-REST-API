package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import models.user.TenantProfile;
import models.user.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.Option;
import services.users.UsersService;
import utils.ResponseBuilder;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Enver on 2017-05-05.
 */

@Security.Authenticated(Security.Authenticator.class)
public class UsersController extends Controller {

    private UsersService usersService;

    @Inject
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    public Result getUser(final Option<Integer> maxRent, final Option<Integer> maxDeposit){

       List<User> users = usersService.getSubset(maxRent, maxDeposit);
      
        // GET http://localhost:9000/users?maxRent=5000&maxDeposit=8000&start=2017-05-01&end=2018-05-1



        //User tenant = (User) ctx().args.get("user");

        return ResponseBuilder.buildOKList(users);


    }
    
    /*private Result createTenant(JsonNode body){
	    int maxRent=body.findValue("maxRent").asInt();
	    int numberOfTenants=body.findValue("numberOfTenants").asInt();
	    int age=body.findValue("age").asInt();
	    String description=body.findValue("description").asText();
	    String email=body.findValue("emailAddress").asText();
	    String password=body.findValue("password").asText();
	    String fullName=body.findValue("fullName").asText();
	    double income=body.findValue("income").asDouble();
	    String occupation=body.findValue("occupation").asText();
	    double deposit=body.findValue("deposit").asDouble();
	    Tenant t=new Tenant(email, password, fullName, description, age, numberOfTenants, maxRent, income, occupation, deposit);
	    t.save();
	    return ok("whoop");
    }
	
	private Result createRenter(JsonNode body){
		String email=body.findValue("emailAddress").asText();
		String password=body.findValue("password").asText();
		String name=body.findValue("fullName").asText();
		String desc=body.findValue("description").asText();
		int age=body.findValue("age").asInt();
		Renter r=new Renter(email, password, name, desc, age);
		r.save();
		return ok("whey");
    }
	
	public Result create(){
	    JsonNode body;
	    try {
		    body = request().body().asJson();
		    if(body==null){
		    	return notFound("bodyisnull");
		    }
	    } catch (NullPointerException e) {
		    Result result = ResponseBuilder.buildBadRequest("Request body required", ResponseBuilder.MALFORMED_REQUEST_BODY);
		    return result;
	    }
		Result usr;
	 
		if(body.findValue("isRenter").asBoolean()) usr=createRenter(body);
    	else usr=createTenant(body);
		
		return usr;
	}*/

    public Result createProfile() {

        JsonNode requestBody = request().body().asJson();

        try {
            TenantProfile profile = usersService.setProfile(FacebookSecurityController.getUser(), requestBody);
        } catch (JsonProcessingException e) {
            return ResponseBuilder.buildBadRequest("Could not parse request body.", ResponseBuilder.MALFORMED_REQUEST_BODY);
        }

        return noContent();

	}
}
