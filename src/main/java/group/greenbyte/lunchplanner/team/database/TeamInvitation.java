package group.greenbyte.lunchplanner.team.database;

import group.greenbyte.lunchplanner.team.InvitationAnswer;
import group.greenbyte.lunchplanner.user.database.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class TeamInvitation implements Serializable {

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teamId")
    private Team teamInvited;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userName")
    private User userInvited;

    private boolean isAdmin;

    private InvitationAnswer answer;
    
}
