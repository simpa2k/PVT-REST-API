package services.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.user.User;
import org.junit.Test;
import repositories.accommodation.AccommodationRepository;
import repositories.facebookData.FacebookDataRepository;
import repositories.interests.InterestsRepository;
import repositories.users.UsersRepository;
import services.users.UsersService;
import testResources.BaseTest;

import static org.junit.Assert.assertNotNull;

/**
 * @author Simon Olofsson
 */
public class UsersServiceTest extends BaseTest {

    private UsersService usersService = new UsersService(new UsersRepository(),
                                                         new AccommodationRepository(),
                                                         new InterestsRepository(),
                                                         new FacebookDataRepository());

    @Test
    public void savesUserToDatabaseWhenCreatingFromFacebookData() {

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
