package group.greenbyte.lunchplanner.team.database;

import group.greenbyte.lunchplanner.event.database.EventTeamVisible;

import javax.persistence.*;
import javax.print.DocFlavor;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Team {

    public static final int MAX_TEAMNAME_LENGHT = 50;
    public static final int MAX_DESCRIPTION_LENGHT = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int teamId;

    @Column
    private boolean isPublic;

    @Column(length = MAX_TEAMNAME_LENGHT, nullable = false)
    private String teamName;

    @Column(length = MAX_DESCRIPTION_LENGHT)
    private String description;

    @OneToMany(mappedBy = "user")
    private Set<TeamMember> teamsMember;

    @OneToMany(mappedBy = "event")
    private Set<EventTeamVisible> eventsVisible;

    @ManyToOne
    @JoinColumn(name = "parentTeam")
    private Team parentTeam;

    @OneToMany(mappedBy = "parentTeam")
    private Set<Team> childTeams = new HashSet<>();

    public Team() {
        isPublic = false;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Set<EventTeamVisible> getEventsVisible() {
        return eventsVisible;
    }

    public void setEventsVisible(Set<EventTeamVisible> eventsVisible) {
        this.eventsVisible = eventsVisible;
    }

    public Set<TeamMember> getTeamsMember() {
        return teamsMember;
    }

    public void setTeamsMember(Set<TeamMember> teamsMember) {
        this.teamsMember = teamsMember;
    }

    public void addTeamsMember(TeamMember teamMember) {
        if (teamsMember == null)
            teamsMember = new HashSet<>();

        teamsMember.add(teamMember);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Team getParentTeam() {
        return parentTeam;
    }

    public void setParentTeam(Team parentTeam) {
        this.parentTeam = parentTeam;
    }

    public Set<Team> getChildTeams() {
        return childTeams;
    }

    public void setChildTeams(Set<Team> childTeams) {
        this.childTeams = childTeams;
    }
}
