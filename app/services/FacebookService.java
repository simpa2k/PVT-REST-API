package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import models.user.FacebookData;
import play.Logger;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import repositories.FacebookDataRepository;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * @author Simon Olofsson
 */
public class FacebookService {

    private WSClient ws;
    private FacebookDataRepository facebookDataRepository;

    private static final String FIELDS = "email,first_name,last_name,gender,locale,name,timezone";

    @Inject
    public FacebookService(WSClient ws, FacebookDataRepository facebookDataRepository) {

        this.ws = ws;
        this.facebookDataRepository = facebookDataRepository;

    }

    public CompletionStage<WSResponse> inspectionRequest(String facebookToken, String appId, String appToken, String appName) {

        WSRequest inspectionRequest = ws.url("https://graph.facebook.com/debug_token")
                .setQueryParameter("input_token", facebookToken)
                .setQueryParameter("access_token", appToken);

        return inspectionRequest.get().thenCompose(inspectionData -> {

            if (inspectionData.asJson().findValue("error") != null) {
                return CompletableFuture.completedFuture(inspectionData);
            }

            JsonNode inspectionJson = inspectionData.asJson();

            String retrievedAppId = inspectionJson.findValue("app_id").asText();
            String retrievedAppName = inspectionJson.findValue("application").asText();
            boolean valid = inspectionJson.findValue("is_valid").asBoolean();

            if (!retrievedAppId.equals(appId) || !retrievedAppName.equals(appName) || !valid) {
                return CompletableFuture.completedFuture(inspectionData);
            }

            return dataRequest(facebookToken, FIELDS);

        });
    }

    private CompletionStage<WSResponse> dataRequest(String facebookToken, String fields) {

        WSRequest dataRequest = ws.url("https://graph.facebook.com/me")
                .setQueryParameter("access_token", facebookToken)
                .setQueryParameter("fields", fields);

        return dataRequest.get().thenCompose(userData -> {

            String id = userData.asJson().findValue("id").asText();

            WSRequest imageRequest = ws.url("https://graph.facebook.com/" + id + "/picture");

            return imageRequest.get().thenApply(imageData -> {

                byte[] image = imageData.asByteArray();

                try {

                    buildFaceBookData(userData.asJson(), image);

                } catch (IOException e) {
                    return null;
                }

                return userData;

            });

        });
    }

    private void buildFaceBookData(JsonNode data, byte[] image) throws JsonProcessingException, IOException {

        String id = data.findValue("id").textValue();

        FacebookData fbData = facebookDataRepository.findByFacebookUserId(id);

        if (fbData == null) {
            fbData = facebookDataRepository.create(data);
        } else {
            fbData = facebookDataRepository.update(fbData, data);
        }

        fbData.image = image;
        facebookDataRepository.save(fbData);

    }
}
