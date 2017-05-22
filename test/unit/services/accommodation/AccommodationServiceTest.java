package unit.services.accommodation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.accommodation.Accommodation;
import models.accommodation.Address;
import models.user.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import play.Configuration;
import play.test.WithApplication;
import repositories.AccommodationRepository;
import repositories.AddressRepository;
import repositories.RentalPeriodRepository;
import repositories.UsersRepository;
import services.AccommodationService;
import testResources.AccommodationUtils;
import testResources.TestData;

import java.text.ParseException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Simon Olofsson
 */
public class AccommodationServiceTest extends WithApplication {

    private Configuration config;

    @Before
    public void setup() {

        TestData testData = app.injector().instanceOf(TestData.class);
        config = testData.getConfig();

    }

    @Test
    public void createsAccommodation() throws JsonProcessingException, ParseException {

        AccommodationRepository accommodationRepository = mock(AccommodationRepository.class);
        AddressRepository addressRepository = mock(AddressRepository.class);
        UsersRepository usersRepository = mock(UsersRepository.class);
        RentalPeriodRepository rentalPeriodRepository = mock(RentalPeriodRepository.class);

        ObjectMapper mapper = mock(ObjectMapper.class);

        Mockito.doNothing().when(accommodationRepository).save(any(Accommodation.class));
        Mockito.doNothing().when(addressRepository).save(any(Address.class));
        when(mapper.treeToValue(any(JsonNode.class), eq(Accommodation.class))).thenReturn(AccommodationUtils.createAccommodation());

        AccommodationService accommodationService = new AccommodationService(accommodationRepository,
                addressRepository, usersRepository, rentalPeriodRepository, mapper, config);

        User renter = new User("renter@renter.com", "Renter");

        Accommodation accommodation = accommodationService.createAccommodationFromJson(renter, AccommodationUtils.createAccommodationJson());

        AccommodationUtils.performStandardAssertions(accommodation);

    }
}
