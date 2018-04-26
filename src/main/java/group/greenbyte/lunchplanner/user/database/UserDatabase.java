package group.greenbyte.lunchplanner.user.database;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDatabase {

    private String userName;

    private String eMail;

    private String password;

    @JsonIgnore
    private String token;

    public User getUser() {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.seteMail(eMail);
        user.setToken(token);

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
}
