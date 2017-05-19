package models.accommodation;

import com.avaje.ebean.Model;
import play.Logger;

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
    
    public Stations closestStation;

    public double longitude;
    public double latitude;

    private static Finder<Long, Address> find = new Finder<>(Address.class);
    
    //===============================================================CONSTRUCTORS

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
	
    //================================================================FINDERS
    
    public static Address findById(long id) {
        return find.byId(id);
    }
	
	public static Address findByCoords(double latitude, double longitude){
    	return find.where().eq("latitude",latitude).and().eq("longitude",longitude).findUnique();
	}
	
	public static Address findByStreet(String streetName,int streetNumber, char streetNumberLetter){
		Logger.debug("findByStreet char: "+streetName+", "+streetNumber+", "+streetNumberLetter);
		return find.where().eq("streetName",streetName).and().eq("streetNumber",streetNumber).and().eq("streetNumberLetter",streetNumberLetter).findUnique();
	}
	public static Address findByStreet(String streetName,int streetNumber){
		Logger.debug("findByStreet no char: "+streetName+", "+streetNumber);
		return find.where().eq("streetName",streetName).and().eq("streetNumber",streetNumber).findUnique();
	}
}
