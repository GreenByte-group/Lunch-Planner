package group.greenbyte.lunchplanner.location.database;

import javax.persistence.*;

@Entity
public class Location {

    static final public int MAX_NAME_LENGTH = 50;
    static final public int MAX_DESCRIPION_LENGTH = 1000;
    static final public int MAX_USERNAME_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer locationId;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String locationName;

    @Column(length = MAX_DESCRIPION_LENGTH)
    private String locationDescription;

    @Column
    private Coordinate coordinate;

    public Location() {}

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
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
