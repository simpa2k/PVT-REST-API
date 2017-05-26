package integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.accommodation.Accommodation;
import models.accommodation.Address;
import models.user.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import repositories.AccommodationRepository;
import repositories.AddressRepository;
import repositories.RentalPeriodRepository;
import repositories.UsersRepository;
import scala.tools.nsc.backend.icode.Primitives;
import services.AccommodationService;
import services.AddressService;
import testResources.AccommodationUtils;
import testResources.BaseTest;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * @author Simon Olofsson
 */
public class AccommodationStackTest extends BaseTest {

    private AccommodationRepository accommodationRepository = new AccommodationRepository();
    private AddressRepository addressRepository = new AddressRepository();
    private UsersRepository usersRepository = new UsersRepository();
    private RentalPeriodRepository rentalPeriodRepository = new RentalPeriodRepository();

    private AccommodationService accommodationService;

    @Before
    public void setup() {

        AddressService mockAddressService = mock(AddressService.class);
        Mockito.doNothing().when(mockAddressService).gatherData(any(Address.class));

        accommodationService = new AccommodationService(accommodationRepository,
                addressRepository, usersRepository, rentalPeriodRepository, new ObjectMapper(), config,
                mockAddressService);

    }

    @Test
    public void savesAccommodationToDatabase() throws JsonProcessingException, ParseException {

        ObjectMapper mapper = new ObjectMapper();

        /*AccommodationService accommodationService = new AccommodationService(accommodationRepository,
                addressRepository,
                usersRepository,
                rentalPeriodRepository,
                mapper,
                config, );*/

        User renter = new User("renter@renter.com", "Renter");

        usersRepository.save(renter);

        Accommodation accommodation = accommodationService.createAccommodationFromJson(renter, AccommodationUtils.createAccommodationJson());

        AccommodationUtils.performStandardAssertions(accommodation);

        assertNotNull(accommodationRepository.findById(accommodation.id));
        assertNotNull(accommodationRepository.findByRenter(renter.id));

    }

    @Test
    public void canHandleCreatingSameAccommodationTwice() throws JsonProcessingException {

        User renter = new User("renter@rent.com", "Renter");
        renter.save();

        ObjectNode accommodationJson = AccommodationUtils.createAccommodationJson();

        AccommodationRepository accommodationRepository = new AccommodationRepository();
        /*AccommodationService accommodationService = new AccommodationService(accommodationRepository,
                new AddressRepository(), usersRepository, rentalPeriodRepository, new ObjectMapper(), config, );*/

        Accommodation accommodation = accommodationService.createAccommodationFromJson(renter, accommodationJson);

        assertNotNull(accommodationRepository.findById(accommodation.id));

        ObjectNode accommodationJson2 = AccommodationUtils.createAccommodationJson();
        Accommodation accommodation2 = accommodationService.createAccommodationFromJson(renter, accommodationJson2);

        assertEquals(accommodation, accommodation2);

    }

    @Test
    public void canHandleNullRentalPeriod() {
        // Implement this.
    }

    @Test
    public void deletesAccommodation() throws ParseException {

        AccommodationUtils.AccommodationData accommodationData = AccommodationUtils.createAccommodationData();
        accommodationData.saveAll(usersRepository, addressRepository, rentalPeriodRepository, accommodationRepository);

        Accommodation accommodation = accommodationData.getAccommodation();

        Accommodation deleted = accommodationService.deleteAccommodation(accommodation.id);

        assertEquals(accommodation, deleted);
        assertNull(accommodationRepository.findById(accommodation.id));

    }
}
