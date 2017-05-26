package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
public class ChurchDescriptor extends StringDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public ChurchDescriptor(JsonNode node){

        super(node , new String[] {"%s ligger i närheten av denna bostad",
            "I detta område ligger den ståtliga  %s", "Nära till %s om man blir sugen på en liten gudstjänst"});

        String churchName = node.findValue("name").asText();

        //churchDescription = String.format(chooseRandomDescriptionString(), churchName);

    }

    @Override
    public String generateDescription() {
        return description;
    }
}
