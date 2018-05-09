package group.greenbyte.lunchplanner.user.database;

import group.greenbyte.lunchplanner.event.database.Comment;
import group.greenbyte.lunchplanner.event.database.EventInvitation;
import group.greenbyte.lunchplanner.team.database.TeamMember;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    static final public int MAX_USERNAME_LENGTH = 50;
    static final public int MAX_MAIL_LENGTH = 50;
    static final public int MAX_PASSWORD_LENGTH = 200;

    @Id
    @Column(unique = true,length = MAX_USERNAME_LENGTH)
    private String userName;

    @Column(nullable = false, length = MAX_MAIL_LENGTH)
    private String eMail;

    @Column(nullable = false, length = MAX_PASSWORD_LENGTH)
    private String password;

    @Column
    private String token;

    @OneToMany(mappedBy = "eventInvited", cascade = CascadeType.ALL)
    private Set<EventInvitation> eventsInvited;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private Set<TeamMember> teamsMember = new HashSet<>();

    @OneToMany(mappedBy = "eventComment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<EventInvitation> getEventsInvited() {
        return eventsInvited;
    }

    public void setEventsInvited(Set<EventInvitation> eventsInvited) {
        this.eventsInvited = eventsInvited;
    }

    public void addEventsInvited(EventInvitation eventInvitation) {
        if(eventsInvited == null)
            eventsInvited = new HashSet<>();

        eventsInvited.add(eventInvitation);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
