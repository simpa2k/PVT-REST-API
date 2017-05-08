package unit.services.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.NoEmailFoundException;
import models.Interest;
import models.RentalPeriod;
import models.user.TenantProfile;
import models.user.User;
import org.junit.Test;
import org.mockito.Mockito;
import play.libs.Json;
import repositories.RentalPeriodRepository.RentalPeriodRepository;
import repositories.accommodation.AccommodationRepository;
import repositories.facebookData.FacebookDataRepository;
import repositories.interests.InterestsRepository;
import repositories.tenantProfile.TenantProfileRepository;
import repositories.users.UsersRepository;
import services.users.UsersService;
import testResources.BaseTest;

import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Simon Olofsson
 */
public class UsersServiceTest extends BaseTest {

    @Test
    public void setsTenantProfile() throws JsonProcessingException {

        UsersRepository usersRepository = mock(UsersRepository.class);
        AccommodationRepository accommodationRepository = mock(AccommodationRepository.class);
        InterestsRepository interestsRepository = mock(InterestsRepository.class);
        FacebookDataRepository facebookDataRepository = mock(FacebookDataRepository.class);
        TenantProfileRepository tenantProfileRepository = mock(TenantProfileRepository.class);
        RentalPeriodRepository rentalPeriodRepository = mock(RentalPeriodRepository.class);

        ObjectMapper mapper = mock(ObjectMapper.class);

        Mockito.doNothing().when(usersRepository).save(any(User.class));

        TenantProfile profile = new TenantProfile(5000, 8000, "Hej!");

        when(mapper.treeToValue(any(ObjectNode.class), eq(TenantProfile.class))).thenReturn(profile);

        UsersService usersService = new UsersService(usersRepository,
                accommodationRepository,
                interestsRepository,
                facebookDataRepository,
                tenantProfileRepository,
                rentalPeriodRepository,
                mapper);

        User kalle = new User();

        ObjectNode profileJson = Json.newObject();

        profileJson.put("maxRent", 5000);
        profileJson.put("maxDeposit", 8000);
        profileJson.put("description", "Hej!");

        ObjectNode rentalPeriodJson = profileJson.putObject("rentalPeriod");
        rentalPeriodJson.put("start", "2017-05-01");
        rentalPeriodJson.put("end", "2018-05-01");

        TenantProfile setProfile = usersService.setProfile(kalle, profileJson);

        assertEquals(kalle.tenantProfile, setProfile);

    }
}
