package models.StringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class FirestationDescriptor  {

    public String firestationDescription;
    public String[] descriptionArray = {"Här kan du bo tryggt och säkert med %s i närheten av denna bostad", "I detta område kryllar det av heta brandmän då %s ligger ett stenkast från denna bostad", "Fritt från pyromaner och eld när %s finns runt hörnet." };
    public FirestationDescriptor(JsonNode node){

        String firestationName = node.findValue("name").asText();

        firestationDescription = String.format(chooseRandomDescriptionString(), firestationName);

    }

    public void getHäst(String string) {

    }
    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return descriptionArray[random.nextInt(3)];
    }
}
