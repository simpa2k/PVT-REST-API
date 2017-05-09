package unit.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.SecurityController;
import controllers.UsersController;
import models.user.TenantProfile;
import models.user.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import play.Application;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import play.test.Helpers;
import play.test.WithApplication;
import services.UsersService;
import testResources.TenantProfileUtils;
import testResources.mocks.SecurityMock;
import utils.ResponseBuilder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.NO_CONTENT;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

/**
 * @author Simon Olofsson
 */
public class UsersControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {

        return new GuiceApplicationBuilder().overrides(binder -> {

            binder.bind(Security.Authenticator.class).to(SecurityMock.class);

        }).in(Mode.TEST).build();
    }

    private UsersService usersService;

    @Before
    public void setup() {
        usersService = mock(UsersService.class);
    }

    @Test
    public void returnsOKOnCorrectRequest() throws JsonProcessingException {

        TenantProfile tenantProfile = TenantProfileUtils.createTenantProfile();
        ObjectNode accommodationJson = TenantProfileUtils.createTenantProfileJson();

        when(usersService.setProfile(any(User.class), any(JsonNode.class))).thenReturn(tenantProfile);
        UsersController usersController = new UsersController(usersService);

        Http.RequestBuilder fakeRequest = new Http.RequestBuilder();
        fakeRequest.bodyJson(accommodationJson);

        Result result = Helpers.invokeWithContext(fakeRequest, usersController::createProfile);

        assertEquals(NO_CONTENT, result.status());

    }

    @Test
    public void returnsBadRequestOnNoRequestBody() throws JsonProcessingException {

        TenantProfile tenantProfile = TenantProfileUtils.createTenantProfile();

        when(usersService.setProfile(any(User.class), any(JsonNode.class))).thenReturn(tenantProfile);

        UsersController usersController = new UsersController(usersService);

        Http.RequestBuilder fakeRequest = new Http.RequestBuilder();
        Result result = Helpers.invokeWithContext(fakeRequest, usersController::createProfile);

        assertEquals(BAD_REQUEST, result.status());

        JsonNode responseBody = Json.parse(contentAsString(result));
        assertEquals(ResponseBuilder.MALFORMED_REQUEST_BODY, responseBody.findValue("type").asText());

    }

    @Test
    public void returnsBadRequestOnEmptyRequestBody() throws JsonProcessingException {

        TenantProfile tenantProfile = TenantProfileUtils.createTenantProfile();
        ObjectNode emptyBody = Json.newObject();

        when(usersService.setProfile(any(User.class), any(JsonNode.class))).thenReturn(tenantProfile);

        UsersController usersController = new UsersController(usersService);

        Http.RequestBuilder fakeRequest = new Http.RequestBuilder();
        fakeRequest.bodyJson(emptyBody);

        Result result = Helpers.invokeWithContext(fakeRequest, usersController::createProfile);

        assertEquals(BAD_REQUEST, result.status());

        JsonNode responseBody = Json.parse(contentAsString(result));
        assertEquals(ResponseBuilder.MALFORMED_REQUEST_BODY, responseBody.findValue("type").asText());

    }

    @Test
    public void returnsBadRequestOnMalformedRequestBody() throws JsonProcessingException {

        ObjectNode accommodationJson = TenantProfileUtils.createTenantProfileJson();

        when(usersService.setProfile(any(User.class), any(JsonNode.class))).thenThrow(JsonProcessingException.class);

        UsersController usersController = new UsersController(usersService);

        Http.RequestBuilder fakeRequest = new Http.RequestBuilder();
        fakeRequest.bodyJson(accommodationJson);

        Result result = Helpers.invokeWithContext(fakeRequest, usersController::createProfile);

        assertEquals(BAD_REQUEST, result.status());

        JsonNode responseBody = Json.parse(contentAsString(result));
        assertEquals(ResponseBuilder.MALFORMED_REQUEST_BODY, responseBody.findValue("type").asText());

    }
}
