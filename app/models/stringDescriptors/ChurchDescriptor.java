package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class ChurchDescriptor {

    public String churchDescription;
    public String[] descriptionArray = {"%s ligger i närheten av denna bostad", "I detta område ligger den ståtliga  %s", "Nära till %s om man blir sugen på en liten gudstjänst"};

    public ChurchDescriptor(JsonNode node){

        String churchName = node.findValue("name").asText();

        churchDescription = String.format(chooseRandomDescriptionString(), churchName);

    }

    public void getHäst(String string) {


    }
    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return descriptionArray[random.nextInt(3)];
    }

}
