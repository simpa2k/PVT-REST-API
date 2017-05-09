package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.OffsetOutOfRangeException;
import models.accommodation.Accommodation;
import models.accommodation.Address;
import models.user.User;
import play.Logger;
import repositories.AccommodationRepository;
import repositories.AddressRepository;
import scala.Option;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Simon Olofsson
 */
public class AccommodationService {

    private AccommodationRepository accommodationRepository;
    private AddressRepository addressRepository;

    private ObjectMapper mapper;

    @Inject
    public AccommodationService(AccommodationRepository accommodationRepository,
                                AddressRepository addressRepository, ObjectMapper mapper) {

        this.accommodationRepository = accommodationRepository;
        this.addressRepository = addressRepository;

        this.mapper = mapper;

    }

    public List<Accommodation> getSubset(final Option<Integer> count, final Option<Integer> offset,
                                         final Option<Double> rent, final Option<Double> size,
                                         final Option<Boolean> smokingAllowed, final Option<Boolean> animalsAllowed) throws OffsetOutOfRangeException {

        List<Accommodation> accommodation = accommodationRepository.findAccommodation(rent, size, smokingAllowed, animalsAllowed);

        int evaluatedOffset = offset.isDefined() ? offset.get() : 0;
        int evaluatedCount = count.isDefined() && ((count.get() + evaluatedOffset) < accommodation.size()) ? count.get() : accommodation.size();

        if (evaluatedOffset > accommodation.size()) {
            throw new OffsetOutOfRangeException("The offset you have requested is larger than the number of results.");
        }

        return accommodation.subList(evaluatedOffset, evaluatedCount);

    }

    public Accommodation createAccommodationFromJson(User user, JsonNode accommodationJson) throws JsonProcessingException {

        Accommodation accommodation = mapper.treeToValue(accommodationJson, Accommodation.class);
        Address address = accommodation.address;

        accommodation.renter = user;
        addressRepository.save(address);

        Accommodation existing = accommodationRepository.findByRenter(accommodation.renter.id);
        if (existing == null) {

            accommodationRepository.save(accommodation);
            return accommodation;

        }

        return existing;

    }
}
