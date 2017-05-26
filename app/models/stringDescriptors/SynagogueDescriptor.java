package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance
@DiscriminatorValue("SYNAGOGUE")
public class SynagogueDescriptor extends StringDescriptor  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String synagogueName;

    public SynagogueDescriptor(JsonNode node){

        super(node, new String[] {"Trött på Buddha? %s finns i närheten av denna bostad",
            "Vill du täcka din flint? Bär en kippah vid %s", "Ett stenkast från din lägenhet ligger %s"});

         synagogueName = node.findValue("name").asText();

        //synagogueDescription = String.format(chooseRandomDescriptionString(), synagogueName);

    }

    @Override
    public String generateDescription() {

        description = String.format(chooseRandomDescriptionString(), synagogueName);
        return description;
    }
}
