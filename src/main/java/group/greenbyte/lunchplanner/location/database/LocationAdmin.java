package group.greenbyte.lunchplanner.location.database;

import group.greenbyte.lunchplanner.user.database.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class LocationAdmin implements Serializable {

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="locationId")
    private Location location;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="userName")
    private User user;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
