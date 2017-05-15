package integration;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.OffsetOutOfRangeException;
import models.Interest;
import models.user.User;
import org.junit.Test;
import play.Logger;
import play.libs.Json;
import repositories.InterestsRepository;
import repositories.UsersRepository;
import scala.Option;
import services.InterestsService;
import testResources.BaseTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Simon Olofsson
 */
public class InterestsStackTest extends BaseTest {

    InterestsRepository interestsRepository = new InterestsRepository();
    InterestsService interestsService = new InterestsService(interestsRepository, new UsersRepository());

    @Test
    public void getInterestsFromDatabase() throws OffsetOutOfRangeException {

        User renter = new User("renter@renter.com", "Renter");
        User tenant = new User("tenant@tenant.com", "Tenant");
        User tenant2 = new User("tenant2@tenant.com", "Tenant2");

        UsersRepository usersRepository = new UsersRepository();

        usersRepository.save(renter);
        usersRepository.save(tenant);
        usersRepository.save(tenant2);

        Interest interest = new Interest(renter, tenant);
        Interest interest2 = new Interest(renter, tenant2);

        interestsRepository.save(interest);
        interestsRepository.save(interest2);

        Option<Integer> count = Option.empty();
        Option<Integer> offset = Option.empty();
        Option<Long> tenantId = Option.apply(tenant.id);
        Option<Long> renterId = Option.empty();
        Option<Boolean> mutual = Option.empty();

        List<Interest> interests = interestsService.getSubset(count, offset, tenantId, renterId, mutual);

        assertEquals(1, interests.size());
        assertEquals(interest, interests.get(0));
        assertFalse(interests.contains(interest2));

    }

    @Test
    public void addInterests() throws OffsetOutOfRangeException {

        User renter = new User("renter@renter.com", "Renter");
        User tenant = new User("tenant@tenant.com", "Tenant");
        User tenant2 = new User("tenant2@tenant.com", "Tenant2");

        UsersRepository usersRepository = new UsersRepository();

        usersRepository.save(renter);
        usersRepository.save(tenant);
        usersRepository.save(tenant2);

        ArrayNode tenantIds = Json.newArray();
        tenantIds.add(tenant.id);
        tenantIds.add(tenant2.id);

        interestsService.addInterests(renter, tenantIds);

        Option<Integer> count = Option.empty();
        Option<Integer> offset = Option.empty();
        Option<Long> tenantId = Option.apply(tenant.id);
        Option<Long> renterId = Option.empty();
        Option<Boolean> mutual = Option.empty();

        List<Interest> interests = interestsService.getSubset(count, offset, tenantId, renterId, mutual);

        assertFalse(interests.isEmpty());
        Logger.debug(interests.toString());


    }

    @Test
    public void canFilterOnMutualInterest() throws OffsetOutOfRangeException {

        User renter = new User("renter@renter.com", "Renter");
        User tenant = new User("tenant@tenant.com", "Tenant");

        UsersRepository usersRepository = new UsersRepository();

        usersRepository.save(renter);
        usersRepository.save(tenant);

        ArrayNode tenantIds = Json.newArray();
        tenantIds.add(tenant.id);

        interestsService.addInterests(renter, tenantIds);

        Option<Integer> count = Option.empty();
        Option<Integer> offset = Option.empty();
        Option<Long> tenantId = Option.apply(tenant.id);
        Option<Long> renterId = Option.apply(renter.id);
        Option<Boolean> mutual = Option.empty();

        List<Interest> interests = interestsService.getSubset(count, offset, tenantId, renterId, mutual);

        assertFalse(interests.isEmpty());
        assertTrue(interests.get(0).mutual);

        interestsService.setMutual(renter.id, tenant.id, false);

        interests = interestsService.getSubset(count, offset, tenantId, renterId, mutual);

        assertTrue(interests.isEmpty());

    }

}
