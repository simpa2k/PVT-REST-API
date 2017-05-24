package models.StringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class CemeteryDescriptor implements StringDescriptor {

    public String cemeteryDescription;
    public String[] descriptionArray = {"I närheten finns %s där du kan leka med pinnar och sörja", "Upplev lugnet vid %s", "På x kan du gå promenader eller sörja dina nära och kära"};

    public CemeteryDescriptor(JsonNode node){

        String cemeteryName = node.findValue("name").asText();

        cemeteryDescription = String.format(chooseRandomDescriptionString(), cemeteryName);

    }

    public void getHäst(String string) {


    }
    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return descriptionArray[random.nextInt(3)];
    }


}
