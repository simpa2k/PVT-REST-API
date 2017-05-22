package testResources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.user.TenantProfile;
import play.libs.Json;

import static org.junit.Assert.assertEquals;

/**
 * @author Simon Olofsson
 */
public class TenantProfileUtils {

    public static TenantProfile createTenantProfile() {
        return new TenantProfile(5000, 8000, "Hej!");
    }

    public static ObjectNode createTenantProfileJson() {

        ObjectNode profileJson = Json.newObject();

        profileJson.put("maxRent", 5000);
        profileJson.put("maxDeposit", 8000);
        profileJson.put("minSize", 20);
        profileJson.put("description", "Hej!");

        ObjectNode rentalPeriodJson = profileJson.putObject("rentalPeriod");
        rentalPeriodJson.put("start", "2017-05-01");
        rentalPeriodJson.put("end", "2018-05-01");

        return profileJson;

    }

    public static void makeStandardAssertionsAgainstJson(JsonNode profile) {

        assertEquals(5000, profile.findValue("maxRent").asInt());
        assertEquals(8000, profile.findValue("maxDeposit").asInt());
        assertEquals(20, profile.findValue("minSize").asInt());
        assertEquals("Hej!", profile.findValue("description").asText());

        JsonNode rentalPeriod = profile.findValue("rentalPeriod");
        assertEquals("2017-05-01", rentalPeriod.findValue("start").asText());
        assertEquals("2018-05-01", rentalPeriod.findValue("end").asText());

    }

}
