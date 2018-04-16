package group.greenbyte.lunchplanner.user;

import java.io.Serializable;

public class TestInvitePersonJson implements Serializable {

    private int eventId;
    private String username;
    private String toInviteUsername;

    public TestInvitePersonJson(String username, String toInviteUsername, int eventId){
        this.username = username;
        this.toInviteUsername = toInviteUsername;
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
        return toInviteUsername;
    }

    public void setToInviteUsername(String toInviteUsername) {
        this.toInviteUsername = toInviteUsername;
    }


}
