package group.greenbyte.lunchplanner.event.database;

import group.greenbyte.lunchplanner.user.database.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @ManyToOne
    @JoinColumn(name = "eventId")
    private Event eventComment;

    @ManyToOne
    @JoinColumn(name = "userName")
    private User userComment;

    private String text;

    private Date time;

}
