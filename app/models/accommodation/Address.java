package models.accommodation;

import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * @author Simon Olofsson
 */
@Entity
public class Address extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String streetName;
    public int streetNumber=-1;
    public char streetNumberLetter;

    public String area;
    @OneToOne
    public AddressDescription addressDescription;

    public double longitude;
    public double latitude;

    private static Finder<Long, Address> find = new Finder<>(Address.class);

    public Address(String streetName, int streetNumber, String area, double longitude, double latitude) {

        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.area = area;
        this.longitude = longitude;
        this.latitude = latitude;

    }


    public Address(String streetName, int streetNumber, char streetNumberLetter, String area) {

        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.streetNumberLetter = streetNumberLetter;
        this.area = area;
    }

    public Address(String streetName, int streetNumber, String area) {

        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.area = area;
        this.addressDescription = new AddressDescription();



    }

    public Address (String streetName, double latitude, double longitude){
        this.streetName = streetName;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Address(String streetName, int streetNumber, char streetNumberLetter) {

        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.streetNumberLetter = streetNumberLetter;
        this.area = area;
        this.longitude = longitude;
        this.latitude = latitude;

    }



    public static Address findById(long id) {
        return find.byId(id);
    }
}
