package integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.accommodation.Accommodation;
import models.user.User;
import org.junit.Test;
import repositories.AccommodationRepository;
import repositories.AddressRepository;
import services.AccommodationService;
import testResources.AccommodationUtils;
import testResources.BaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Simon Olofsson
 */
public class AccommodationStackTest extends BaseTest {

    @Test
    public void canHandleCreatingSameAccommodationTwice() throws JsonProcessingException {

        User renter = new User("renter@rent.com", "Renter");
        renter.save();

        ObjectNode accommodationJson = AccommodationUtils.createAccommodationJson();

        AccommodationRepository accommodationRepository = new AccommodationRepository();
        AccommodationService accommodationService = new AccommodationService(accommodationRepository,
                new AddressRepository(), new ObjectMapper());

        Accommodation accommodation = accommodationService.createAccommodationFromJson(renter, accommodationJson);

        assertNotNull(accommodationRepository.findById(accommodation.id));

        ObjectNode accommodationJson2 = AccommodationUtils.createAccommodationJson();
        Accommodation accommodation2 = accommodationService.createAccommodationFromJson(renter, accommodationJson2);

        assertEquals(accommodation, accommodation2);

    }
}
