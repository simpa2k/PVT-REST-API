package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Interest;
import models.user.Renter;
import models.user.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.Option;
import services.InterestsService;
import exceptions.OffsetOutOfRangeException;
import services.UsersService;
import utils.ResponseBuilder;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Simon Olofsson
 */
@Security.Authenticated(Secured.class)
public class InterestsController extends Controller {

    private InterestsService interestsService;

    @Inject
    public InterestsController(InterestsService interestsService) {
        this.interestsService = interestsService;
    }

    public Result get(Option<Integer> count, Option<Integer> offset,
                      Option<Long> tenantId, Option<Long> renterId, Option<Boolean> mutual) {

        try {

            List<Interest> interests = interestsService.getSubset(count, offset, tenantId, renterId, mutual);
            return ResponseBuilder.buildOKList(interests);

        } catch(OffsetOutOfRangeException e) {
            return ResponseBuilder.buildBadRequest("The offset you have requested is larger than the number of results.", ResponseBuilder.OUT_OF_RANGE);
        }
    }

    public Result create() {

        JsonNode body = request().body().asJson();

        try {

            User renter = SecurityController.getUser();
            interestsService.addInterests(renter, (ArrayNode) body);

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

            String mutual = body.findValue("mutual").textValue();

            if (!mutual.equals("true") && !mutual.equals("false")) {
                return ResponseBuilder.buildBadRequest("Attribute 'mutual' must be set to either 'true' or 'false'.", ResponseBuilder.ILLEGAL_ARGUMENT);
            }

            Interest interest = interestsService.setMutual(renterId, tenantId, Boolean.parseBoolean(mutual));

            return ResponseBuilder.buildOKObject(interest);

        } catch (ClassCastException cce) {
            return ResponseBuilder.buildBadRequest("User must be a valid renter.", ResponseBuilder.NO_SUCH_ENTITY);
        }
    }

    public Result withdrawInterest(long tenantId, long accommodationId) {

        if (((User) ctx().args.get("user")).id != tenantId) {
            return ResponseBuilder.buildUnauthorizedRequest("Owner of token and owner of tenant id do not match. A user may only withdraw own interests.");
        }

        interestsService.withdrawInterest(tenantId, accommodationId);

        return noContent();

    }
}
