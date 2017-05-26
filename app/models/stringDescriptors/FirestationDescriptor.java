package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance
@DiscriminatorValue("FIRESTATION")
public class FirestationDescriptor extends StringDescriptor  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public FirestationDescriptor(JsonNode node){

        super(node, new String[] {"Här kan du bo tryggt och säkert med %s i närheten av denna bostad",
            "I detta område kryllar det av heta brandmän då %s ligger ett stenkast från denna bostad",
            "Fritt från pyromaner och eld när %s finns runt hörnet." });

        String firestationName = node.findValue("name").asText();

        //firestationDescription = String.format(chooseRandomDescriptionString(), firestationName);

    }

    public void getHäst(String string) {

    }
    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return possibleDescriptions[random.nextInt(3)];
    }

    @Override
    public String generateDescription() {
        return description;
    }
}
