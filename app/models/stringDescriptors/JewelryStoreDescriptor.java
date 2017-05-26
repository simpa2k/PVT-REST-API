package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance
@DiscriminatorValue("JEWELRY_STORE")
public class JewelryStoreDescriptor extends StringDescriptor  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String jewelryStoreName;

    public JewelryStoreDescriptor(JsonNode node){

        super(node, new String[] {"Gillar du smycken och blingbling? I så fall passar denna lägga perfekt för dig. %s finns i området.",
            "Gå förbi %s påväg till festen för att se extra piffig ut.",
            "Vill du fria till Stellan Skarsgård? På %s kan du köpa dig en ögonsten som passar till Stellans finger."});

         jewelryStoreName = node.findValue("name").asText();

        //jewelryStoreDescription = String.format(chooseRandomDescriptionString(), jewelryStoreName);

    }

    @Override
    public String generateDescription() {
        description = String.format(chooseRandomDescriptionString(), jewelryStoreName);
        return description;
    }
}
