package repositories.address;

import models.accommodation.Address;

/**
 * @author Simon Olofsson
 */
public class AddressRepository implements AddressStorage {

    @Override
    public void save(Address address) {
        address.save();
    }
}
