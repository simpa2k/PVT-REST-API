package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import exceptions.OffsetOutOfRangeException;
import models.accommodation.Accommodation;
import play.mvc.Controller;
import play.mvc.Result;
import scala.Option;
import services.AccommodationService;
import utils.ResponseBuilder;

import javax.inject.Inject;
import java.util.List;

import static play.mvc.Results.ok;

/**
 * @author Simon Olofsson
 */
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

        try {
            accommodationService.createAccommodationFromJson(FacebookSecurityController.getUser(), requestBody);
        } catch (JsonProcessingException e) {
            return ResponseBuilder.buildBadRequest("Could not parse request body", ResponseBuilder.MALFORMED_REQUEST_BODY);
        }

        return noContent();

    }
}
