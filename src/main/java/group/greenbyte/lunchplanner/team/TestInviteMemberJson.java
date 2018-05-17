package group.greenbyte.lunchplanner.team;

import java.io.Serializable;

public class TestInviteMemberJson implements Serializable {
    private int teamId;
    private String userToInvite;

    public TestInviteMemberJson(String userToInvite, int teamId){
        this.userToInvite = userToInvite;
        this.teamId = teamId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getUserToInvite() {
        return userToInvite;
    }

    public void setUserToInvite(String userToInvite) {
        this.userToInvite = userToInvite;
    }
}
