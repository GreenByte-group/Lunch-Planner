package group.greenbyte.lunchplanner.team.database;

import group.greenbyte.lunchplanner.user.database.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class TeamMember implements Serializable {

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teamId")
    private Team team;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userName")
    private User user;

    private boolean isAdmin;

}
