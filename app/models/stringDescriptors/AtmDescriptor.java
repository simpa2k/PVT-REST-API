package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance // Ebeans does not support any other strategy than SINGLE_TABLE. This works fine, but remember that no fields in subclasses can be non-nullable.
@DiscriminatorValue("ATM_DESCRIPTOR")
public class AtmDescriptor extends StringDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public AtmDescriptor(JsonNode node){

        super(node, new String[] {"Det finns en bankomat i närheten av bostaden."});

        String atmName = node.findValue("name").asText();

        //atmDescription = String.format(chooseRandomDescriptionString(), atmName);

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
