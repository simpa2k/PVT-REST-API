package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance // Ebeans does not support any other strategy than SINGLE_TABLE. This works fine, but remember that no fields in subclasses can be non-nullable.
@DiscriminatorValue("ATM")
public class AtmDescriptor extends StringDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public AtmDescriptor(JsonNode node){

        super(node, new String[] {"Det finns en bankomat i närheten av bostaden."});

        //atmDescription = String.format(chooseRandomDescriptionString(), atmName);

    }




    public String toString(){
        return description;
    }

    @Override
    public String generateDescription() {

        description = String.format(possibleDescriptions[0]);

        return description;
    }
}
