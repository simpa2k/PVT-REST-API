package models.StringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class RvparkDescriptor implements StringDescriptor {

    public String rvparkDescription;
    public String[] descriptionArray = {"Är du redo att skaffa dig en stor och härlig white-trash familj? Då är detta det perfekta boendet för dig. Med %s i närheten kan du och din blivande familj uppleva en fantastisk campingsemester", "Det finns en %s inte så långt ifrån lägenheten", "Nu kan du elda ifred på %s alldeles i närheten av denna bostad"};

    public RvparkDescriptor(JsonNode node){

        String rvparkName = node.findValue("name").asText();

        rvparkDescription = String.format(chooseRandomDescriptionString(), rvparkName);

    }

    public void getHäst(String string) {


    }
    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return descriptionArray[random.nextInt(3)];
    }
}
