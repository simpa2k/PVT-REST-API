package repositories.RentalPeriodRepository;

import models.RentalPeriod;

/**
 * @author Simon Olofsson
 */
public class RentalPeriodRepository implements RentalPeriodStorage {


    @Override
    public void save(RentalPeriod rentalPeriod) {
        rentalPeriod.save();
    }
}
