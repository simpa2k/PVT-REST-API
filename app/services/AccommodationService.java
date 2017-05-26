package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.OffsetOutOfRangeException;
import models.RentalPeriod;
import models.accommodation.Accommodation;
import models.accommodation.Address;
import models.user.User;
import play.Configuration;
import play.Logger;
import repositories.AccommodationRepository;
import repositories.AddressRepository;
import repositories.RentalPeriodRepository;
import repositories.UsersRepository;
import scala.Option;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Simon Olofsson
 */
public class AccommodationService {

    private AccommodationRepository accommodationRepository;
    private AddressRepository addressRepository;
    private UsersRepository usersRepository;
    private RentalPeriodRepository rentalPeriodRepository;

    private AddressService addressService;

    private ObjectMapper mapper;

    @Inject
    public AccommodationService(AccommodationRepository accommodationRepository,
                                AddressRepository addressRepository,
                                UsersRepository usersRepository,
                                RentalPeriodRepository rentalPeriodRepository,
                                ObjectMapper mapper,
                                Configuration configuration, AddressService addressService) {

        this.accommodationRepository = accommodationRepository;
        this.addressRepository = addressRepository;
        this.usersRepository = usersRepository;
        this.rentalPeriodRepository = rentalPeriodRepository;

        this.addressService = addressService;

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
	    Logger.info("In createAccommodationFromJson");

        if (user == null) {
            throw new IllegalArgumentException("User was null. Accommodation must be associated with a user.");
        }
	    
        Accommodation accommodation = mapper.treeToValue(accommodationJson, Accommodation.class);
        Logger.debug("accommodation created");

        Address address = accommodation.address;
        addressService.gatherData(address);

        RentalPeriod rentalPeriod = accommodation.rentalPeriod;
        Logger.debug("rentalPeriod: "+rentalPeriod.start);
        accommodation.renter = user;

        Logger.debug("tets");
        String sName=address.streetName;
        int sNumber=address.streetNumber;
        char sLetter=address.streetNumberLetter;
        Logger.debug(sLetter+"");
        Address inList=getInList(sName,sNumber,sLetter);
	    Logger.debug("2");
	    if(inList!=null){
	    	Logger.debug("address already in db");
	    	address=inList;
	    	accommodation.address=inList;
	    }
	    Accommodation existing = accommodationRepository.findByRenter(accommodation.renter.id);
        if (existing == null) {
        	Logger.debug("No earlier accommodation");
            printAddress(address);
            addressRepository.save(address);

            Logger.debug("saved Address");
            rentalPeriodRepository.save(rentalPeriod);
	        Logger.debug("saved rentalPeriod");
	        accommodationRepository.save(accommodation);
	        Logger.debug("saved Accommodation");
	        user.accommodation = accommodation;
	        Logger.debug("set Accommodation for user: "+user.fullName);
            usersRepository.save(user);
            Logger.debug("saved User");

            return accommodation;

        }

        return existing;

    }

    private Address getInList(String s, int n, char c){
    	Logger.debug("getInList");
    	Address a;
	    if(c==0)
		    a=Address.findByStreet(s, n);
	    else{
		    a=Address.findByStreet(s, n, c);
	    }

    	return a;
    }

    private void printAddress(Address a){
        Logger.debug("===============");
        Logger.debug(a.streetName);
        Logger.debug(a.streetNumber+"");
        Logger.debug(a.latitude+"");
        Logger.debug(a.longitude+"");
        Logger.debug(a.area);
    }

    public Accommodation deleteAccommodation(long accommodationId) {

        Accommodation accommodation = accommodationRepository.findById(accommodationId);
        User renter = accommodation.renter;

        renter.accommodation = null;
        usersRepository.save(renter);

        accommodationRepository.delete(accommodation);

        return accommodation;

    }
}
