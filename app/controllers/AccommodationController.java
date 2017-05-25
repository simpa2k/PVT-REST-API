package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import exceptions.OffsetOutOfRangeException;
import models.accommodation.Accommodation;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.Option;
import services.AccommodationService;
import utils.ResponseBuilder;

import javax.inject.Inject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static play.mvc.Results.ok;

/**
 * @author Simon Olofsson
 */
@Security.Authenticated(Security.Authenticator.class)
public class AccommodationController extends Controller{

    private AccommodationService accommodationService;

    @Inject
	public AccommodationController(AccommodationService accommodationService) {
		this.accommodationService = accommodationService;
	}

	public Result get(final Option<Integer> count, final Option<Integer> offset,
					  final Option<Double> rent, final Option<Double> size,
					  final Option<Boolean> smokingAllowed, final Option<Boolean> animalsAllowed){

        try {

            List<Accommodation> accommodation = accommodationService.getSubset(count, offset, rent, size, smokingAllowed, animalsAllowed);
            return ResponseBuilder.buildOKList(accommodation);

        } catch (OffsetOutOfRangeException e) {
            return ResponseBuilder.buildBadRequest("The offset you have requested is larger than the number of results.", ResponseBuilder.OUT_OF_RANGE);
        }
	}
	
	public Result put(){
		JsonNode body;
		try {
			body = request().body().asJson();
		} catch (NullPointerException e) {
			Result result = ResponseBuilder.buildBadRequest("Request body required", ResponseBuilder.MALFORMED_REQUEST_BODY);
			return result;
		}

		return ok("description is :"+body.get("description").asText());
	}

    public Result createAccommodation() {

	    JsonNode requestBody = request().body().asJson();

		if (requestBody == null || requestBody.size() == 0) {
			return ResponseBuilder.buildBadRequest("Non-empty request body required.", ResponseBuilder.MALFORMED_REQUEST_BODY);
		}

        try {
            accommodationService.createAccommodationFromJson(SecurityController.getUser(), requestBody);
            
        } catch (JsonProcessingException e) {

            return ResponseBuilder.buildBadRequest("Could not parse request body.", ResponseBuilder.MALFORMED_REQUEST_BODY);

        }

        return noContent();

    }

    public Result deleteAccommodation(long accommodationId) {

		Accommodation accommodation = accommodationService.deleteAccommodation(accommodationId);

		if (accommodation == null) {
			return ResponseBuilder.buildNotFound("Could not find any accommodation with that id.");
		}

		return ResponseBuilder.buildOKObject(accommodation);

	}
}
