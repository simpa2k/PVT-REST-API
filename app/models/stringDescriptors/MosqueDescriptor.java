package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance
@DiscriminatorValue("MOSQUE")
public class MosqueDescriptor extends StringDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String mosqueName;

    public MosqueDescriptor(JsonNode node){

        super(node, new String[] {"Väldigt nära din lägenhet ligger %s, ett ypperligt tillfälle att kontemplera",
            "Ett stenkast från denna lya finner du %s", "Alldelles intill din nya lägenhet finner du %s"});
        if(node.findValue("name").asText()!= null){

             mosqueName = node.findValue("name").asText();
            //mosqueDescription = String.format(chooseRandomDescriptionString(), mosqueName);
        }

    }

    @Override
    public String generateDescription() {

        description = String.format(chooseRandomDescriptionString(), mosqueName);
        return description;
    }
}
