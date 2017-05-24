package models.StringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
public class HardwareStoreDescriptor {

    public String hardwareStoreDescription;
    public String[] descriptionArray = {"Ingenting spelar någon roll för du bor nära %s", "Gillar du att snickra och sådant? Då passar detta boende bra för dig. Nära till %s", "Mera bågfil? %s ligger precis runt hörnet."};
    public HardwareStoreDescriptor(JsonNode node){

        String hardwareStoreName = node.findValue("name").asText();

        hardwareStoreDescription = String.format(chooseRandomDescriptionString(), hardwareStoreName);

    }

    public void getHäst(String string) {


    }
    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return descriptionArray[random.nextInt(3)];
    }
}
