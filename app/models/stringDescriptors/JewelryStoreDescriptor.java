package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance
@DiscriminatorValue("JSTORE")
public class JewelryStoreDescriptor extends StringDescriptor  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String jewelryStoreName;

    public JewelryStoreDescriptor(JsonNode node){

        super(node, new String[] {"Gillar du smycken och blingbling? I så fall passar denna lägga perfekt för dig. %s finns i området.",
            "Lägenheten ligger nära %s om du ska på fest och vill se extra piffig ut.",
            "Vill du fria till din partner? På %s precis i närheten kan du köpa dig en ögonsten för ändamålet."});

         jewelryStoreName = node.findValue("name").asText();

        //jewelryStoreDescription = String.format(chooseRandomDescriptionString(), jewelryStoreName);

    }

    @Override
    public String generateDescription() {
        description = String.format(chooseRandomDescriptionString(), jewelryStoreName);
        return description;
    }
}
