package repositories.facebookData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.user.FacebookData;
import org.junit.Test;
import testResources.BaseTest;

import static org.junit.Assert.assertNotNull;

/**
 * @author Simon Olofsson
 */
public class FacebookDataRepositoryTest extends BaseTest {

    private FacebookDataRepository fbDataRepo = new FacebookDataRepository();

    @Test
    public void savesCreatedFacebookDataToDatabase() throws JsonProcessingException {

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

        FacebookData saved = fbDataRepo.create(facebookData);

        assertNotNull(fbDataRepo.findByFacebookUserId(saved.facebookUserId));

    }
}
