package repositories;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import models.Interest;
import models.accommodation.Accommodation;
import models.user.User;
import scala.Option;
import utils.ResponseBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * @author Simon Olofsson
 */
public class InterestsRepository {

    public Interest create(User renter, User tenant) {

        Interest interest = Interest.findByRenterAndTenant(renter.id, tenant.id);

        if (interest == null) {

            interest = new Interest(renter, tenant);
            save(interest);

        }
        return interest;

    }

    public List<Interest> findInterests(Option<Long> tenantId, Option<Long> renterId, Option<Boolean> mutual) {

        List<Function<ExpressionList<Interest>, ExpressionList<Interest>>> functions = Arrays.asList(

                exprList -> tenantId.isDefined() ? exprList.eq("tenant_id", tenantId.get()) : exprList,
                exprList -> renterId.isDefined() ? exprList.eq("renter_id", renterId.get()) : exprList,
                exprList -> mutual.isDefined() ? exprList.eq("mutual", mutual.get()) : exprList

        );

        return Interest.filterBy(functions);
    }

    public Interest findInterest(long renterId, long tenantId) {
        return Interest.findByRenterAndTenant(renterId, tenantId);
    }

    public void save(Interest interest) {
        interest.save();
    }

    public void delete(Interest interest) {

        if (interest == null) {
            throw new IllegalArgumentException("Interest to be deleted may not be null.");
        }

        interest.delete();

    }
}
