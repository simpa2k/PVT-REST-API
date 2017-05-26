package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class CasinoDescriptor implements StringDescriptor {

    public String casinoDescription;
    public String[] descriptionArray = {"Närhet till %s, kul ju!", "Gå till %s och dubbla din lön!", "Sätt allt på rött vid %s!"};
    public CasinoDescriptor(JsonNode node){

        String casinoName = node.get(0).findValue("name").asText();

        casinoDescription = String.format(chooseRandomDescriptionString(), casinoName);

    }


    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return descriptionArray[random.nextInt(3)];
    }

    @Override
    public String getDescription() {
        return  casinoDescription;




    }
}
