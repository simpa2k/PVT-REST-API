package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.NoEmailFoundException;
import models.user.FacebookData;
import models.user.User;
import org.junit.Test;
import repositories.RentalPeriodRepository;
import repositories.AccommodationRepository;
import repositories.FacebookDataRepository;
import repositories.EdgesRepository;
import repositories.TenantProfileRepository;
import repositories.UsersRepository;
import services.UsersService;
import testResources.BaseTest;
import testResources.FacebookUtils;

import static org.junit.Assert.assertNotNull;

/**
 * @author Simon Olofsson
 */
public class UserStackTest extends BaseTest {

    UsersRepository usersRepository = new UsersRepository();
    FacebookDataRepository facebookDataRepository = new FacebookDataRepository();

    UsersService usersService = new UsersService(new UsersRepository(),
            new AccommodationRepository(),
            new EdgesRepository(),
            new FacebookDataRepository(),
            new TenantProfileRepository(),
            new RentalPeriodRepository(),
            new ObjectMapper());


    @Test
    public void canSaveUserWithMinimalData() {

        FacebookData facebookData = FacebookUtils.createFacebookData();
        facebookDataRepository.save(facebookData);

        User user = new User();

        user.setEmailAddress(facebookData.email);
        user.facebookData = facebookData;

        usersRepository.save(user);

        assertNotNull(usersRepository.findByEmailAddress(user.facebookData.email));

    }

    @Test
    public void savesUserToDatabaseWhenCreatingFromFacebookData() throws NoEmailFoundException {

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode facebookData = mapper.createObjectNode();

        facebookData.put("email", "s.olofsson@gmail.com");
        facebookData.put("first_name", "Simon");
        facebookData.put("last_name", "Olofsson");
        facebookData.put("gender", "male");
        facebookData.put("locale", "en_GB");
        facebookData.put("name", "Simon Olofsson");
        facebookData.put("timezone", 2);
        facebookData.put("id", "10154623247991818");

        User user = usersService.createFromFacebookData(facebookData);

    }

}
