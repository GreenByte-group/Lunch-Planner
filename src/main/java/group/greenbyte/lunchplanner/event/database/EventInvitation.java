package group.greenbyte.lunchplanner.event.database;

import group.greenbyte.lunchplanner.user.database.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class EventInvitation implements Serializable {

    @Id
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "eventId")
    private Event eventInvited;

    @Id
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "userName")
    private User userInvited;

    private boolean isAdmin;

    private boolean confirmed;

    public EventInvitation() {
        isAdmin = false;
        confirmed = false;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Event getEventInvited() {
        return eventInvited;
    }

    public void setEventInvited(Event eventInvited) {
        this.eventInvited = eventInvited;
    }

    public User getUserInvited() {
        return userInvited;
    }

    public void setUserInvited(User userInvited) {
        this.userInvited = userInvited;
    }
}
