package unit.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.InterestsController;
import models.Interest;
import models.user.User;
import org.junit.Test;
import org.mockito.Mockito;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import services.InterestsService;
import utils.ResponseBuilder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.mvc.Http.Status.OK;
import static play.mvc.Results.ok;

/**
 * @author Simon Olofsson
 */
public class InterestsControllerTest {

    private InterestsController interestsController;

    @Test
    public void setsMutual() {

        InterestsService mockInterestsService = mock(InterestsService.class);

        User renter = new User("renter@renter.com", "Renter");
        User tenant = new User("tenant@tenant.com", "Tenant");
        Interest interest = new Interest(renter, tenant);

        when(mockInterestsService.setMutual(any(Long.class), any(Long.class), any(Boolean.class))).thenReturn(interest);

        interestsController = new InterestsController(mockInterestsService);

        ObjectNode requestBody = Json.newObject();
        requestBody.put("mutual", "false");

        Http.RequestBuilder fakeRequest = new Http.RequestBuilder();
        fakeRequest.bodyJson(requestBody);

        Result result = Helpers.invokeWithContext(fakeRequest, () -> interestsController.setMutual(1L, 1L));

        assertEquals(OK, result.status());

    }

}
