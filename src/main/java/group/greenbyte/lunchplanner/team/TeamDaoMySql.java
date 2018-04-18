package group.greenbyte.lunchplanner.team;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.team.database.TeamDatabaseConnector;
import group.greenbyte.lunchplanner.team.database.TeamInvitation;
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

    @Override
    public Team putUserTeamMember(String userToInvite, int teamId) throws DatabaseException {

        if(!isValidName(userToInvite))
            throw new DatabaseException();

        try{
            //User user = userDao.getUser(userToInvite);
            //Team team = getEventById(teamId);
            User user = new User();
            Team team = new Team();

            team.setTeamName("dummyEvent");
            user.setUserName(userToInvite);

            TeamInvitation teamInvitation = new TeamInvitation();
            teamInvitation.setUserInvited(user);
            teamInvitation.setTeamInvited(team);

            team.addUsersInvited(teamInvitation);

            return team;
            //return eventDatabaseConnector.save(event);

        } catch(Exception e) {
            throw new DatabaseException();
        }

    }

    private boolean isValidName(String name){
        if(name.length() <= User.MAX_USERNAME_LENGTH && name.length() > 0)
            return true;
        else
            return false;
    }

    @Autowired
    public void setTdc(TeamDatabaseConnector tdc) {
        this.tdc = tdc;
    }
}
