package repositories;

import models.RentalPeriod;

/**
 * @author Simon Olofsson
 */
public class RentalPeriodRepository {

    public void save(RentalPeriod rentalPeriod) {
        rentalPeriod.save();
    }
}
