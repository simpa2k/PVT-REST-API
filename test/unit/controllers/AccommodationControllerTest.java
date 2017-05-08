package unit.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.SecurityController;
import controllers.routes;
import models.user.User;
import org.junit.Test;
import org.mockito.Mock;
import play.mvc.Http;
import play.mvc.Result;
import services.accommodation.AccommodationService;
import testResources.AccommodationUtils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static play.mvc.Http.Status.NO_CONTENT;
import static play.test.Helpers.route;

/**
 * @author Simon Olofsson
 */
public class AccommodationControllerTest {

    @Mock
    private AccommodationService accommodationService;

    @Test
    public void testReturnsOKOnCorrectRequest() throws JsonProcessingException {

    }
}
