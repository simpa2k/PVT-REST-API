package repositories;

import models.accommodation.AddressDescription;

/**
 * @author Simon Olofsson
 */
public class AddressDescriptionRepository {

    public void save(AddressDescription addressDescription) {
        addressDescription.save();
    }
}
