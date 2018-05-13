package group.greenbyte.lunchplanner.team.database;

public class TeamInvitationDataForReturn {

    private String userName;
    private int answer;
    private boolean admin;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setIsAdmin(boolean admin) {
        this.admin = admin;
    }
}
