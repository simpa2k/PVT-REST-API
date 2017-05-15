package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Edge;
import models.user.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.Option;
import services.EdgesService;
import exceptions.OffsetOutOfRangeException;
import utils.ResponseBuilder;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Simon Olofsson
 */
@Security.Authenticated(Secured.class)
public class EdgesController extends Controller {

    private EdgesService edgesService;

    @Inject
    public EdgesController(EdgesService edgesService) {
        this.edgesService = edgesService;
    }

    public Result get(Option<Integer> count, Option<Integer> offset,
                      Option<Long> tenantId, Option<Long> renterId, Option<Boolean> mutual) {

        try {

            List<Edge> interests = edgesService.getSubset(count, offset, tenantId, renterId, mutual);
            return ResponseBuilder.buildOKList(interests);

        } catch(OffsetOutOfRangeException e) {
            return ResponseBuilder.buildBadRequest("The offset you have requested is larger than the number of results.", ResponseBuilder.OUT_OF_RANGE);
        }
    }

    public Result create() {

        JsonNode body = request().body().asJson();

        try {

            User renter = SecurityController.getUser();
            edgesService.addEdge(renter, body);

            return noContent();

        } catch (IllegalArgumentException iae) {
            return ResponseBuilder.buildBadRequest(iae.getMessage(), ResponseBuilder.ILLEGAL_ARGUMENT);
        } catch (ClassCastException cce) {
            return ResponseBuilder.buildBadRequest("User ids must be passed as an array.", ResponseBuilder.MALFORMED_REQUEST_BODY);
        }
    }

    public Result setMutual(long renterId, long tenantId) {

        JsonNode body = request().body().asJson();

        try {

            String mutual = body.findValue("active").textValue();

            if (!mutual.equals("true") && !mutual.equals("false")) {
                return ResponseBuilder.buildBadRequest("Attribute 'active' must be set to either 'true' or 'false'.", ResponseBuilder.ILLEGAL_ARGUMENT);
            }

            Edge interest = edgesService.setMutual(renterId, tenantId, Boolean.parseBoolean(mutual));

            return ResponseBuilder.buildOKObject(interest);

        } catch (ClassCastException cce) {
            return ResponseBuilder.buildBadRequest("User must be a valid renter.", ResponseBuilder.NO_SUCH_ENTITY);
        }
    }

    public Result withdrawInterest(long tenantId, long accommodationId) {

        if (((User) ctx().args.get("user")).id != tenantId) {
            return ResponseBuilder.buildUnauthorizedRequest("Owner of token and owner of tenant id do not match. A user may only withdraw own interests.");
        }

        edgesService.withdrawInterest(tenantId, accommodationId);

        return noContent();

    }
}
