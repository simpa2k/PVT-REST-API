package repositories.address;

import models.accommodation.Address;

/**
 * @author Simon Olofsson
 */
public interface AddressStorage {

    void save(Address address);

}
