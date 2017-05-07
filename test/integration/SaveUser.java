package integration;

import models.user.FacebookData;
import models.user.User;
import org.junit.Test;
import repositories.facebookData.FacebookDataRepository;
import repositories.users.UsersRepository;
import testResources.BaseTest;
import testResources.FacebookUtils;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertNotNull;

/**
 * @author Simon Olofsson
 */
public class SaveUser extends BaseTest {

    UsersRepository usersRepository = new UsersRepository();
    FacebookDataRepository facebookDataRepository = new FacebookDataRepository();

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
}
