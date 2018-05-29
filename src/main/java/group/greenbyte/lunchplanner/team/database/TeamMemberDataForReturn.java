package group.greenbyte.lunchplanner.team.database;

public class TeamMemberDataForReturn {

    private String userName;
    private boolean admin;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setIsAdmin(boolean admin) {
        this.admin = admin;
    }
}
