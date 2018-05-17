package group.greenbyte.lunchplanner.team;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.team.database.Team;
<<<<<<< HEAD
=======
import group.greenbyte.lunchplanner.user.UserLogic;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
import group.greenbyte.lunchplanner.user.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
=======
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
@Service
public class TeamLogic {

    private TeamDao teamdao;
<<<<<<< HEAD
=======

    private UserLogic userLogic;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
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
<<<<<<< HEAD

    int createTeam(String userName, int parent, String teamName, String description) throws HttpRequestException {

=======
    int createTeamWithParent(String userName, int parent, String teamName, String description) throws HttpRequestException {
        checkParams(userName, teamName, description);

        try {
            if(!hasViewPrivileges(userName, parent))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "No Privileges to acces parent team: " + parent);

            return teamdao.insertTeamWithParent(teamName, description, userName, parent);
        } catch(DatabaseException d){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), d.getMessage());
        }
    }

    /**
     *
     * @param userName userName that is logged in
     * @param teamName name of the new team
     * @param description description of the new location
     * @return the id of the new team
     * @throws HttpRequestException when teamName, userName, description not valid
     * or an Database error happens
     */
    int createTeamWithoutParent(String userName, String teamName, String description) throws HttpRequestException {
        checkParams(userName, teamName, description);

        try {
            return teamdao.insertTeam(teamName, description, userName);
        } catch(DatabaseException d){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), d.getMessage());
        }
    }

    private void checkParams(String userName, String teamName, String description) throws HttpRequestException {
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
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
<<<<<<< HEAD

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
=======
    }

    private boolean hasViewPrivileges(String userName, int teamId) throws DatabaseException {
        return teamdao.hasViewPrivileges(teamId, userName);
    }

    private boolean hasRootPrivileges(String userName, int teamId) throws DatabaseException {
        return teamdao.hasAdminPrivileges(teamId, userName);
    }

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
            if(!hasRootPrivileges(username, teamId))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You dont have write access to this team");

            teamdao.addUserToTeam(teamId, userToInvite);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

        userLogic.sendInvitation(username, userToInvite);
    }

    /**
     *
     * @param userName the user who wants to access the team
     * @param teamId  id of the team
     * @return Team which matched with the given id or null
     */
    public Team getTeam(String userName, int teamId)throws HttpRequestException{
        try{
            Team team = teamdao.getTeam(teamId);

            if(team == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Team with team-id: " + teamId + "was not found");
            else {
                if(!hasViewPrivileges(teamId, userName)) //TODO write test for next line
                    throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You dont have rights to access this team");

                return team;
            }
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    /**
     *
     * @param username  userName that is logged in
     * @return List<Team> List with generic typ of Team which includes all teams matching with the searchword
     *
     */
    public List<Team> getAllTeams(String username) throws HttpRequestException{
        if(username.length() > User.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is too long, maximum length: " + User.MAX_USERNAME_LENGTH);
        if(username.length() == 0 )
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is empty");

        return this.searchTeamsForUser(username, "");
    }


    /**
     *
     * @param userName
     * @param searchword
     * @return
     * @throws HttpRequestException
     */
    public List<Team> searchTeamsForUser(String userName, String searchword) throws HttpRequestException{

        if(searchword == null || searchword.length() > Team.MAX_SEARCHWORD_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Searchword is too long or null ");
        if(userName == null || userName.length()== 0 || userName.length() > User.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is too long, empty or null ");

        try{

            Set<Team> searchResults = new HashSet<>(teamdao.findPublicTeams(searchword));

            List<Team> temp = teamdao.findTeamsUserInvited(userName, searchword);
            for(Team team : temp) {
                if(!searchResults.contains(team))
                    searchResults.add(team);
            }

            return new ArrayList<>(searchResults);

        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    /**
     * Checks if a user has privileges to change the team object
     *
     * @param teamId id of the team to check
     * @param userName who wants to change
     * @return true if the user has permission, false if not
     */
    private boolean hasAdminPrivileges(int teamId, String userName) throws DatabaseException {
        return teamdao.hasAdminPrivileges(teamId, userName);
    }

    /**
     * Checks if a user has privileges to view the team object
     *
     * @param teamId id of the team to check
     * @param userName who wants to change
     * @return true if the user has permission, false if not
     */
    private boolean hasViewPrivileges(int teamId, String userName) throws DatabaseException {
        return teamdao.hasViewPrivileges(teamId, userName);
    }

    private boolean isValidName(String name){
        return name.length() <= User.MAX_USERNAME_LENGTH && name.length() > 0;
    }
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56



    @Autowired
    public void setTeamDao(TeamDao teamdao) {
        this.teamdao = teamdao;
    }
<<<<<<< HEAD
=======

    @Autowired
    public void setUserLogic(UserLogic userLogic) {
        this.userLogic = userLogic;
    }
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
}
