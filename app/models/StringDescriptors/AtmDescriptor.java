package models.StringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class AtmDescriptor {

    public String atmDescription;
    public String[] descriptionArray = {"%s finns om någon i din närhet går bort", "Det finns ett %s i närheten av denna bostad", "I %s kan du sova livet ut i lugn och ro"};

    public AtmDescriptor(JsonNode node){

        String atmName = node.findValue("name").asText();

        atmDescription = String.format(chooseRandomDescriptionString(), atmName);

    }

    public void getHäst(String string) {


    }
    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return descriptionArray[random.nextInt(3)];
    }

    public String toString(){
        return atmDescription;
    }
}
