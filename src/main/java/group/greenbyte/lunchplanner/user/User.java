package group.greenbyte.lunchplanner.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @Column(nullable = false, length = 50)
    private String userName;

    @Column(nullable = false, length = 50)
    private String eMail;

    @Column(nullable = false, length = 80)
    private String password;

    /*
    @ManyToMany(mappedBy = "usersInvited")
    private Set<Event> eventsInvited = new HashSet<>();

    @ManyToMany(mappedBy = "usersAdmin")
    private Set<Event> eventsAdmin = new HashSet<>();
    */

}
