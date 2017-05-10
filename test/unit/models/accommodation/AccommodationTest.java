package unit.models.accommodation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.accommodation.Accommodation;
import org.junit.Test;
import testResources.AccommodationUtils;

import java.text.ParseException;

/**
 * @author Simon Olofsson
 */
public class AccommodationTest {

    @Test
    public void deserializesProperly() throws JsonProcessingException, ParseException {

        ObjectNode accommodationJson = AccommodationUtils.createAccommodationJson();

        ObjectMapper mapper = new ObjectMapper();
        Accommodation accommodation = mapper.treeToValue(accommodationJson, Accommodation.class);

        AccommodationUtils.performStandardAssertions(accommodation);

    }
}
