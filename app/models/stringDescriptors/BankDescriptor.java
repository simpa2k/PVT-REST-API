package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class BankDescriptor implements StringDescriptor {

    public String bankDescription;
    public String[] descriptionArray = {"Har du ont om pengar? %s ligger ett stenkast från denna bostad.",
            "Är du trött på att lägga pengar på hög? Ingen fara. %s ligger precis runt hörnet",
            "Ett normalt stenkast bort ligger %s."};

    public BankDescriptor(JsonNode node){

        String bankName = node.findValue("name").asText();

        bankDescription = String.format(chooseRandomDescriptionString(), bankName);

    }

    public void getHäst(String string) {


    }
    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return descriptionArray[random.nextInt(3)];
    }

    public String toString(){
        return bankDescription;
    }

    @Override
    public String getDescription() {
        return bankDescription;
    }
}
