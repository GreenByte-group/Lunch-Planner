package group.greenbyte.lunchplanner.team.database;

public class TeamDatabase {

    private int teamId;

    private boolean isPublic;

    private String teamName;

    private String description;

    private Integer parentTeam ;

    /**
     * @return team with all data from the entity and no relations
     */
    public Team getTeam() {
        Team team = new Team();
        team.setTeamName(teamName);
        team.setDescription(description);
        team.setIsPublic(isPublic);
        team.setTeamId(teamId);

        return team;
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
        return parentTeam;
    }

    public void setParentTeam(Integer parentTeam) {
        this.parentTeam = parentTeam;
    }

}
