package group.greenbyte.lunchplanner.team;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.team.database.TeamDatabaseConnector;
import group.greenbyte.lunchplanner.team.database.TeamMember;
import group.greenbyte.lunchplanner.user.UserDao;
import group.greenbyte.lunchplanner.user.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.team.database.Team;

@Repository
public class TeamDaoMySql implements TeamDao {

    private final UserDao userDao;

    private TeamDatabaseConnector tdc;

    @Autowired
    public TeamDaoMySql(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public int insertTeam(String teamName, String description, String adminName, int parent) throws DatabaseException {

        if(teamName == null || description == null || adminName == null ||
                teamName.length() == 0 || adminName.length() == 0 || adminName.length() > User.MAX_USERNAME_LENGTH)
            throw new DatabaseException();

        Team team = new Team();
        team.setTeamName(teamName);
        team.setDescription(description);
        team.setParentTeam(getTeam(parent));

        TeamMember teamMember = new TeamMember();
        teamMember.setUser(userDao.getUser(adminName));
        teamMember.setAdmin(true);

        team.addTeamsMember(teamMember);

        try {
            return tdc.save(team).getTeamId();
        } catch(Exception e){
            throw new DatabaseException();
        }

    }

    @Override
    public Team getTeam(int teamId) throws DatabaseException {
        return null;
    }

    @Autowired
    public void setTdc(TeamDatabaseConnector tdc) {
        this.tdc = tdc;
    }
}
