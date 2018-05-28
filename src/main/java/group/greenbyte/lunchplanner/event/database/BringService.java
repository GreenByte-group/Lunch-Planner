package group.greenbyte.lunchplanner.event.database;

import group.greenbyte.lunchplanner.user.database.User;

import javax.persistence.*;
import java.io.Serializable;


@Entity
public class BringService implements Serializable {

    public BringService(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer serviceId;

    private String food;

    @ManyToOne
    @JoinColumn(name = "event_Id")
    private Event event_Id;

    @Transient
    private Integer event_ID;

    @ManyToOne
    @JoinColumn(name = "userName")
    private User creater;

    @Transient
    private String createrName;


    private String accepter;

    private String description;



    //gettER AN SETTER---------------------------------------------------------

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }


    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getAccepter() {
        return accepter;
    }

    public void setAccepter(String accepter) {
        this.accepter = accepter;
    }


    public Integer getEvent_ID() {
        return event_ID;
    }

    public void setEvent_ID(Integer event_ID) {
        this.event_ID = event_ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }
}
