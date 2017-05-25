package testResources;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.RentalPeriod;
import models.accommodation.Accommodation;
import models.accommodation.Address;
import models.user.Renter;
import models.user.User;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import play.Logger;
import play.libs.Json;
import repositories.AccommodationRepository;
import repositories.AddressRepository;
import repositories.RentalPeriodRepository;
import repositories.UsersRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * @author Simon Olofsson
 */
public class AccommodationUtils {

    private static String getBase64Image() {

        File file = new File("./test/testResources/testImage.jpg");

        // From: https://stackoverflow.com/questions/36492084/how-to-convert-an-image-to-base64-string-in-java

        String encodedfile = null;
        try {

            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return encodedfile;

    }

    public static ObjectNode createAccommodationJson() {

        ObjectNode accommodationJson = Json.newObject();

        accommodationJson.put("image", getBase64Image());
        accommodationJson.put("rent", 5000);
        accommodationJson.put("size", 20);
        accommodationJson.put("rooms", 1);
        accommodationJson.put("deposit", 8000);

        ObjectNode address = accommodationJson.putObject("address");
        address.put("streetName", "Dymlingsgränd");
        address.put("streetNumber", 3);
        address.put("streetNumberLetter", "A");

        ObjectNode rentalPeriod = accommodationJson.putObject("rentalPeriod");
        rentalPeriod.put("start", "2017-05-01");
        rentalPeriod.put("end", "2018-05-01");

        return accommodationJson;

    }

    public static Accommodation createAccommodation() throws ParseException {

        User renter = new User("renter@renter.com", "Renter");

        Address address = new Address("Dymlingsgränd", 3, 'A');
        RentalPeriod rentalPeriod = new RentalPeriod("2017-05-01", "2018-05-01");

        Accommodation accommodation = new Accommodation(5000, 20, 1, 8000, address, renter);
        accommodation.rentalPeriod = rentalPeriod;

        return accommodation;

    }

    public static AccommodationData createAccommodationData() throws ParseException {

        User renter = new User("renter@renter.com", "Renter");

        Address address = new Address("Dymlingsgränd", 3, 'A');
        RentalPeriod rentalPeriod = new RentalPeriod("2017-05-01", "2018-05-01");

        Accommodation accommodation = new Accommodation(5000, 20, 1, 8000, address, renter);
        accommodation.rentalPeriod = rentalPeriod;

        return new AccommodationData(renter, address, rentalPeriod, accommodation);

    }

    public static void performStandardAssertions(Accommodation accommodation) throws ParseException {

        assertNotNull(accommodation);

        assertEquals(5000, accommodation.rent);
        assertEquals(20.0, accommodation.size);
        assertEquals(1.0, accommodation.rooms);
        assertEquals(8000, accommodation.deposit);

        assertEquals("Dymlingsgränd", accommodation.address.streetName);
        assertEquals(3, accommodation.address.streetNumber);
        assertEquals('A', accommodation.address.streetNumberLetter);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Date start = DateUtils.truncate(df.parse("2017-05-01"), Calendar.DAY_OF_MONTH);
        Date end = DateUtils.truncate(df.parse("2018-05-01"), Calendar.DAY_OF_MONTH);

        Logger.debug("" + accommodation.rent);

        assertEquals(start, DateUtils.truncate(accommodation.rentalPeriod.start, Calendar.DAY_OF_MONTH));
        assertEquals(end, DateUtils.truncate(accommodation.rentalPeriod.end, Calendar.DAY_OF_MONTH));

    }

    public static class AccommodationData {

        private User renter;
        private Address address;
        private RentalPeriod rentalPeriod;
        private Accommodation accommodation;

        public AccommodationData(User renter, Address address, RentalPeriod rentalPeriod, Accommodation accommodation) {

            this.renter = renter;
            this.address = address;
            this.rentalPeriod = rentalPeriod;
            this.accommodation = accommodation;

        }

        public User getRenter() {
            return renter;
        }

        public Address getAddress() {
            return address;
        }

        public RentalPeriod getRentalPeriod() {
            return rentalPeriod;
        }

        public Accommodation getAccommodation() {
            return accommodation;
        }

        public void saveAll(UsersRepository usersRepository,
                            AddressRepository addressRepository,
                            RentalPeriodRepository rentalPeriodRepository,
                            AccommodationRepository accommodationRepository) {

            usersRepository.save(renter);
            addressRepository.save(address);
            rentalPeriodRepository.save(rentalPeriod);
            accommodationRepository.save(accommodation);

        }
    }
}
