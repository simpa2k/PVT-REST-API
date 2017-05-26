package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance
@DiscriminatorValue("HINDU_TEMPLE")
public class HinduTempleDescriptor extends StringDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public HinduTempleDescriptor(JsonNode node){

        super(node, new String[] {"Ett stenkast från %s templet hittar du denna panglya",
            "Du är konstant en kort promenad från %s. en plats för kontemplation och lugn",
            "Ta en titt förbi ditt lokala hindutempel. Nära dig ligger nämligen %s"});

        String hinduTempleName = node.findValue("name").asText();

        //hinduTempleDescription = String.format(chooseRandomDescriptionString(), hinduTempleName);

    }

    @Override
    public String generateDescription() {
        return description;
    }
}
