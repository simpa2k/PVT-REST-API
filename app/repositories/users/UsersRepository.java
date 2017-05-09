package repositories.users;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import models.accommodation.Accommodation;
import models.user.User;
import scala.Option;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

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

    @Override
    public List<User> findUser (final Option<Integer> maxRent, final Option<Integer> maxDeposit){
       List<Function<ExpressionList<User>, ExpressionList<User>>> functions = Arrays.asList(

           exprList ->maxRent.isDefined() ? exprList.le("tenantProfile.maxRent", maxRent.get()) : exprList,
           exprList ->maxDeposit.isDefined() ? exprList.le("tenantProfile.maxDeposit", maxDeposit.get()) : exprList

        );

        return User.filterBy(functions);


    }
}
