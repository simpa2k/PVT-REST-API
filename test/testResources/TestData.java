/*
 * Modified from https://github.com/jamesward/play-rest-security
 */

package testResources;

import models.Activity;
import models.Interest;
import models.accommodation.Accommodation;
import models.user.Authorization;
import models.SwipingSession;
import models.user.Renter;
import models.user.Tenant;
import models.user.User;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class TestData {

    private User user1;
    private User user2;
    private User admin;

    private Renter renter1;
    private Accommodation renter1Accommodation;

    private Tenant tenant1;
    private Interest interest1;

    private SwipingSession session;
    private Activity modernaMuseet;

    private String facebookToken;

    @Inject
    public TestData(Configuration config) {
        
        if (User.findByEmailAddressAndPassword("user1@demo.com", "password") == null) {

            user1 = new User("user1@demo.com", "password", "John Doe");
            user1.save();

            user2 = new User("user2@demo.com", "password", "Jane Doe");
            user2.save();

            admin = new User("admin@demo.com", "password", "Sven Svensson");
            admin.authorization = Authorization.ADMIN;
            admin.save();

            Set<User> participants = new HashSet<>();

            participants.add(user1);
            participants.add(user2);

            modernaMuseet = new Activity("Moderna Museet");
            modernaMuseet.save();

            Set<Activity> activities = new HashSet<>();
            activities.add(modernaMuseet);

            session = new SwipingSession(participants, activities);
            session.save();

            addInterestSpecificData();

        }

        facebookToken = config.getString("facebookToken");

    }

    private void addInterestSpecificData() {

        renter1 = new Renter("anna@example.com", "password", "Anna Svensson", "Hej! Jag är en skön prick.", 35);
        renter1.save();

        renter1Accommodation = new Accommodation(5000, 20, 1, 8000, false, false, true, true, "Schysst ställe!", renter1);
        renter1Accommodation.save();

        renter1.accommodation = renter1Accommodation;
        renter1.save();

        tenant1 = new Tenant("kalle@example.com", "password", "Kalle Blomkvist",
                "Hej! Jag letar boende", 23, 1, 5000, 18000, "Karaktär i berättelse", 8000);
        tenant1.save();

        tenant1.addInterest(renter1Accommodation);

    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public User getAdmin() {
        return admin;
    }

    public Renter getRenter1() {
        return renter1;
    }

    public Accommodation getRenter1Accommodation() {
        return renter1Accommodation;
    }

    public SwipingSession getSession() {
        return session;
    }

    public Activity getModernaMuseet() {
        return modernaMuseet;
    }

    public String getFacebookToken() {
        return facebookToken;
    }
}
