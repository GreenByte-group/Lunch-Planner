package group.greenbyte.lunchplanner.team;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.team.database.Team;
import group.greenbyte.lunchplanner.user.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TeamLogic {

    private TeamDao teamdao;
    /**
     *
     * @param userName userName that is logged in
     * @param parent parent of the new team
     * @param teamName name of the new team
     * @param description description of the new location
     * @return the id of the new team
     * @throws HttpRequestException when teamName, userName, description not valid
     * or an Database error happens
     */

    int createTeam(String userName, int parent, String teamName, String description) throws HttpRequestException {

        if(userName.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is empty");

        if(userName.length() > User.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username too long");

        if(teamName.length() == 0)
            throw new HttpRequestException(HttpStatus.NOT_EXTENDED.value(), "Teamname is empty");

        if(teamName.length() > Team.MAX_TEAMNAME_LENGHT)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Teamname too long");

        if(description.length() > Team.MAX_DESCRIPTION_LENGHT)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Description too long");

        /*if(hasRootPrivileges(userName, teamdao.getTeam(parent)))
            throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "No Privileges");*/

        /*if(canCreateTeam(teamdao.getTeam(parent), teamName))
            throw new HttpRequestException(HttpStatus.CONFLICT.value(), "Team already exists");*/

        try {
            return teamdao.insertTeam(teamName, description, userName, parent);
        } catch(DatabaseException d){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), d.getMessage());
        }

    }
    //TODO check privileges

    /*private boolean hasViewPrivileges(String userName, Team team){
        return false;
    }*/

    /*private boolean hasRootPrivileges(String userName, Team team){
        return false;
    }*/

    /*private canCreateTeam(Team parent, String teamName) {
        return false;
    }*/



    @Autowired
    public void setTeamDao(TeamDao teamdao) {
        this.teamdao = teamdao;
    }
}
