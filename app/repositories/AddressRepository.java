package repositories;

import models.accommodation.Address;

/**
 * @author Simon Olofsson
 */
public class AddressRepository {

    public void save(Address address) {
        address.save();
    }
}
