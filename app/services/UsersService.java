package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.NoEmailFoundException;
import models.user.TenantProfile;
import models.user.User;

import scala.Option;
import repositories.RentalPeriodRepository;
import repositories.AccommodationRepository;
import repositories.FacebookDataRepository;
import repositories.EdgesRepository;
import repositories.TenantProfileRepository;
import repositories.UsersRepository;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Simon Olofsson
 */
public class UsersService {

    private UsersRepository usersRepository;
    private AccommodationRepository accommodationRepository;
    private EdgesRepository interestsRepository;
    private FacebookDataRepository facebookDataRepository;
    private TenantProfileRepository tenantProfileRepository;
    private RentalPeriodRepository rentalPeriodRepository;

    private ObjectMapper mapper;

    @Inject
    public UsersService(UsersRepository usersRepository,
                        AccommodationRepository accommodationRepository,
                        EdgesRepository interestsRepository,
                        FacebookDataRepository facebookDataRepository,
                        TenantProfileRepository tenantProfileRepository,
                        RentalPeriodRepository rentalPeriodRepository,
                        ObjectMapper mapper) {

        this.usersRepository = usersRepository;
        this.accommodationRepository = accommodationRepository;
        this.interestsRepository = interestsRepository;
        this.facebookDataRepository = facebookDataRepository;
        this.tenantProfileRepository = tenantProfileRepository;
        this.rentalPeriodRepository = rentalPeriodRepository;

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

    public List<User> getSubset(final String authToken,
                                final Option<Integer> maxRent, final Option<Integer> maxDeposit,
                                final Option<String> start, final Option<String> end)  {

        List<User> users = usersRepository.findUsers(authToken, maxRent, maxDeposit, start, end);



        return users;

    }

}
