package group.greenbyte.lunchplanner.event.database;

import group.greenbyte.lunchplanner.location.database.Location;

import javax.persistence.*;
import java.util.*;

@Entity
public class Event {

    static final public int MAX_USERNAME_LENGHT = 50;
    static final public int MAX_DESCRITION_LENGTH = 1000;
    static final public int MAX_EVENTNAME_LENGTH = 50;
    static final public int MAX_SEARCHWORD_LENGTH = 50;
    static final public int MAX_COMMENT_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;

    @Column(name="startDate")
    private Date startDate;

    @Column(name="endDate")
    private Date endDate;

    @Column(nullable = false)
    private boolean isPublic;

    @Column(nullable = false, length = MAX_EVENTNAME_LENGTH)
    private String eventName;

    @Column(length = MAX_DESCRITION_LENGTH)
    private String eventDescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "locationId")
    private Location location;

    @OneToMany(mappedBy = "userInvited", fetch = FetchType.EAGER)
    private Set<EventInvitation> usersInvited = new HashSet<>();

    @OneToMany(mappedBy = "commentId", fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    public Event() {
        isPublic = false;
    }

    public Integer getEventId() {
        return eventId;
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

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
