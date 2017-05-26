package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance
@DiscriminatorValue("LOCKSMITH")
public class LocksmithDescriptor extends StringDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public LocksmithDescriptor(JsonNode node){

        super(node, new String[] {"Tappar du dina nycklar ofta är det inga problem. %s finns i närheten av denna förtjusande bostad",
            "Med %s i närheten behöver du aldrig oroa dig för att förbli utelåst. ",
            "Behöver du göra fler kopior på nycklar till denna bostad om du flyttar in? I sådant fall finns %s alldeles i närheten"});

        String locksmithName = node.findValue("name").asText();

        //locksmithDescription = String.format(chooseRandomDescriptionString(), locksmithName);

    }

    @Override
    public String generateDescription() {
        return description;
    }
}
