package models;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.*;
import models.user.User;

import javax.persistence.*;
import java.util.List;
import java.util.function.Function;

/**
 * @author Simon Olofsson
 */
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"renter_id", "tenant_id"}))
@Entity
public class Interest extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public long id;

    @ManyToOne
    @JsonProperty("renter")
    public User renter;

    /*
     * The JsonIdentity annotations make sure that only id is serialized.
     */
    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("tenantId")
    public User tenant;

    public boolean mutual = true;

    private static Finder<Long, Interest> find = new Finder<>(Interest.class);

    public Interest(User renter, User tenant) {

        this.renter = renter;
        this.tenant = tenant;

    }

    public static List<Interest> filterBy(List<Function<ExpressionList<Interest>, ExpressionList<Interest>>> functions) {

        ExpressionList<Interest> expressionList = find.where();

        for (Function<ExpressionList<Interest>, ExpressionList<Interest>> function : functions) {
            expressionList = function.apply(expressionList);
        }

        return expressionList.findList();

    }

    public static List<Interest> findAll() {
        return find.all();
    }

    public static Interest findByRenterAndTenant(long renterId, long tenantId) {
        return find.where().eq("renter_id", renterId).eq("tenant_id", tenantId).findUnique();
    }
}
