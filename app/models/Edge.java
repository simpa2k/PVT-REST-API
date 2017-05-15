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
public class Edge extends Model {

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

    public boolean active = true;

    private static Finder<Long, Edge> find = new Finder<>(Edge.class);

    public Edge(User renter, User tenant) {

        this.renter = renter;
        this.tenant = tenant;

    }

    public Edge(User renter, User tenant, boolean active) {

        this.renter = renter;
        this.tenant = tenant;
        this.active = active;

    }

    public static List<Edge> filterBy(List<Function<ExpressionList<Edge>, ExpressionList<Edge>>> functions) {

        ExpressionList<Edge> expressionList = find.where();

        for (Function<ExpressionList<Edge>, ExpressionList<Edge>> function : functions) {
            expressionList = function.apply(expressionList);
        }

        return expressionList.findList();

    }

    public static List<Edge> findAll() {
        return find.all();
    }

    public static Edge findByRenterAndTenant(long renterId, long tenantId) {
        return find.where().eq("renter_id", renterId).eq("tenant_id", tenantId).findUnique();
    }
}
