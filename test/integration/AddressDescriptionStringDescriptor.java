package integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.accommodation.AddressDescription;
import models.stringDescriptors.RestaurantDescriptor;
import org.junit.Test;
import play.Logger;
import play.libs.Json;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;

/**
 * @author Simon Olofsson
 */
public class AddressDescriptionStringDescriptor extends WithApplication {

    @Test
    public void handlesNullDescriptionsProperly() {

        AddressDescription addressDescription = new AddressDescription();

        ArrayNode restaurants = Json.newArray();

        for (int i = 0; i < 3; i++) {

            ObjectNode restaurant = Json.newObject();
            restaurant.put("name", "This is a restaurant!");

            restaurants.add(restaurant);

        }

        RestaurantDescriptor restaurantDescriptor = new RestaurantDescriptor(restaurants);
        addressDescription.addStringDescriptor(restaurantDescriptor);

        ObjectNode addressDescriptionJson  = Json.mapper().valueToTree(addressDescription);

        assertEquals(0, addressDescriptionJson.findValue("stringDescriptors").size());

    }
}
