package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class SynagogueDescriptor implements StringDescriptor  {

    public String synagogueDescription;
    public String[] descriptionArray = {"Trött på Buddha? %s finns i närheten av denna bostad",
            "Vill du täcka din flint? Bär en kippah vid %s", "Ett stenkast från din lägenhet ligger %s"};

    public SynagogueDescriptor(JsonNode node){

        String synagogueName = node.findValue("name").asText();

        synagogueDescription = String.format(chooseRandomDescriptionString(), synagogueName);

    }

    public void getHäst(String string) {


    }
    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return descriptionArray[random.nextInt(3)];
    }

    @Override
    public String getDescription() {
        return synagogueDescription;
    }
}
