package group.greenbyte.lunchplanner.event.database;

import group.greenbyte.lunchplanner.location.database.Location;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Event {

    static final public int MAX_USERNAME_LENGHT = 50;
    static final public int MAX_DESCRITION_LENGTH = 1000;
    static final public int MAX_EVENTNAME_LENGTH = 50;
    static final public int MAX_SEARCHWORD_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer eventId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="startDate")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="endDate")
    private Date endDate;

    @Column(nullable = false)
    private boolean isPublic;

    @Column(nullable = false, length = MAX_EVENTNAME_LENGTH)
    private String eventName;

    @Column(length = MAX_DESCRITION_LENGTH)
    private String eventDescription;

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

    public Event() {
        isPublic = false;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

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

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
}
