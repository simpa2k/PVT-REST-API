package models.StringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class CasinoDescriptor {

    public String casinoDescription;
    public String[] descriptionArray = {"Med %s runt hörnet kan du köpa din drömcykel och utforska området", "Har du fått punktering på din cykel ligger %s precis runt hörnet av din bostad", "Lär dig cykla utan händer! %s ligger precis runt hörnet."};
    public CasinoDescriptor(JsonNode node){

        String casinoName = node.findValue("name").asText();

        casinoDescription = String.format(chooseRandomDescriptionString(), casinoName);

    }

    public void getHäst(String string) {


    }
    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return descriptionArray[random.nextInt(3)];
    }
}
