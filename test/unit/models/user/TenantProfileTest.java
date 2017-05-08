package unit.models.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.user.TenantProfile;
import org.junit.Test;
import play.libs.Json;
import testResources.TenantProfileUtils;

import static org.junit.Assert.assertEquals;

/**
 * @author Simon Olofsson
 */
public class TenantProfileTest {

    @Test
    public void deserializesProperly() throws JsonProcessingException {

        ObjectNode profileJson = TenantProfileUtils.createTenantProfileJson();

        // Not testing serialization of rentalPeriod as this is a unit test.
        profileJson.remove("rentalPeriod");

        ObjectMapper mapper = new ObjectMapper();
        TenantProfile profile = mapper.treeToValue(profileJson, TenantProfile.class);

        assertEquals(profile.maxRent, 5000);
        assertEquals(profile.maxDeposit, 8000);
        assertEquals(profile.description, "Hej!");

    }
}
