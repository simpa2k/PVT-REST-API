package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class JewelryStoreDescriptor  {

    public String jewelryStoreDescription;
    public String[] descriptionArray = {"Gillar du smycken och blingbling? I så fall passar denna lägga perfekt för dig. %s finns i området.", "Gå förbi %s påväg till festen för att se extra piffig ut.", "Vill du fria till Stellan Skarsgård? På %s kan du köpa dig en ögonsten som passar till Stellans finger."};
    public JewelryStoreDescriptor(JsonNode node){

        String jewelryStoreName = node.findValue("name").asText();

        jewelryStoreDescription = String.format(chooseRandomDescriptionString(), jewelryStoreName);

    }

    public void getHäst(String string) {


    }
    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return descriptionArray[random.nextInt(3)];
    }
}
