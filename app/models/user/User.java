package models.user;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import models.Edge;
import models.accommodation.Accommodation;
import models.accommodation.Address;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

/**
 * An entity representing a tenant.
 * Modified from https://github.com/jamesward/play-rest-security
 */
@Entity
@Inheritance // Ebeans does not support any other strategy than SINGLE_TABLE. This works fine, but remember that no fields in subclasses can be non-nullable.
@DiscriminatorValue("USER")
@Table(name = "users")
public class User extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @JsonIgnore
    private String authToken;

    //@Column(unique = true, nullable = false)
    @Column(unique = true)
    @Constraints.MaxLength(255)
    //@Constraints.Required
    @Constraints.Email
    @JsonIgnore
    private String emailAddress;

    @Column(length = 64)
    private byte[] shaPassword;

    @Transient
    @Constraints.MinLength(6)
    @Constraints.MaxLength(256)
    @JsonIgnore
    private String password;

    @Column()
    private byte[] shaFacebookAuthToken;

    @Transient
    @JsonIgnore
    private String facebookAuthToken;

    //@Column(length = 256, nullable = false)
    @Column(unique = true)
    //@Constraints.Required
    @Constraints.MinLength(2)
    @Constraints.MaxLength(256)
    @JsonIgnore
    public String fullName;

    @Column(nullable = false, columnDefinition = "datetime") // columnDefinition prevents ebeans from generating
    @JsonIgnore
    public Date creationDate;                                // SQL that the DSV mysql server cannot handle.

    public int age;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "renter")
    public Set<Edge> interests;

    @OneToOne
    @JsonUnwrapped
    public FacebookData facebookData;

    @OneToOne
    public Accommodation accommodation;

    @OneToOne
    public TenantProfile tenantProfile;

    @ManyToOne
    @JsonIgnore
    public Authorization authorization = Authorization.USER;

    private static Finder<Long, User> find = new Finder<>(User.class);

    public User() {
        this.creationDate = new Date();
    }

    public User(String emailAddress, String password, String fullName) {

        setEmailAddress(emailAddress);
        setPassword(password);
        this.fullName = fullName;
        this.creationDate = new Date();

    }

    public User(String emailAddress, String fullName) {

        setEmailAddress(emailAddress);
        this.fullName = fullName;
        this.creationDate = new Date();

    }

    public User(String emailAddress, String password, String fullName, int age) {

        setEmailAddress(emailAddress);
        setPassword(password);

        this.fullName = fullName;
        this.creationDate = new Date();
        this.age = age;

    }

    public Edge addInterest(User tenant) {

        if (Edge.findByRenterAndTenant(id, tenant.id) != null) {
            throw new IllegalArgumentException("You may not add an interest that has already been added.");
        }

        Edge interest = new Edge(this, tenant);
        interest.save();

        interests.add(interest);

        save();

        return interest;

    }

    public void addInterest(Edge interest) {
        interests.add(interest);
    }

    public Accommodation createAccommodation(int rent, double size, double rooms, int deposit,
                                             Address address) {

        Accommodation accommodation = new Accommodation(rent, size, rooms, deposit,
                address, this);

        accommodation.save();

        this.accommodation = accommodation;
        save();

        return accommodation;

    }

    public static byte[] getSha512(String value) {
        
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress.toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = password;
        shaPassword = getSha512(password);

    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setFacebookAuthToken(String facebookAuthToken) {

        this.facebookAuthToken = facebookAuthToken;
        shaFacebookAuthToken = getSha512(facebookAuthToken);

    }

    public void setFacebookData(FacebookData facebookData) {
        this.facebookData = facebookData;
    }

    public String createToken() {

        authToken = UUID.randomUUID().toString();
        return authToken;

    }

    public void deleteAuthToken() {
        authToken = null;
    }

    public static User findByAuthToken(String authToken) {
        
        if (authToken == null) {
            return null;
        }

        try {
            return find.where().eq("authToken", authToken).findUnique();
        } catch (Exception e) {
            return null;
        }
    }

    public static User findByEmailAddress(String emailAddress) {
        return find.where().eq("emailAddress", emailAddress).findUnique();
    }

    public static User findByEmailAddressAndPassword(String emailAddress, String password) {
        return find.where().eq("emailAddress", emailAddress.toLowerCase()).eq("shaPassword", getSha512(password)).findUnique();
    }

    public static User findById(long id) {
        return find.byId(id);
    }

    public static List<User> filterBy(List<Function<ExpressionList<User>, ExpressionList<User>>> functions) {

        ExpressionList<User> expressionList = find.where();

        for (Function<ExpressionList<User>, ExpressionList<User>> function : functions) {
            expressionList = function.apply(expressionList);
        }

        return expressionList.findList();

    }
    public static List<User> findByMaxRent(int maxRent){
            List<User>  users = find.where().le("tenantProfile.maxRent", maxRent).findList();

        return users;
    }


}
