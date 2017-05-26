package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance // Ebeans does not support any other strategy than SINGLE_TABLE. This works fine, but remember that no fields in subclasses can be non-nullable.
@DiscriminatorValue("CEMETERY")
public class CemeteryDescriptor extends StringDescriptor  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String cemeteryName;

    public CemeteryDescriptor(JsonNode node){

        super(node, new String[] {"I närheten finns %s där du kan leka med pinnar och sörja",
            "Upplev lugnet vid %s", "På %s kan du gå promenader eller sörja dina nära och kära"});

         cemeteryName = node.findValue("name").asText();

        //cemeteryDescription = String.format(chooseRandomDescriptionString(), cemeteryName);

    }

    @Override
    public String generateDescription() {
        description = String.format(chooseRandomDescriptionString(), cemeteryName);
        return description;

    }
}
