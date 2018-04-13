package group.greenbyte.lunchplanner.location;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer locationId;

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    /*
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="isAt",
            joinColumns = { @JoinColumn(name = "locationId")},
            inverseJoinColumns = { @JoinColumn(name = "eventId")}
    )
    private Set<Event> events = new HashSet<>();
    */

}
