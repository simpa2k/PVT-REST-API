package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance
@DiscriminatorValue("FUNERAL_HOME")
public class FuneralHomeDescriptor extends StringDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public FuneralHomeDescriptor(JsonNode node){

        super(node, new String[] {"%s finns om någon i din närhet går bort",
            "Det finns ett %s i närheten av denna bostad", "I %s kan du vila ut i lugn och ro"});

        String funeralHomeName = node.findValue("name").asText();

        //funeralHomeDescription = String.format(chooseRandomDescriptionString(), funeralHomeName);

        }

    @Override
    public String generateDescription() {
        return description;
    }
}
