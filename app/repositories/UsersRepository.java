package repositories;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import models.accommodation.Accommodation;
import models.user.User;
import play.Logger;
import scala.Option;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.Date;

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


    public List<User> findUsers (final String authToken,
                                 final Option<Integer> maxRent, final Option<Integer> maxDeposit,
                                 final Option<String> start , final Option<String> end)


    {
       List<Function<ExpressionList<User>, ExpressionList<User>>> functions = Arrays.asList(

           exprList -> exprList.ne("auth_token", authToken),
           exprList ->maxRent.isDefined() ? exprList.le("tenantProfile.maxRent", maxRent.get()) : exprList,
           exprList ->maxDeposit.isDefined() ? exprList.le("tenantProfile.maxDeposit", maxDeposit.get()) : exprList,
           exprList ->{

               if(start.isDefined()){
                   try{
                       Date startDate = convertStringToDate(start);
                       return exprList.ge("tenantProfile.rentalPeriod.start", startDate);

                   }catch (ParseException e){
                       Logger.error("Could not parse start date while finding user");
                   }
               }


               return exprList;
           },
           exprList ->{

               if(end.isDefined()){
                   try{
                       Date endDate = convertStringToDate(end);
                       return exprList.le("tenantProfile.rentalPeriod.end", endDate);

                   }catch (ParseException e){
                       Logger.error("Could not parse start date while finding user");
                   }
               }

               return exprList;
           }

       );

        return User.filterBy(functions);
    }

    private Date convertStringToDate(Option<String> start) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(start.get());
    }
}
