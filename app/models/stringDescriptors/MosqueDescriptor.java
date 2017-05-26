package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class MosqueDescriptor implements StringDescriptor {


    public String mosqueDescription;
    public String[] descriptionArray = {"Väldigt nära din lägenhet ligger %s, ett ypperligt tillfälle att kontemplera",
            "Ett stenkast från denna lya finner du %s", "Alldelles intill din nya lägenhet finner du %s"};

    public MosqueDescriptor(JsonNode node){
        if(node.findValue("name").asText()!= null){

            String mosqueName = node.findValue("name").asText();
            mosqueDescription = String.format(chooseRandomDescriptionString(), mosqueName);
        }

    }

    public void getHäst(String string) {


    }
    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return descriptionArray[random.nextInt(3)];
    }

    public String toString(){
        return mosqueDescription;
    }

    @Override
    public String getDescription() {
        return mosqueDescription;
    }
}
