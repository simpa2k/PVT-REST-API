package testResources;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.accommodation.Accommodation;
import models.accommodation.Address;
import models.user.Renter;
import models.user.User;
import org.junit.Before;
import org.junit.BeforeClass;
import play.libs.Json;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * @author Simon Olofsson
 */
public class AccommodationUtils {

    public static ObjectNode createAccommodationJson() {

        ObjectNode accommodationJson = Json.newObject();

        accommodationJson.put("rent", 5000);
        accommodationJson.put("size", 20);
        accommodationJson.put("rooms", 1);
        accommodationJson.put("deposit", 8000);

        ObjectNode address = accommodationJson.putObject("address");
        address.put("streetName", "Dymlingsgränd");
        address.put("streetNumber", 3);
        address.put("streetNumberLetter", "A");

        return accommodationJson;

    }

    public static Accommodation createAccommodation() {

        User renter = new User("renter@renter.com", "Renter");

        Address address = new Address("Dymlingsgränd", 3, 'A');
        return new Accommodation(5000, 20, 1, 8000, address, renter);
    }

    public static void performStandardAssertions(Accommodation accommodation) {

        assertNotNull(accommodation);

        assertEquals(5000, accommodation.rent);
        assertEquals(20.0, accommodation.size);
        assertEquals(1.0, accommodation.rooms);
        assertEquals(8000, accommodation.deposit);

        assertEquals(accommodation.address.streetName, "Dymlingsgränd");
        assertEquals(accommodation.address.streetNumber, 3);
        assertEquals(accommodation.address.streetNumberLetter, 'A');

    }
}
