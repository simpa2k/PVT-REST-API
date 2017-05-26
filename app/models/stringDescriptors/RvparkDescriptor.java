package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance
@DiscriminatorValue("RV_PARK_DESCRIPTOR")
public class RvparkDescriptor extends StringDescriptor  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public RvparkDescriptor(JsonNode node){

        super(node, new String[] {            "Det finns en %s inte så långt ifrån lägenheten",
            "Nu kan du elda ifred på %s alldeles i närheten av denna bostad"});

        String rvparkName = node.findValue("name").asText();

        //rvparkDescription = String.format(chooseRandomDescriptionString(), rvparkName);

    }

    @Override
    public String generateDescription() {
        return description;
    }
}
