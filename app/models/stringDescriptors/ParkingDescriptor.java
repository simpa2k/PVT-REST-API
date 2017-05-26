package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance
@DiscriminatorValue("PARKING")
public class ParkingDescriptor extends StringDescriptor  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String parkingName;

    public ParkingDescriptor(JsonNode node){

        super(node, new String[] {"I närheten av din lägenhet finns parkeringsplatsen %s",
            "Sluta oroa dig för perkering, %s tillhandahåller lösningen",
            "Smidigt nog finns det tillgång till parkering i angräsning till bostaden vid %s."});

         parkingName = node.findValue("name").asText();

        //parkingDescription = String.format(chooseRandomDescriptionString(), parkingName);

    }

    @Override
    public String generateDescription() {

        description = String.format(chooseRandomDescriptionString(), parkingName);
        return description;
    }

}
