package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance // Ebeans does not support any other strategy than SINGLE_TABLE. This works fine, but remember that no fields in subclasses can be non-nullable.
@DiscriminatorValue("BANK")
public class BankDescriptor extends StringDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public BankDescriptor(JsonNode node){

        super(node, new String[] {"Har du ont om pengar? %s ligger ett stenkast från denna bostad.",
            "Är du trött på att lägga pengar på hög? Ingen fara. %s ligger precis runt hörnet",
            "Ett normalt stenkast bort ligger %s."});

        String bankName = node.findValue("name").asText();

        //bankDescription = String.format(chooseRandomDescriptionString(), bankName);

    }

    public void getHäst(String string) {


    }
    public String chooseRandomDescriptionString(){
        Random random = new Random();
        return possibleDescriptions[random.nextInt(3)];
    }

    public String toString(){
        return description;
    }

    @Override
    public String generateDescription() {
        return description;
    }
}
