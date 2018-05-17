package group.greenbyte.lunchplanner.team.database;

import group.greenbyte.lunchplanner.user.database.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class TeamMember implements Serializable {

    @Id
<<<<<<< HEAD
    @ManyToOne(cascade = CascadeType.ALL)
=======
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    @JoinColumn(name = "teamId")
    private Team team;

    @Id
<<<<<<< HEAD
    @ManyToOne(cascade = CascadeType.ALL)
=======
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    @JoinColumn(name = "userName")
    private User user;

    private boolean isAdmin;

    public TeamMember() {
        isAdmin = false;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
