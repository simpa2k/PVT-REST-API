package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class ParkingDescriptor  {

    public String parkingDescription;
    public String[] descriptionArray = {"I närheten av din lägenhet finns parkeringsplatsen %s", "Sluta oroa dig för perkering, %s tillhandahåller lösningen", "Smidigt nog finns det tillgång till parkering i angräsning till bostaden vid %s."};

    public ParkingDescriptor(JsonNode node){

        String parkingName = node.findValue("name").asText();

        parkingDescription = String.format(chooseRandomDescriptionString(), parkingName);

    }

    public void getHäst(String string) {

    }
    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return descriptionArray[random.nextInt(3)];
    }
}
