package models.StringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class LocksmithDescriptor implements StringDescriptor {

    public String locksmithDescription;
    public String[] descriptionArray = {"Tappar du dina nycklar ofta är det inga problem. %s finns i närheten av denna förtjusande bostad", "Med %s i närheten behöver du aldrig oroa dig för att förbli utelåst. ", "Behöver du göra fler kopior på nycklar till denna bostad om du flyttar in? I sådant fall finns %s alldeles i närheten"};

    public LocksmithDescriptor(JsonNode node){

        String locksmithName = node.findValue("name").asText();

        locksmithDescription = String.format(chooseRandomDescriptionString(), locksmithName);

    }

    public void getHäst(String string) {

    }
    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return descriptionArray[random.nextInt(3)];
    }
}
