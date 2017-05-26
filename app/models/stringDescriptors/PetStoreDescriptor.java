package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class PetStoreDescriptor implements StringDescriptor  {

    public String petStoreDescription;
    public String[] descriptionArray = {"Ett litet härligt djur för att liva upp lägenheten hittar du på %s som ligger nära denna lya.",
            "Är du en katt eller hundmänniska? Splear ingen roll, båda finns på %s",
            "Kaske en papegoja vore ett härligt komplement till din nya ptentiella lägenhet, du hittar den på %s."};

    public PetStoreDescriptor(JsonNode node){

        String petStoreName = node.findValue("name").asText();

        petStoreDescription = String.format(chooseRandomDescriptionString(), petStoreName);

    }

    public void getHäst(String string) {


    }
    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return descriptionArray[random.nextInt(3)];
    }

    @Override
    public String getDescription() {
        return petStoreDescription;
    }
}
