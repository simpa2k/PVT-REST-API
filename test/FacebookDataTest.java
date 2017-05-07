package models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.user.FacebookData;
import org.junit.Test;
import play.Logger;
import play.libs.Json;

import static org.junit.Assert.assertEquals;

/**
 * @author Simon Olofsson
 */
public class FacebookDataTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void deserializesProperly() throws JsonProcessingException {

        ObjectNode facebookData = mapper.createObjectNode();

        facebookData.put("email", "s.olofsson@gmail.com");
        facebookData.put("first_name", "Simon");
        facebookData.put("last_name", "Olofsson");
        facebookData.put("gender", "male");
        facebookData.put("locale", "en_GB");
        facebookData.put("name", "Simon Olofsson");
        facebookData.put("timezone", 2);
        facebookData.put("id", "10154623247991818");

        FacebookData deserialized = mapper.treeToValue(facebookData, FacebookData.class);

        assertEquals("s.olofsson@gmail.com", deserialized.email);
        assertEquals("Simon", deserialized.firstName);
        assertEquals("Olofsson", deserialized.lastName);
        assertEquals("male", deserialized.gender);
        assertEquals("en_GB", deserialized.locale);
        assertEquals("Simon Olofsson", deserialized.name);
        assertEquals(2, deserialized.timezone);
        assertEquals("10154623247991818", deserialized.facebookUserId);

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

        FacebookData facebookData = new FacebookData();

        facebookData.email = "s.olofsson@gmail.com";
        facebookData.firstName = "Simon";
        facebookData.lastName = "Olofsson";
        facebookData.gender = "male";
        facebookData.locale = "en_GB";
        facebookData.name = "Simon Olofsson";
        facebookData.timezone = 2;
        facebookData.facebookUserId = "10154623247991818";

        JsonNode serialized = mapper.valueToTree(facebookData);

        assertEquals(serialized.findValue("email").textValue(), facebookData.email);
        assertEquals(serialized.findValue("first_name").textValue(), facebookData.firstName);
        assertEquals(serialized.findValue("last_name").textValue(), facebookData.lastName);
        assertEquals(serialized.findValue("gender").textValue(), facebookData.gender);
        assertEquals(serialized.findValue("name").textValue(), facebookData.name);

        assertEquals(serialized.findValue("locale"), null);
        assertEquals(serialized.findValue("timezone"), null);
        assertEquals(serialized.findValue("id"), null);

    }
}
