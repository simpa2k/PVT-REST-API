package services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Interest;
import models.user.User;
import repositories.InterestsRepository;
import repositories.UsersRepository;
import scala.Option;
import exceptions.OffsetOutOfRangeException;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Simon Olofsson
 */
public class InterestsService {

    private final InterestsRepository interestsRepository;
    private final UsersRepository usersRepository;

    @Inject
    public InterestsService(InterestsRepository interestsRepository, UsersRepository usersRepository) {

        this.interestsRepository = interestsRepository;
        this.usersRepository = usersRepository;

    }

    public void addInterests(User renter, ArrayNode tenantIds) {

        tenantIds.forEach(tenantId -> {

            User tenant = usersRepository.findById(tenantId.asLong());

            if (tenant != null) {

                Interest interest = interestsRepository.create(renter, tenant);
                renter.addInterest(interest);

            }
        });
    }

    public Interest setMutual(long renterId, long tenantId, boolean mutual) {

        Interest interest = interestsRepository.findInterest(renterId, tenantId);
        interest.mutual = mutual;

        interestsRepository.save(interest);

        return interest;

    }

    public void withdrawInterest(long renterId, long tenantId) {

        Interest interest = interestsRepository.findInterest(renterId, tenantId);

        if (interest != null) {
            interestsRepository.delete(interest);
        }
    }

    public List<Interest> getSubset(Option<Integer> count, Option<Integer> offset,
                                    Option<Long> tenantId, Option<Long> renterId, Option<Boolean> mutual) throws OffsetOutOfRangeException {

        if (!mutual.isDefined()) { // Setting a default value to true.
            mutual = Option.apply(true);
        }

        List<Interest> interests = interestsRepository.findInterests(tenantId, renterId, mutual);

        int evaluatedOffset = offset.isDefined() ? offset.get() : 0;
        int evaluatedCount = count.isDefined() && ((count.get() + evaluatedOffset) < interests.size()) ? count.get() : interests.size();

        if (evaluatedOffset > interests.size()) {
            throw new OffsetOutOfRangeException("The offset you have requested is larger than the number of results.");
        }

        return interests.subList(evaluatedOffset, evaluatedCount);
    }

    public boolean deleteInterest(long tenantId, long accommodationId) {

        Interest interest = interestsRepository.findInterest(tenantId, accommodationId);

        if (interest == null) {
            return false;
        }

        interestsRepository.delete(interest);

        return true;
    }
}
