package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance
@DiscriminatorValue("PAINTER_DESCRIPTOR")
public class PainterDescriptor extends StringDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public PainterDescriptor(JsonNode node){

        super(node, new String[] {"Babylon brinner! Bygg upp det igen och köp färg från %s som ligger nära belägen till denna bostad",
            "Behöver du också måla om din träskföjt? %s finns i närheten.",
            "Det ligger en %s i området om du blir sugen på att måla." });

        String painterName = node.findValue("name").asText();

        //painterDescription = String.format(chooseRandomDescriptionString(), painterName);

    }

    @Override
    public String generateDescription() {
        return description;
    }
}
