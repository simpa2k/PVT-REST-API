package repositories;

import com.avaje.ebean.Finder;
import models.accommodation.Address;

/**
 * @author Simon Olofsson
 */
public class AddressRepository {
	Finder finder=new Finder(Address.class);

    public void save(Address address) {
        address.save();
    }
	
	public Address findByCoordinates(double latitude, double longitude){
		return Address.findByCoords(latitude, longitude);
	}
	public Address findByStreet(String streetName,int streetNumber,char streetNumberLetter){
		return Address.findByStreet(streetName, streetNumber, streetNumberLetter);
	}
}
