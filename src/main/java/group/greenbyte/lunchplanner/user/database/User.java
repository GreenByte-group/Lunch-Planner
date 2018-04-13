package group.greenbyte.lunchplanner.user.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    static final public int MAX_USERNAME_LENGTH = 50;
    static final public int MAX_MAIL_LENGTH = 50;
    static final public int MAX_PASSWORD_LENGTH = 80;

    @Id
    @Column(nullable = false, length = MAX_USERNAME_LENGTH)
    private String userName;

    @Column(nullable = false, length = MAX_MAIL_LENGTH)
    private String eMail;

    @Column(nullable = false, length = MAX_PASSWORD_LENGTH)
    private String password;

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

    /*
    @ManyToMany(mappedBy = "usersInvited")
    private Set<Event> eventsInvited = new HashSet<>();

    @ManyToMany(mappedBy = "usersAdmin")
    private Set<Event> eventsAdmin = new HashSet<>();
    */

}
