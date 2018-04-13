package group.greenbyte.lunchplanner.user;

import java.io.Serializable;

/**
 * Stores all data that will be send or received over REST API. This class is used to convert Java
 * objects into json or json into java objects
 */
public class UserJson implements Serializable {

    private String userName;
    private String password;
    private String mail;

    public UserJson(String userName, String password, String mail) {
        this.userName = userName;
        this.password = password;
        this.mail = mail;
    }

    public UserJson() {}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
