package unit.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.AccommodationController;
import controllers.SecurityController;
import controllers.routes;
import models.accommodation.Accommodation;
import models.user.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import services.AccommodationService;
import testResources.AccommodationUtils;
import utils.ResponseBuilder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.NO_CONTENT;
import static play.test.Helpers.contentAsString;

/**
 * @author Simon Olofsson
 */
public class AccommodationControllerTest extends WithApplication {

    private AccommodationService accommodationService;

    @Before
    public void setup() {
        accommodationService = mock(AccommodationService.class);
    }

    @Test
    public void returnsOKOnCorrectRequest() throws JsonProcessingException {

        Accommodation accommodation = AccommodationUtils.createAccommodation();
        ObjectNode accommodationJson = AccommodationUtils.createAccommodationJson();

        when(accommodationService.createAccommodationFromJson(any(User.class), any(JsonNode.class))).thenReturn(accommodation);
        AccommodationController accommodationController = new AccommodationController(accommodationService);

        Http.RequestBuilder fakeRequest = new Http.RequestBuilder();
        fakeRequest.bodyJson(accommodationJson);

        Result result = Helpers.invokeWithContext(fakeRequest, accommodationController::createAccommodation);

        assertEquals(NO_CONTENT, result.status());

    }

    @Test
    public void returnsBadRequestOnNoRequestBody() throws JsonProcessingException {

        Accommodation accommodation = AccommodationUtils.createAccommodation();

        when(accommodationService.createAccommodationFromJson(any(User.class), any(JsonNode.class))).thenReturn(accommodation);

        AccommodationController accommodationController = new AccommodationController(accommodationService);

        Http.RequestBuilder fakeRequest = new Http.RequestBuilder();
        Result result = Helpers.invokeWithContext(fakeRequest, accommodationController::createAccommodation);

        assertEquals(BAD_REQUEST, result.status());

        JsonNode responseBody = Json.parse(contentAsString(result));
        assertEquals(ResponseBuilder.MALFORMED_REQUEST_BODY, responseBody.findValue("type").asText());

    }

    @Test
    public void returnsBadRequestOnEmptyRequestBody() throws JsonProcessingException {

        Accommodation accommodation = AccommodationUtils.createAccommodation();
        ObjectNode emptyBody = Json.newObject();

        when(accommodationService.createAccommodationFromJson(any(User.class), any(JsonNode.class))).thenReturn(accommodation);

        AccommodationController accommodationController = new AccommodationController(accommodationService);

        Http.RequestBuilder fakeRequest = new Http.RequestBuilder();
        fakeRequest.bodyJson(emptyBody);

        Result result = Helpers.invokeWithContext(fakeRequest, accommodationController::createAccommodation);

        assertEquals(BAD_REQUEST, result.status());

        JsonNode responseBody = Json.parse(contentAsString(result));
        assertEquals(ResponseBuilder.MALFORMED_REQUEST_BODY, responseBody.findValue("type").asText());

    }

    @Test
    public void returnsBadRequestOnMalformedRequestBody() throws JsonProcessingException {

        Accommodation accommodation = AccommodationUtils.createAccommodation();
        ObjectNode accommodationJson = AccommodationUtils.createAccommodationJson();

        when(accommodationService.createAccommodationFromJson(any(User.class), any(JsonNode.class))).thenThrow(JsonProcessingException.class);

        AccommodationController accommodationController = new AccommodationController(accommodationService);

        Http.RequestBuilder fakeRequest = new Http.RequestBuilder();
        fakeRequest.bodyJson(accommodationJson);

        Result result = Helpers.invokeWithContext(fakeRequest, accommodationController::createAccommodation);

        assertEquals(BAD_REQUEST, result.status());

        JsonNode responseBody = Json.parse(contentAsString(result));
        assertEquals(ResponseBuilder.MALFORMED_REQUEST_BODY, responseBody.findValue("type").asText());

    }
}
