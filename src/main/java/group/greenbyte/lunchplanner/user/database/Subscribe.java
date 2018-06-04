package group.greenbyte.lunchplanner.user.database;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Subscribe implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name = "userName")
    private User subscriber;

    @Id
    private String location;

    @Transient
    private String user;


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
}
