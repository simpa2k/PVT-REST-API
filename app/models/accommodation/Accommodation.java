package models.accommodation;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.*;
import models.RentalPeriod;
import models.user.User;
import scala.Option;

import javax.persistence.*;
import java.util.List;
import java.util.function.Function;

/**
 * @author Simon Olofsson
 */
@Entity
public class Accommodation extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public int rent;
    public double size;
    public double rooms;
    public int deposit;

    /*
     * The JsonIdentity annotations make sure that only id is serialized.
     */
    @OneToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("renterId")
    public User renter;

    @ManyToOne
    public Address address;

    @ManyToOne
    public RentalPeriod rentalPeriod;

    private static Finder<Long, Accommodation> find = new Finder<>(Accommodation.class);

    public Accommodation(int rent, double size, double rooms, int deposit,
                         Address address, User renter) {

        this.rent = rent;
        this.size = size;
        this.rooms = rooms;
        this.deposit = deposit;
        this.address = address;
        this.renter = renter;

    }

    public static Accommodation findById(long id) {
        return find.byId(id);
    }

    public static List<Accommodation> findAll() {
        return find.all();
    }

    public static List<Accommodation> filterBy(List<Function<ExpressionList<Accommodation>, ExpressionList<Accommodation>>> functions) {

        ExpressionList<Accommodation> expressionList = find.where();

        for (Function<ExpressionList<Accommodation>, ExpressionList<Accommodation>> function : functions) {
            expressionList = function.apply(expressionList);
        }

        return expressionList.findList();

    }

    /*
     * Example
     */
    /*public static List<Accommodation> findByRentAndDeposit(Option<Integer> rent, Option<Integer> deposit) {

        // SELECT * FROM ACCOMMODATION WHERE ;
        // SELECT * FROM ACCOMMODATION WHERE rent <= 5000;
        // SELECT * FROM ACCOMMODATION WHERE rent <= 10 AND deposit <= 8000;

        ExpressionList<Accommodation> expressionList = find.where();

        if (rent.isDefined()) {
            expressionList = expressionList.le("rent", rent);
        }

        if (deposit.isDefined()) {
            expressionList = expressionList.le("deposit", deposit);
        }

        List<Accommodation> accommodation = expressionList.findList();

        return accommodation;

    }*/
}
