package repositories;

import com.avaje.ebean.ExpressionList;
import models.accommodation.Accommodation;
import scala.Option;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author Simon Olofsson
 */
public class AccommodationRepository {

    public Accommodation findById(long id) {
        return Accommodation.findById(id);
    }

    public Accommodation findByRenter(long renterId) {
        return Accommodation.findByRenterId(renterId);
    }

    public List<Accommodation> findAccommodation(final Option<Double> rent, final Option<Double> size,
                                                 final Option<Boolean> smokingAllowed, final Option<Boolean> animalsAllowed) {

        List<Function<ExpressionList<Accommodation>, ExpressionList<Accommodation>>> functions = Arrays.asList(

                exprList -> rent.isDefined() ? exprList.le("rent", rent.get()) : exprList,
                exprList -> size.isDefined() ? exprList.ge("size", size.get()) : exprList,
                exprList -> smokingAllowed.isDefined() ? exprList.eq("smoking_allowed", smokingAllowed.get()) : exprList,
                exprList -> animalsAllowed.isDefined() ? exprList.eq("animals_allowed", animalsAllowed.get()) : exprList

        );

        return Accommodation.filterBy(functions);

    }

    public void update(Accommodation accommodation) {
        accommodation.update();
    }

    public void save(Accommodation accommodation) {
        accommodation.save();
    }

    public void delete(Accommodation accommodation) {
        accommodation.delete();
    }
}
