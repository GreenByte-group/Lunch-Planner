package group.greenbyte.lunchplanner.user.database;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDatabase {


    private String userName;

    private String eMail;

    private String password;

    @JsonIgnore
    private String token;

    private String fcmToken;

    private String profilePictureUrl;

    public User getUser() {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.seteMail(eMail);
        user.setToken(token);
        user.setFcmToken(fcmToken);
        user.setProfilePictureUrl("/static"+profilePictureUrl);

        return user;
    }

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

    public void setToken(String token) {
        this.token = token;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
