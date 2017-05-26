package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class AtmDescriptor implements StringDescriptor {

    public String atmDescription;
    public String[] descriptionArray = {"Det finns en bankomat i närheten av bostaden."
            , };

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

    @Override
    public String getDescription() {

        return atmDescription;
    }
}
