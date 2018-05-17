package group.greenbyte.lunchplanner.event.database;

<<<<<<< HEAD
=======
import group.greenbyte.lunchplanner.event.InvitationAnswer;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
import group.greenbyte.lunchplanner.user.database.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class EventInvitation implements Serializable {

    @Id
<<<<<<< HEAD
    @ManyToOne(cascade = CascadeType.ALL)
=======
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    @JoinColumn(name = "eventId")
    private Event eventInvited;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
<<<<<<< HEAD
    @JoinColumn(name = "userId")
=======
    @JoinColumn(name = "userName")
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    private User userInvited;

    private boolean isAdmin;

<<<<<<< HEAD
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
=======
    private InvitationAnswer answer;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
}
