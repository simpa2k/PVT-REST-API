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

        super(node, new String[] {"Närhet till %s, ett perfekt sätt att spendera en torsdagskväll!",
                "Gå till %s precis i krokarna och dubbla din lön!", "Sätt allt på rött på %s, som ligger precis i krokarna."});

        casinoName = node.get(0).findValue("name").asText();

        //casinoDescription = String.format(chooseRandomDescriptionString(), casinoName);

    }


    @Override
    public String generateDescription() {

        if(!casinoName.toLowerCase().contains("casino")){
            casinoName = "Casino " + casinoName;
        }

        description = String.format(chooseRandomDescriptionString(), casinoName);


        return  description;
    }
}
