package models.user;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * An entity storing data from Facebook.
 *
 * @author Simon Olofsson.
 */
@Entity
public class FacebookData extends Model {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @JsonIgnore
   public long id;

   @Column(unique = true, nullable = false)
   @Constraints.Required
   //@JsonIgnore
   @JsonProperty(value = "id", access = JsonProperty.Access.WRITE_ONLY)
   public String facebookUserId;

   @Column(unique = true, nullable = false)
   @Constraints.MaxLength(255)
   @Constraints.Required
   @Constraints.Email
   public String email;

   @Column(length = 256)
   @Constraints.MaxLength(256)
   //@JsonProperty("first_name")
   public String firstName;

   @Column(length = 256)
   @Constraints.MaxLength(256)
   //@JsonProperty("last_name")
   public String lastName;

   public String name;

   @Column(length = 15)
   @Constraints.MaxLength(256)
   public String gender;

   @Column(length = 10)
   @Constraints.MaxLength(10)
   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   public String locale;

   @Column
   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   public int timezone;

   @OneToOne
   @JsonIgnore
   public User user;

   @Lob
   public byte[] image;

   private static Finder<Long, FacebookData> find = new Finder<>(FacebookData.class);

   public FacebookData() {};

   public FacebookData(String facebookUserId, String email, String firstName, String lastName, String gender, String locale, int timezone) {

       this.facebookUserId = facebookUserId;
       this.email = email;
       this.firstName = firstName;
       this.lastName = lastName;
       this.name = firstName + " " + lastName;
       this.gender = gender;
       this.locale = locale;
       this.timezone = timezone;

   }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("first_name")
    public void setFirstNameUnderscored(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("last_name")
    public void setLastNameUnderScored(String lastName) {
        this.lastName = lastName;
    }

    public static FacebookData findByFacebookUserId(String facebookUserId) {
        return find.where().eq("facebookUserId", facebookUserId).findUnique();
    }

   public static FacebookData findByEmailAddress(String emailAddress) {
      return find.where().eq("emailAddress", emailAddress).findUnique();
   }
}
