package group.greenbyte.lunchplanner.event.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import group.greenbyte.lunchplanner.user.database.User;

import javax.persistence.*;
import java.io.Serializable;


@Entity
public class BringService implements Serializable {

    static final public int MAX_NAME_LENGTH = 50;
    static final public int MAX_DESCRIPTION_LENGTH = 1000;

    public BringService(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer serviceId;

    @Column(length = MAX_NAME_LENGTH)
    private String food;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "eventId")
    private Event event;

    @Transient
    private Integer eventId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userName")
    private User creator;

    @Transient
    private String creatorName;

    private String accepter;

    @Column(length = MAX_DESCRIPTION_LENGTH)
    private String description;



    //gettER AN SETTER---------------------------------------------------------

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }


    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getAccepter() {
        return accepter;
    }

    public void setAccepter(String accepter) {
        this.accepter = accepter;
    }


    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
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
