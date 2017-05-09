package services.interests;

import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Interest;
import models.user.User;
import repositories.interests.InterestStorage;
import repositories.users.UserStorage;
import scala.Option;
import exceptions.OffsetOutOfRangeException;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Simon Olofsson
 */
public class InterestsService {

    private final InterestStorage interestsRepository;
    private final UserStorage usersRepository;

    @Inject
    public InterestsService(InterestStorage interestsRepository, UserStorage usersRepository) {

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


    public List<Interest> getSubset(Option<Integer> count, Option<Integer> offset,
                                    Option<Long> tenantId, Option<Long> renterId) throws OffsetOutOfRangeException {

        List<Interest> interests = interestsRepository.findInterests(tenantId, renterId);

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
