package testResources.mocks;

import models.Interest;
import models.user.User;
import repositories.InterestsRepository;
import scala.Option;

import java.util.List;

/**
 * @author Simon Olofsson
 */
public class MockInterestRepository extends InterestsRepository {

    @Override
    public Interest create(User renter, User tenant) {
        return null;
    }

    @Override
    public List<Interest> findInterests(Option<Long> tenantId, Option<Long> accommodationId, Option<Boolean> mutual) {
        return null;
    }

    @Override
    public Interest findInterest(long tenantId, long accommodationId) {
        return null;
    }

    @Override
    public void save(Interest interest) {

    }

    @Override
    public void delete(Interest interest) {

    }
}
