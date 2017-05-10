package integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.user.FacebookData;
import org.junit.Test;
import repositories.FacebookDataRepository;
import testResources.BaseTest;
import testResources.FacebookUtils;

import java.io.IOException;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Simon Olofsson
 */
public class FacebookDataStackTest extends BaseTest {

    private FacebookDataRepository fbDataRepo = new FacebookDataRepository();
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void savesCreatedFacebookDataToDatabase() throws JsonProcessingException {

        ObjectNode facebookData = FacebookUtils.createFacebookJson();

        FacebookData saved = fbDataRepo.create(facebookData);

        assertNotNull(fbDataRepo.findByFacebookUserId(saved.facebookUserId));

    }

    @Test
    public void updatesFacebookDataFromJson() throws IOException {

        FacebookData original = new FacebookData();
        original.name = "Simon Olofsson";
        original.facebookUserId = "10154623247991818";

        assertNull(original.email);
        assertNull(original.firstName);
        assertNull(original.lastName);
        assertNull(original.gender);
        assertNull(original.locale);
        assertEquals(0, original.timezone);

        ObjectNode facebookData = FacebookUtils.createFacebookJson();
        FacebookData updated = fbDataRepo.update(original, facebookData);

        for (Consumer<FacebookData> assertion : FacebookUtils.getAssertions()) {
            assertion.accept(updated);
        }
    }
}
