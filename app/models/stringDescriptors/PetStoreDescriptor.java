package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance
@DiscriminatorValue("PSTORE")
public class PetStoreDescriptor extends StringDescriptor  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String petStoreName;

    public PetStoreDescriptor(JsonNode node){

        super(node, new String[] {"Ett litet härligt djur för att liva upp lägenheten hittar du på %s som ligger nära denna lya.",
            "Är du en katt eller hundmänniska? Splear ingen roll, båda finns på %s",
            "Kaske en papegoja vore ett härligt komplement till din nya ptentiella lägenhet, du hittar den på %s."});

         petStoreName = node.findValue("name").asText();

        //petStoreDescription = String.format(chooseRandomDescriptionString(), petStoreName);

    }

    @Override
    public String generateDescription() {

        description = String.format(chooseRandomDescriptionString(), petStoreName);
        return description;
    }
}
