package group.greenbyte.lunchplanner.team;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.team.database.Team;

public interface TeamDao {

    /**
     *
     * @param teamName name of the team
     * @param description description of the team
     * @param adminName creator of the team
     * @param parent parent of the team
     * @return returns the teamId created by the database
     * @throws DatabaseException
     */
    int insertTeam(String teamName, String description, String adminName, int parent) throws DatabaseException;


    /**
     *
     * @param teamId id of the team
     * @return returns a team object
     * @throws DatabaseException
     */
    Team getTeam(int teamId) throws DatabaseException;

}
