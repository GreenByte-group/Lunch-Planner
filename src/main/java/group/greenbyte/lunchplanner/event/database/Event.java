package group.greenbyte.lunchplanner.event.database;

<<<<<<< HEAD
import group.greenbyte.lunchplanner.location.database.Location;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
=======
import javax.persistence.*;
import java.util.*;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

@Entity
public class Event {

    static final public int MAX_USERNAME_LENGHT = 50;
    static final public int MAX_DESCRITION_LENGTH = 1000;
    static final public int MAX_EVENTNAME_LENGTH = 50;
    static final public int MAX_SEARCHWORD_LENGTH = 50;
<<<<<<< HEAD

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer eventId;


    @Column(name="startDate")
    private Date startDate;

    @Column(name="endDate")
    private Date endDate;

=======
    static final public int MAX_COMMENT_LENGTH = 100;
    static final public int MAX_LOCATION_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;

    @Column(name="startDate")
    private Date startDate;

>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    @Column(nullable = false)
    private boolean isPublic;

    @Column(nullable = false, length = MAX_EVENTNAME_LENGTH)
    private String eventName;

    @Column(length = MAX_DESCRITION_LENGTH)
    private String eventDescription;

<<<<<<< HEAD
    @ManyToOne
    @JoinColumn(name = "locationId")
    private Location location;

    @OneToMany(mappedBy = "userInvited")
    private Set<EventInvitation> usersInvited = new HashSet<>();

    @OneToMany(mappedBy = "team")
    private Set<EventTeamVisible> teamsVisible = new HashSet<>();

    /*
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="eventAdmin",
            joinColumns = { @JoinColumn(name = "eventId")},
            inverseJoinColumns = { @JoinColumn(name = "userName")})
    private Set<User> usersAdmin = new HashSet<>();
    */
=======
    @Column(length = MAX_LOCATION_LENGTH)
    private String location;

    @OneToMany(mappedBy = "userInvited", fetch = FetchType.EAGER)
    private Set<EventInvitation> usersInvited = new HashSet<>();

    @OneToMany(mappedBy = "userComment", fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    @Transient
    private Set<EventInvitationDataForReturn> invitations = new HashSet<>();
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

    public Event() {
        isPublic = false;
    }

    public Integer getEventId() {
        return eventId;
    }

<<<<<<< HEAD
    public void setEventTd(Integer eventId) {
        this.eventId = eventId;
    }

=======
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

<<<<<<< HEAD
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

=======
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

<<<<<<< HEAD
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<EventInvitation> getUsersInvited() {
        return usersInvited;
    }

    public void setUsersInvited(Set<EventInvitation> usersInvited) {
        this.usersInvited = usersInvited;
    }

    public void addUsersInvited(EventInvitation eventInvitation) {
        if(usersInvited == null) {
            usersInvited = new HashSet<>();
        }

        usersInvited.add(eventInvitation);
    }

    public Set<EventTeamVisible> getTeamsVisible() {
        return teamsVisible;
    }

    public void setTeamsVisible(Set<EventTeamVisible> teamsVisible) {
        this.teamsVisible = teamsVisible;
    }


    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
=======
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<EventInvitationDataForReturn> getInvitations() {
        return invitations;
    }

    public void setInvitations(Set<EventInvitationDataForReturn> invitations) {
        this.invitations = invitations;
    }

    @Override
    public String toString() {
        return String.valueOf(getEventId());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Event) {
            Event event = (Event) obj;
            return event.getEventId().equals(getEventId());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return getEventId().hashCode();
    }


>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
}
