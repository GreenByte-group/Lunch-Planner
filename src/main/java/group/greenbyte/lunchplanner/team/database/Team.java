package group.greenbyte.lunchplanner.team.database;

<<<<<<< HEAD
=======
import group.greenbyte.lunchplanner.event.database.Event;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
import group.greenbyte.lunchplanner.event.database.EventTeamVisible;

import javax.persistence.*;
import javax.print.DocFlavor;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Team {

    public static final int MAX_TEAMNAME_LENGHT = 50;
    public static final int MAX_DESCRIPTION_LENGHT = 1000;
<<<<<<< HEAD

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int teamId;
=======
    static final public int MAX_SEARCHWORD_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer teamId;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

    @Column
    private boolean isPublic;

    @Column(length = MAX_TEAMNAME_LENGHT, nullable = false)
    private String teamName;

    @Column(length = MAX_DESCRIPTION_LENGHT)
    private String description;

    @OneToMany(mappedBy = "user")
    private Set<TeamMember> teamsMember;

<<<<<<< HEAD
    @OneToMany(mappedBy = "event")
    private Set<EventTeamVisible> eventsVisible;

    @ManyToOne
=======
    @ManyToOne(fetch = FetchType.EAGER)
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    @JoinColumn(name = "parentTeam")
    private Team parentTeam;

    @OneToMany(mappedBy = "parentTeam")
    private Set<Team> childTeams = new HashSet<>();

<<<<<<< HEAD
=======
    @OneToMany(mappedBy = "userInvited")
    private Set<TeamInvitation> usersInvited = new HashSet<>();

    @Transient
    private Set<TeamInvitationDataForReturn> invitations = new HashSet<>();

>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    public Team() {
        isPublic = false;
    }

<<<<<<< HEAD
    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
=======
    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
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

<<<<<<< HEAD
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

=======
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
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

<<<<<<< HEAD
    public Set<Team> getChildTeams() {
        return childTeams;
    }

    public void setChildTeams(Set<Team> childTeams) {
        this.childTeams = childTeams;
=======
    public Set<TeamInvitationDataForReturn> getInvitations() {
        return invitations;
    }

    public void setInvitations(Set<TeamInvitationDataForReturn> invitations) {
        this.invitations = invitations;
    }

    @Override
    public String toString() {
        return String.valueOf(getTeamId());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Team) {
            Team team = (Team) obj;
            return team.getTeamId().equals(getTeamId());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return getTeamId().hashCode();
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    }
}
