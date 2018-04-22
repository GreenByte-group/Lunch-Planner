package group.greenbyte.lunchplanner.event.database;

import group.greenbyte.lunchplanner.event.InvitationAnswer;
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
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userName")
    private User userInvited;

    private boolean isAdmin;

    private InvitationAnswer answer;

    public EventInvitation() {
        isAdmin = false;
        answer = InvitationAnswer.MAYBE;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
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

    public InvitationAnswer getAnswer() {
        return answer;
    }

    public void setAnswer(InvitationAnswer answer) {
        this.answer = answer;
    }
}
