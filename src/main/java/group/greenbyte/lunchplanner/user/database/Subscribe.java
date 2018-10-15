package group.greenbyte.lunchplanner.user.database;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Subscribe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subscribeId;

    @OneToOne
    @JoinColumn(name = "userName")
    private User subscriber;

    @Column(name = "location")
    private String location;

    @Transient
    private String user;

    @Column(name = "locationName")
    private String locationName;

    //------------GETTER AND SETTER-----------------------
    public String getSubscriber() {
        return user;
    }

    public void setSubscriber(String userName) {
        this.user = userName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationName() { return locationName; }

    public void setLocationName(String locationName) { this.locationName = locationName; }


}
