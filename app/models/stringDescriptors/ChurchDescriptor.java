package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance // Ebeans does not support any other strategy than SINGLE_TABLE. This works fine, but remember that no fields in subclasses can be non-nullable.
@DiscriminatorValue("CHURCH")
public class ChurchDescriptor extends StringDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String churchName;

    public ChurchDescriptor(JsonNode node){

        super(node , new String[] {"%s ligger i närheten av denna bostad",
            "I detta område ligger den ståtliga  %s", "Nära till %s om man blir sugen på en liten gudstjänst"});

         churchName = node.findValue("name").asText();

        //churchDescription = String.format(chooseRandomDescriptionString(), churchName);

    }

    @Override
    public String generateDescription() {
        description = String.format(chooseRandomDescriptionString(), churchName);
        return description;
    }
}
