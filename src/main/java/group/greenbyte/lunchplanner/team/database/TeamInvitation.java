package group.greenbyte.lunchplanner.team.database;

import group.greenbyte.lunchplanner.user.database.User;

import javax.persistence.*;

@Entity
public class TeamInvitation {

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teamId")
    private Team teamInvited;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private User userInvited;

    private boolean isAdmin;

    private boolean confirmed;

    public TeamInvitation() {
        isAdmin = false;
        confirmed = false;
    }

    public Team getTeamInvited() {
        return teamInvited;
    }

    public void setTeamInvited(Team teamInvited) {
        this.teamInvited = teamInvited;
    }

    public User getUserInvited() {
        return userInvited;
    }

    public void setUserInvited(User userInvited) {
        this.userInvited = userInvited;
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
}
