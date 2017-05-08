package models.user;

import com.avaje.ebean.Model;
import models.RentalPeriod;

import javax.persistence.*;
import java.io.File;
import java.util.List;

/**
 * @author Simon Olofsson
 */
@Entity
public class TenantProfile extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public int maxRent;
    public int maxDeposit;
    public String description;

    @ManyToOne
    public RentalPeriod rentalPeriod;

    private static Model.Finder<Long, TenantProfile> find = new Finder<>(TenantProfile.class);

    public TenantProfile(int maxRent, int maxDeposit, String description) {

        this.maxRent = maxRent;
        this.maxDeposit = maxDeposit;
        this.description = description;

    }

    public static TenantProfile findById(long tenantId) {
        return find.byId(tenantId);
    }

}
