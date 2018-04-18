package group.greenbyte.lunchplanner.team;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.team.database.Team;
import group.greenbyte.lunchplanner.user.UserLogic;
import group.greenbyte.lunchplanner.user.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TeamLogic {

    private TeamDao teamdao;

    private UserLogic userLogic;
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

    /**
     * Invite user to a team
     *
     * @param username id of the user who creates the events
     * @param userToInvite id of the user who is invited
     * @param teamId id of team
     * @return the Event of the invitation
     *
     * @throws HttpRequestException when an unexpected error happens
     *
     */
    public void inviteTeamMember(String username, String userToInvite, int teamId) throws HttpRequestException{

        if(!isValidName(username))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is not valid, maximun length" + User.MAX_USERNAME_LENGTH + ", minimum length 1");
        if(!isValidName(userToInvite))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username of invited user is not valid, maximun length" + User.MAX_USERNAME_LENGTH + ", minimum length 1");

        try{
            teamdao.putUserTeamMember(userToInvite, teamId);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

        userLogic.sendInvitation(username, userToInvite);
    }

    private boolean isValidName(String name){
        if(name.length() <= User.MAX_USERNAME_LENGTH && name.length() > 0){
            System.out.println("isValid");
            return true;
        }

        else
            return false;
    }



    @Autowired
    public void setTeamDao(TeamDao teamdao) {
        this.teamdao = teamdao;
    }

    @Autowired
    public void setUserLogic(UserLogic userLogic) {
        this.userLogic = userLogic;
    }
}
