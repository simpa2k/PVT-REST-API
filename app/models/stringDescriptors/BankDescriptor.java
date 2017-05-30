package models.stringDescriptors;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    public long id;

    public BankDescriptor(JsonNode node){

        super(node, new String[] {"Har du ont om pengar? %s ligger ett stenkast från denna bostad.",
            "Är du trött på att lägga pengar på hög? Ingen fara. %s ligger precis runt hörnet",
            "Ett stenkast bort ligger %s."});

        //bankDescription = String.format(chooseRandomDescriptionString(), bankName);

    }



    public String toString(){
        return description;
    }

    @Override
    public String generateDescription() {

        description = String.format(chooseRandomDescriptionString(), name);
        return description;
    }
}
