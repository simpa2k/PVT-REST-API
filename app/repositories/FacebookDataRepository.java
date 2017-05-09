package repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.user.FacebookData;

import java.io.IOException;

/**
 * @author Simon Olofsson
 */
public class FacebookDataRepository {

    private ObjectMapper mapper = new ObjectMapper();

    public FacebookData findByFacebookUserId(String facebookUserId) {
        return FacebookData.findByFacebookUserId(facebookUserId);
    }

    public FacebookData create(JsonNode data) throws JsonProcessingException {

        FacebookData fbData = mapper.treeToValue(data, FacebookData.class);
        save(fbData);

        return fbData;

    }

    public FacebookData update(FacebookData fbData, JsonNode data) throws IOException {

        mapper.readerForUpdating(fbData).readValue(data);
        save(fbData);

        return fbData;

    }

    public void save(FacebookData fbData) {
        fbData.save();
    }
}
