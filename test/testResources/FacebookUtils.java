package testResources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.user.FacebookData;
import play.libs.Json;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

/**
 * @author Simon Olofsson
 */
public class FacebookUtils {

    public static FacebookData createFacebookData() {

        return new FacebookData("10154623247991818",
                "s.olofsson@gmail.com", "" +
                "Simon", "Olofsson", "" +
                "male",
                "en_GB",
                2);

    }

    public static ObjectNode createFacebookJson() {

        ObjectNode facebookData = Json.newObject();

        facebookData.put("email", "s.olofsson@gmail.com");
        facebookData.put("first_name", "Simon");
        facebookData.put("last_name", "Olofsson");
        facebookData.put("gender", "male");
        facebookData.put("locale", "en_GB");
        facebookData.put("name", "Simon Olofsson");
        facebookData.put("timezone", 2);
        facebookData.put("id", "10154623247991818");

        return facebookData;

    }

    public static List<Consumer<FacebookData>> getAssertions() {

        return Arrays.asList(

            fbData -> assertEquals("s.olofsson@gmail.com", fbData.email),
            fbData -> assertEquals("Simon", fbData.firstName),
            fbData -> assertEquals("Olofsson", fbData.lastName),
            fbData -> assertEquals("male", fbData.gender),
            fbData -> assertEquals("en_GB", fbData.locale),
            fbData -> assertEquals("Simon Olofsson", fbData.name),
            fbData -> assertEquals(2, fbData.timezone),
            fbData -> assertEquals("10154623247991818", fbData.facebookUserId)

        );
    }

    public static List<BiConsumer<JsonNode, FacebookData>> getJsonComparisonAssertions() {

        return Arrays.asList(

                (json, fbData) -> assertEquals(fbData.email, json.findValue("email").textValue()),
                (json, fbData) -> assertEquals(fbData.firstName, json.findValue("firstName").textValue()),
                (json, fbData) -> assertEquals(fbData.lastName, json.findValue("lastName").textValue()),
                (json, fbData) -> assertEquals(fbData.gender, json.findValue("gender").textValue()),
                (json, fbData) -> assertEquals(fbData.name, json.findValue("name").textValue()),
                (json, fbData) -> assertEquals(null, json.findValue("locale")),
                (json, fbData) -> assertEquals(null, json.findValue("timezone")),
                (json, fbData) -> assertEquals(null, json.findValue("id"))

        );

    }
}
