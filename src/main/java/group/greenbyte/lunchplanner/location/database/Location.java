package group.greenbyte.lunchplanner.location.database;

import group.greenbyte.lunchplanner.event.database.Event;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Location {

    static final public int MAX_NAME_LENGTH = 50;
    static final public int MAX_DESCRIPION_LENGTH = 1000;
    static final public int MAX_USERNAME_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int locationId;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String locationName;

    @Column(length = MAX_DESCRIPION_LENGTH)
    private String locationDescription;

    @Column
    private Coordinate coordinate;

    @Column
    private boolean isPublic;

    @OneToMany(mappedBy = "userAdmin")
    private Set<LocationAdmin> locationAdmins = new HashSet<>();

    @OneToMany(mappedBy = "location")
    private Set<Event> events = new HashSet<>();

    public Location() {
        isPublic = false;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof Location) {
            Location otherLocation = (Location) object;
            return otherLocation.getLocationId() == this.getLocationId();
        }

        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + locationId;
        return result;
    }
}
