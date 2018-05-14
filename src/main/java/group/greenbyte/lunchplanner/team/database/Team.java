package group.greenbyte.lunchplanner.team.database;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.event.database.EventTeamVisible;

import javax.persistence.*;
import javax.print.DocFlavor;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Team {

    public static final int MAX_TEAMNAME_LENGHT = 50;
    public static final int MAX_DESCRIPTION_LENGHT = 1000;
    static final public int MAX_SEARCHWORD_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer teamId;

    @Column
    private boolean isPublic;

    @Column(length = MAX_TEAMNAME_LENGHT, nullable = false)
    private String teamName;

    @Column(length = MAX_DESCRIPTION_LENGHT)
    private String description;

    @OneToMany(mappedBy = "user")
    private Set<TeamMember> teamsMember;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parentTeam")
    private Team parentTeam;

    @OneToMany(mappedBy = "parentTeam")
    private Set<Team> childTeams = new HashSet<>();

    @OneToMany(mappedBy = "userInvited")
    private Set<TeamInvitation> usersInvited = new HashSet<>();

    @Transient
    private Set<TeamInvitationDataForReturn> invitations = new HashSet<>();

    public Team() {
        isPublic = false;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
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
    }
}
