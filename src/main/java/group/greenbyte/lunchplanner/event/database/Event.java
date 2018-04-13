package group.greenbyte.lunchplanner.event.database;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer eventTd;

    @Temporal(TemporalType.TIME)
    @Column(name="startDate")
    private Date startDate;

    @Temporal(TemporalType.TIME)
    @Column(name="endDate")
    private Date endDate;

    @Column(nullable = false)
    private boolean isPublic;

    @Column(nullable = false, length = 50)
    private String eventName;

    @Column(length = 1000)
    private String eventDescription;

    @Column
    private int locationId;

    /*
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="eventInvited",
            joinColumns = { @JoinColumn(name = "eventId")},
            inverseJoinColumns = { @JoinColumn(name = "userName")})
    private Set<User> usersInvited = new HashSet<>();
    */

    /*
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="eventAdmin",
            joinColumns = { @JoinColumn(name = "eventId")},
            inverseJoinColumns = { @JoinColumn(name = "userName")})
    private Set<User> usersAdmin = new HashSet<>();
    */

    /*
    @ManyToMany(mappedBy = "events")
    private Set<Location> locations = new HashSet<>();
    */

    public Event() {
        isPublic = false;
    }

    public Integer getEventTd() {
        return eventTd;
    }

    public void setEventTd(Integer eventTd) {
        this.eventTd = eventTd;
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

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
}
