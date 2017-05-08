package repositories.users;

import models.user.User;

/**
 * @author Simon Olofsson
 */
public class UsersRepository implements UserStorage {

    @Override
    public User findByAuthToken(String authToken) {
        return User.findByAuthToken(authToken);
    }

    @Override
    public User findByEmailAddress(String email) {
        return User.findByEmailAddress(email);
    }

    @Override
    public User findByEmailAddressAndPassword(String emailAddress, String password) {
        return User.findByEmailAddressAndPassword(emailAddress, password);
    }

    @Override
    public User findById(long id) {
        return User.findById(id);
    }

    @Override
    public void save(User user) {
        user.save();
    }
}
