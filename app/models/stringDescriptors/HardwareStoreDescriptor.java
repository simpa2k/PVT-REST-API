package models.stringDescriptors;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Random;

/**
 * Created by Henke on 2017-05-23.
 */
@Entity
@Inheritance
@DiscriminatorValue("HSTORE")
public class HardwareStoreDescriptor extends StringDescriptor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String hardwareStoreName;

    public HardwareStoreDescriptor(JsonNode node){

        super(node, new String[] {"Inget kras spelar någon roll för du bor nära %s.",
            "Gillar du att snickra och sådant? Då passar detta boende bra för dig. Nära till %s.",
            "Behöver du en bågfil? %s ligger precis runt hörnet."});

         hardwareStoreName = node.get(0).findValue("name").asText();

        //hardwareStoreDescription = String.format(chooseRandomDescriptionString(), hardwareStoreName);

    }

    @Override
    public String generateDescription() {
        description = String.format(chooseRandomDescriptionString(), hardwareStoreName);
        return description;
    }
}
