package repositories.users;

import models.user.User;
import scala.Option;

import java.util.List;

/**
 * @author Simon Olofsson
 */
public interface UserStorage {

    User findByAuthToken(String authToken);

    User findByEmailAddress(String email);

    void save(User user);

    User findByEmailAddressAndPassword(String emailAddress, String password);

    User findById(long id);

    List<User> findUser (Option<Integer> maxRent, Option<Integer> maxDeposit);
}
