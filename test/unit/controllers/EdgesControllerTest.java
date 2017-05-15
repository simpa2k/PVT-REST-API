package unit.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.EdgesController;
import models.Edge;
import models.user.User;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import services.EdgesService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.mvc.Http.Status.OK;

/**
 * @author Simon Olofsson
 */
public class EdgesControllerTest {

    private EdgesController edgesController;

    @Test
    public void setsMutual() {

        EdgesService mockEdgesService = mock(EdgesService.class);

        User renter = new User("renter@renter.com", "Renter");
        User tenant = new User("tenant@tenant.com", "Tenant");
        Edge edge = new Edge(renter, tenant);

        when(mockEdgesService.setMutual(any(Long.class), any(Long.class), any(Boolean.class))).thenReturn(edge);

        edgesController = new EdgesController(mockEdgesService);

        ObjectNode requestBody = Json.newObject();
        requestBody.put("active", "false");

        Http.RequestBuilder fakeRequest = new Http.RequestBuilder();
        fakeRequest.bodyJson(requestBody);

        Result result = Helpers.invokeWithContext(fakeRequest, () -> edgesController.setMutual(1L, 1L));

        assertEquals(OK, result.status());

    }

}
