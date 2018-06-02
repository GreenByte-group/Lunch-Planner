package group.greenbyte.lunchplanner.user.database;

import javax.persistence.*;

@Entity
public class Subscribe {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int  subscribeId;

    @ManyToMany(cascade = CascadeType.ALL)
    @Column
    private String subscriber;
    private String location;


    //------------GETTER AND SETTER-----------------------
    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String userName) {
        this.subscriber = userName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
