package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance // Ebeans does not support any other strategy than SINGLE_TABLE. This works fine, but remember that no fields in subclasses can be non-nullable.
@DiscriminatorValue("CASINO")
public class CasinoDescriptor extends StringDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String casinoName;

    public CasinoDescriptor(JsonNode node){

        super(node, new String[] {"Närhet till %s, kul ju!", "Gå till %s och dubbla din lön!", "Sätt allt på rött vid %s!"});

        casinoName = node.get(0).findValue("name").asText();

        //casinoDescription = String.format(chooseRandomDescriptionString(), casinoName);

    }


    @Override
    public String generateDescription() {

        description = String.format(chooseRandomDescriptionString(), casinoName);
        return  description;
    }
}
