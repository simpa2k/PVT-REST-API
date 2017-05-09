package functional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.SecurityController;
import org.junit.Before;
import org.junit.Test;
import play.Configuration;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import repositories.UsersRepository;
import testResources.AccommodationUtils;
import testResources.TenantProfileUtils;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.NO_CONTENT;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

/**
 * @author Simon Olofsson
 */
public class FunctionalTest extends WithApplication {

    private UsersRepository usersRepository = new UsersRepository();
    private String facebookToken;

    @Before
    public void setup() {

        Configuration config = app.injector().instanceOf(Configuration.class);
        facebookToken = config.getString("facebookToken");

    }

    private String authenticateViaFacebook() {

        ObjectNode loginJson = Json.newObject();
        loginJson.put("facebookAuthToken", facebookToken);

        Http.RequestBuilder facebookLogin = fakeRequest(controllers.routes.FacebookSecurityController.login());
        facebookLogin.header("Content-Type", "application/json");
        facebookLogin.bodyJson(loginJson);

        Result loginResult = route(facebookLogin);
        JsonNode loginResponse = Json.parse(contentAsString(loginResult));

        return loginResponse.findValue("authToken").asText();

    }

    @Test
    public void setsTenantProfile() throws ParseException {

        String authToken = authenticateViaFacebook();

        ObjectNode profileJson = TenantProfileUtils.createTenantProfileJson();

        Http.RequestBuilder createProfileRequest = fakeRequest(controllers.routes.UsersController.createProfile());
        createProfileRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);
        createProfileRequest.bodyJson(profileJson);

        Result result = route(createProfileRequest);
        assertEquals(NO_CONTENT, result.status());

        Http.RequestBuilder getProfileRequest = fakeRequest(controllers.routes.UsersController.getUser());
        getProfileRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);

        Result getResult = route(getProfileRequest);
        JsonNode responseBody = Json.parse(contentAsString(getResult));

        JsonNode profile = responseBody.findValue("tenantProfile");

        TenantProfileUtils.makeStandardAssertionsAgainstJson(profile);

    }

    @Test
    public void createsAccommodation() {

        String authToken = authenticateViaFacebook();

        ObjectNode accommodationJson = AccommodationUtils.createAccommodationJson();

        Http.RequestBuilder createRequest = fakeRequest(controllers.routes.AccommodationController.createAccommodation());
        createRequest.header(SecurityController.AUTH_TOKEN_HEADER, authToken);
        createRequest.bodyJson(accommodationJson);

        Result result = route(createRequest);
        assertEquals(NO_CONTENT, result.status());

    }
}
