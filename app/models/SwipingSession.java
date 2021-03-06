package models;

import com.avaje.ebean.*;
import models.user.User;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * An entity representing a swiping session between a set of users. Apart from
 * having a set of users associated with it, each swiping session also keeps
 * track of their chosen activities.
 *
 * @author Simon Olofsson
 */
@Entity
public class SwipingSession extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToMany
    public Set<User> participatingUsers = new HashSet<>();

    @ManyToMany
    public Set<Activity> generatedActivities;

    @OneToMany
    public List<ActivityChoice> chosenActivities;

    @Column(nullable = false, columnDefinition = "datetime") // columnDefinition prevents ebeans from generating
    public Date initializationDate;                          // SQL that the DSV mysql server cannot handle.

    private static Finder<Long, SwipingSession> find = new Finder<>(SwipingSession.class);

    public SwipingSession(Set<User> participatingUsers, Set<Activity> generatedActivities) {

        this.participatingUsers = participatingUsers;
        this.generatedActivities = generatedActivities;
        this.initializationDate = new Date();

    }

    /**
     * Method to record a tenant's choice of activities.
     *
     * @param userEmailAddress the email address of the tenant whose choice is to
     *                         be recorded.
     * @param activities the activities to associate with the tenant.
     * @throws IllegalArgumentException if any of the activities chosen are not
     * in the original set of generated activities.
     */
    public void setUserActivityChoice(String userEmailAddress, Set<Activity> activities) {

        if (!generatedActivities.containsAll(activities)) {
            throw new IllegalArgumentException("Chosen activities must be picked from the original set of generated activities.");
        }

        User user = User.findByEmailAddress(userEmailAddress);
        ActivityChoice choice = new ActivityChoice(user, this, activities);
        choice.save();

        chosenActivities.add(choice);

    }

    /**
     * Method to get the activities chosen by a tenant during this swiping session.
     *
     * @param userEmailAddress the email address of the tenant whose activities
     *                         are to be returned.
     * @return A list containing the activities chosen by the tenant,
     * may be empty.
     */
    public Set<Activity> getChosenActivities(String userEmailAddress) {
        return ActivityChoice.findBySwipingSessionAndUser(User.findByEmailAddress(userEmailAddress).id, id).activities;
    }


    public static List<SwipingSession> findByEmail(String email) {
        return find.where().in("participatingUsers", User.findByEmailAddress(email)).findList();
    }

    /**
     * Method to get all swiping sessions where all of the users and exactly
     * those users associate with the passed email addresses have taken part.
     * That is, will only return those swiping sessions whose set of users
     * are exactly equal to those indicated by the email addresses.
     *
     * @param emails a list of emails to match swiping sessions against.
     * @return a list of swiping sessions that are associated with exactly
     * those users indicated by the email addresses passed.
     */
    public static List<SwipingSession> findByEmails(List<String> emails) {

        Set<User> users = new HashSet<>();
        emails.forEach(email -> users.add(User.findByEmailAddress(email)));

        List<SwipingSession> allSwipingSessions = find.all();

        return allSwipingSessions.stream()
                .filter(swipingSession -> swipingSession.participatingUsers.equals(users)).collect(Collectors.toList());

    }

    /**
     * Method to find a swiping session by its id.
     *
     * @param id the id of the swiping session to get.
     * @return the swiping session indicated by the id, null if there is none.
     */
    public static SwipingSession findById(long id) {
        return find.byId(id);
    }
}
