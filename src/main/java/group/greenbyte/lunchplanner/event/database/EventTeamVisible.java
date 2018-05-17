package group.greenbyte.lunchplanner.event.database;

import group.greenbyte.lunchplanner.team.database.Team;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class EventTeamVisible implements Serializable {

    @Id
<<<<<<< HEAD
    @ManyToOne(cascade = CascadeType.ALL)
=======
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    @JoinColumn(name = "eventId")
    private Event event;

    @Id
<<<<<<< HEAD
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teamId")
    private Team team;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
=======
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "teamId")
    private Team team;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
}
