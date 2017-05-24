package models.StringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class PainterDescriptor implements StringDescriptor {

    public String painterDescription;
    public String[] descriptionArray = {"Babylon brinner! Bygg upp det igen och köp färg från %s som ligger nära belägen till denna bostad", "Behöver du också måla om din träskföjt? %s finns i närheten.", "Det ligger en %s i området om du blir sugen på att måla." };

    public PainterDescriptor(JsonNode node){

        String painterName = node.findValue("name").asText();

        painterDescription = String.format(chooseRandomDescriptionString(), painterName);

    }

    public void getHäst(String string) {


    }
    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return descriptionArray[random.nextInt(3)];
    }
}
