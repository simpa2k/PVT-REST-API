package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance
@DiscriminatorValue("FHOME")
public class FuneralHomeDescriptor extends StringDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String funeralHomeName;

    public FuneralHomeDescriptor(JsonNode node){

        super(node, new String[] {"%s finns om någon i din närhet går bort.",
            "Det finns ett %s i närheten av denna bostad", "Med %s i närheten så kan du planera närståendes bortgång."});

         funeralHomeName = node.findValue("name").asText();

        //funeralHomeDescription = String.format(chooseRandomDescriptionString(), funeralHomeName);

        }

    @Override
    public String generateDescription() {

        description = String.format(chooseRandomDescriptionString(), funeralHomeName);
        return description;
    }
}
