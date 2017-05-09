package integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.accommodation.Accommodation;
import org.junit.Test;
import repositories.AccommodationRepository;
import repositories.AddressRepository;
import services.AccommodationService;
import testResources.AccommodationUtils;
import testResources.BaseTest;

import static junit.framework.TestCase.assertNotNull;

/**
 * @author Simon Olofsson
 */
public class SaveAccommodation extends BaseTest {

    @Test
    public void savesAccommodationToDatabase() throws JsonProcessingException {

        AccommodationRepository accommodationRepository = new AccommodationRepository();
        AddressRepository addressRepository = new AddressRepository();

        ObjectMapper mapper = new ObjectMapper();

        AccommodationService accommodationService = new AccommodationService(accommodationRepository, addressRepository, mapper);

        Accommodation accommodation = accommodationService.createAccommodationFromJson(null, AccommodationUtils.createAccommodationJson());

        AccommodationUtils.performStandardAssertions(accommodation);
        assertNotNull(accommodationRepository.findById(accommodation.id));

    }
}
