package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class HinduTempleDescriptor implements StringDescriptor {

    public String hinduTempleDescription;
    public String[] descriptionArray = {"Ett stenkast från %s templet hittar du denna panglya",
            "Du är konstant en kort promenad från %s. en plats för kontemplation och lugn",
            "Ta en titt förbi ditt lokala hindutempel. Nära dig ligger nämligen %s"};

    public HinduTempleDescriptor(JsonNode node){

        String hinduTempleName = node.findValue("name").asText();

        hinduTempleDescription = String.format(chooseRandomDescriptionString(), hinduTempleName);

    }




    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return descriptionArray[random.nextInt(3)];
    }

    @Override
    public String getDescription() {
        return hinduTempleDescription;
    }
}
