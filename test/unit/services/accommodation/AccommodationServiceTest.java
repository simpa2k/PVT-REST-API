package unit.services.accommodation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.accommodation.Accommodation;
import models.accommodation.Address;
import org.junit.Test;
import org.mockito.Mockito;
import repositories.AccommodationRepository;
import repositories.AddressRepository;
import services.AccommodationService;
import testResources.AccommodationUtils;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Simon Olofsson
 */
public class AccommodationServiceTest {

    @Test
    public void createsAccommodation() throws JsonProcessingException {

        AccommodationRepository accommodationRepository = mock(AccommodationRepository.class);
        AddressRepository addressRepository = mock(AddressRepository.class);

        ObjectMapper mapper = mock(ObjectMapper.class);

        Mockito.doNothing().when(accommodationRepository).save(any(Accommodation.class));
        Mockito.doNothing().when(addressRepository).save(any(Address.class));
        when(mapper.treeToValue(any(JsonNode.class), eq(Accommodation.class))).thenReturn(AccommodationUtils.createAccommodation());

        AccommodationService accommodationService = new AccommodationService(accommodationRepository, addressRepository, mapper);

        Accommodation accommodation = accommodationService.createAccommodationFromJson(null, AccommodationUtils.createAccommodationJson());

        AccommodationUtils.performStandardAssertions(accommodation);

    }
}
