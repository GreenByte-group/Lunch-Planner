package group.greenbyte.lunchplanner.user;

import java.io.Serializable;

public class TestInvitePersonJson implements Serializable {

    private int eventId;
    private String username;
    private String userToInvite;

    public TestInvitePersonJson(String username, String userToInvite, int eventId){
        this.username = username;
        this.userToInvite = userToInvite;
        this.eventId = eventId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToInviteUsername() {
        return userToInvite;
    }

    public void setToInviteUsername(String userToInvite) {
        this.userToInvite = userToInvite;
    }


}
