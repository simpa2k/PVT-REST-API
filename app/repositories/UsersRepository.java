package repositories;

import models.user.User;

/**
 * @author Simon Olofsson
 */
public class UsersRepository {

    public User findByAuthToken(String authToken) {
        return User.findByAuthToken(authToken);
    }

    public User findByEmailAddress(String email) {
        return User.findByEmailAddress(email);
    }

    public User findByEmailAddressAndPassword(String emailAddress, String password) {
        return User.findByEmailAddressAndPassword(emailAddress, password);
    }

    public User findById(long id) {
        return User.findById(id);
    }

    public void save(User user) {
        user.save();
    }
}
