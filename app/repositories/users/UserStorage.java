package repositories.users;

import models.user.User;

/**
 * @author Simon Olofsson
 */
public interface UserStorage {

    User findByAuthToken(String authToken);

    User findByEmailAddress(String email);

    void save(User user);

    User findByEmailAddressAndPassword(String emailAddress, String password);

    User findById(long id);
}
