package models.accommodation;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Simon Olofsson
 */
@Entity
public class Address extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String streetName;
    public int streetNumber;
    public char streetNumberLetter;

    public String area;

    public double longitude;
    public double latitude;

    public Address(String streetName, int streetNumber, String area, double longitude, double latitude) {

        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.area = area;
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
}
