package unit.models.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.user.FacebookData;
import org.junit.Test;
import testResources.FacebookUtils;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

/**
 * @author Simon Olofsson
 */
public class FacebookDataTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void deserializesProperly() throws JsonProcessingException {

        ObjectNode facebookData = FacebookUtils.createFacebookJson();

        FacebookData deserialized = mapper.treeToValue(facebookData, FacebookData.class);

        for (Consumer<FacebookData> assertion : FacebookUtils.getAssertions()) {
            assertion.accept(deserialized);
        }
    }

    @Test
    public void deserializesProperlyWithMissingValues() throws JsonProcessingException {

        ObjectNode facebookData = mapper.createObjectNode();

        facebookData.put("name", "Simon Olofsson");
        facebookData.put("id", "10154623247991818");

        FacebookData deserialized = mapper.treeToValue(facebookData, FacebookData.class);

        assertEquals(null, deserialized.email);
        assertEquals(null, deserialized.firstName);
        assertEquals(null, deserialized.lastName);
        assertEquals(null, deserialized.gender);
        assertEquals(null, deserialized.locale);
        assertEquals("Simon Olofsson", deserialized.name);
        assertEquals(0, deserialized.timezone);
        assertEquals("10154623247991818", deserialized.facebookUserId);

    }

    @Test
    public void serializesProperly() {

        FacebookData facebookData = FacebookUtils.createFacebookData();

        JsonNode serialized = mapper.valueToTree(facebookData);

        for (BiConsumer<JsonNode, FacebookData> assertion : FacebookUtils.getJsonComparisonAssertions()) {
            assertion.accept(serialized, facebookData);
        }
    }
}
