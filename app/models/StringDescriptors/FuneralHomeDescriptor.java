package models.StringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class FuneralHomeDescriptor {

    public String mosqueDescription;
    public String[] descriptionArray = {"%s finns om någon i din närhet går bort", "Det finns ett %s i närheten av denna bostad", "I %s kan du vila ut i lugn och ro"};

    public FuneralHomeDescriptor(JsonNode node){

        String funeralHomeName = node.findValue("name").asText();

        mosqueDescription = String.format(chooseRandomDescriptionString(), funeralHomeName);

        }

    public void getHäst(String string) {


    }
    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return descriptionArray[random.nextInt(3)];
    }

}
