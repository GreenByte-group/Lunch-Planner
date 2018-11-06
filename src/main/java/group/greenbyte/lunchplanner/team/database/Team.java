package group.greenbyte.lunchplanner.team.database;

import javax.persistence.*;
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

    @Column
    private String picture;

    @OneToMany(mappedBy = "user")
    private Set<TeamMember> teamsMember;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parentTeam")
    private Team parentTeam;

    @OneToMany(mappedBy = "parentTeam")
    private Set<Team> childTeams = new HashSet<>();

    @Transient
    private Set<TeamMemberDataForReturn> invitations = new HashSet<>();

    @Transient
    private Integer parentId;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public void setIsPublic(boolean aPublic) {
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

    public Integer getParentTeam() {
        return parentId;
    }

    public void setParentTeam(Integer parentTeam) {
        this.parentId = parentTeam;
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

    public Set<TeamMemberDataForReturn> getInvitations() {
        return invitations;
    }

    public void setInvitations(Set<TeamMemberDataForReturn> invitations) {
        this.invitations = invitations;
    }
}
