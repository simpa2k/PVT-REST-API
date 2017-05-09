package services.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import exceptions.NoEmailFoundException;
import models.Interest;
import models.user.Renter;
import models.user.TenantProfile;
import models.user.User;
import repositories.RentalPeriodRepository.RentalPeriodStorage;
import repositories.accommodation.AccommodationStorage;
import repositories.facebookData.FacebookDataStorage;
import repositories.interests.InterestStorage;
import repositories.tenantProfile.TenantProfileRepository;
import repositories.tenantProfile.TenantProfileStorage;
import repositories.users.UserStorage;

import javax.inject.Inject;

/**
 * @author Simon Olofsson
 */
public class UsersService {

    private UserStorage usersRepository;
    private AccommodationStorage accommodationRepository;
    private InterestStorage interestsRepository;
    private FacebookDataStorage facebookDataRepository;
    private TenantProfileStorage tenantProfileRepository;
    private RentalPeriodStorage rentalPeriodRepository;

    private ObjectMapper mapper;

    @Inject
    public UsersService(UserStorage usersRepository,
                        AccommodationStorage accommodationRepository,
                        InterestStorage interestsRepository,
                        FacebookDataStorage facebookDataRepository,
                        TenantProfileStorage tenantProfileRepository,
                        RentalPeriodStorage rentalPeriodRepository,
                        ObjectMapper mapper) {

        this.usersRepository = usersRepository;
        this.accommodationRepository = accommodationRepository;
        this.interestsRepository = interestsRepository;
        this.facebookDataRepository = facebookDataRepository;
        this.tenantProfileRepository = tenantProfileRepository;
        this.rentalPeriodRepository = rentalPeriodRepository;

    }

    public Interest setMutualInterest(Renter renter, long tenantId, boolean mutual) {

        //Interest interest = interestsRepository.findInterest(tenantId, renter.accommodation.id);
        //interest.mutual = mutual;

        //interestsRepository.save(interest);

        //return interest;
        return null;

    }

    public void withdrawInterest(long tenantId, long accommodationId) {

        Interest interest = interestsRepository.findInterest(tenantId, accommodationId);

        if (interest != null) {
            interestsRepository.delete(interest);
        }
    }

    public User createFromFacebookData(JsonNode facebookData) throws NoEmailFoundException {

        if  (facebookData.findValue("email") == null) {
            throw new NoEmailFoundException("Email was null. Email is required.");
        }

        String email = facebookData.findValue("email").textValue();
        User user = usersRepository.findByEmailAddress(email);

        if (user == null) {
            user = new User();
        }

        String facebookUserId = facebookData.findValue("id").textValue();
        user.setEmailAddress(email);
        user.facebookData = facebookDataRepository.findByFacebookUserId(facebookUserId);

        usersRepository.save(user);

        return user;

    }

    public String getToken(User user) {

        String userToken = user.createToken();
        usersRepository.save(user);

        return userToken;

    }

    public void deleteToken(User user) {

        user.deleteAuthToken();
        user.save();

    }

    public User findByEmailAddressAndPassword(String emailAddress, String password) {
        return usersRepository.findByEmailAddressAndPassword(emailAddress, password);
    }

    public TenantProfile setProfile(User user, JsonNode profileJson) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        TenantProfile profile = mapper.treeToValue(profileJson, TenantProfile.class);

        rentalPeriodRepository.save(profile.rentalPeriod);
        tenantProfileRepository.save(profile);

        user.tenantProfile = profile;
        usersRepository.save(user);

        return profile;

    }
}
