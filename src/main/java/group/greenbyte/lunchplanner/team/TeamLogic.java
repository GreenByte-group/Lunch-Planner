package group.greenbyte.lunchplanner.team;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.team.database.Team;
import group.greenbyte.lunchplanner.team.database.TeamMemberDataForReturn;
import group.greenbyte.lunchplanner.user.UserDao;
import group.greenbyte.lunchplanner.user.UserLogic;
import group.greenbyte.lunchplanner.user.database.User;
import group.greenbyte.lunchplanner.user.database.notifications.NotificationOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TeamLogic {

    private TeamDao teamdao;

    private UserLogic userLogic;

    private UserDao userDao;

    /**
     * @param userName    userName that is logged in
     * @param parent      parent of the new team
     * @param teamName    name of the new team
     * @param description description of the new location
     * @return the id of the new team
     * @throws HttpRequestException when teamName, userName, description not valid
     *                              or an Database error happens
     */
    int createTeamWithParent(String userName, int parent, String teamName, String description, String picture, boolean isPublic) throws HttpRequestException {
        checkParams(userName, teamName, description);
        System.out.println("logic: "+teamName+", "+picture);

        try {
            if (!hasViewPrivileges(userName, parent))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "No Privileges to acces parent team: " + parent);

            return teamdao.insertTeamWithParent(teamName, description, userName, picture, isPublic, parent);
        } catch (DatabaseException d) {
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), d.getMessage());
        }
    }

    /**
     * @param userName    userName that is logged in
     * @param teamName    name of the new team
     * @param description description of the new location
     * @return the id of the new team
     * @throws HttpRequestException when teamName, userName, description not valid
     *                              or an Database error happens
     */
    int createTeamWithoutParent(String userName, String teamName, String description,String picture, boolean isPublic) throws HttpRequestException {
        checkParams(userName, teamName, description);
        System.out.println("logic: "+teamName+", "+picture);

        try {
            return teamdao.insertTeam(teamName, description, userName, picture, isPublic);
        } catch (DatabaseException d) {
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), d.getMessage());
        }
    }

    private void checkParams(String userName, String teamName, String description) throws HttpRequestException {
        if (userName.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is empty");

        if (userName.length() > User.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username too long");

        if (teamName.length() == 0)
            throw new HttpRequestException(HttpStatus.NOT_EXTENDED.value(), "Teamname is empty");

        if (teamName.length() > Team.MAX_TEAMNAME_LENGHT)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Teamname too long");

        if (description.length() > Team.MAX_DESCRIPTION_LENGHT)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Description too long");
    }

    private boolean hasViewPrivileges(String userName, int teamId) throws DatabaseException {
        return teamdao.hasViewPrivileges(teamId, userName);
    }

    /**
     * Invite user to a team
     *
     * @param username     id of the user who creates the events
     * @param userToInvite id of the user who is invited
     * @param teamId       id of team
     * @return the Event of the invitation
     * @throws HttpRequestException when an unexpected error happens
     */
    public void inviteTeamMember(String username, String userToInvite, int teamId) throws HttpRequestException {

        if (!isValidName(username))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is not valid, maximun length" + User.MAX_USERNAME_LENGTH + ", minimum length 1");
        if (!isValidName(userToInvite))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username of invited user is not valid, maximun length" + User.MAX_USERNAME_LENGTH + ", minimum length 1");

        try {
            if (!hasAdminPrivileges(teamId, username))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You dont have write access to this team");

            if (username.equals(userToInvite))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You can't invite yourself to a team");

            teamdao.addUserToTeam(teamId, userToInvite);
        } catch (DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

        User user = userLogic.getUser(userToInvite);
        //set notification information
        String title = "Team invitation";
        String description = String.format("%s invited you to join their team", username);
        String linkToClick = "/team/" + teamId;

        saveAndSendNotification(userToInvite, user.getFcmToken(), username, title, description, linkToClick, "");
    }

    /**
     * Remove a team member from a team
     *
     * @param userName user that is going to be removed
     * @param teamId   id of the team
     * @throws DatabaseException
     */
    public void removeTeamMember(String userName, String userToRemove, int teamId) throws HttpRequestException {

        if (!isValidName(userName))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is not valid, maximun length" + User.MAX_USERNAME_LENGTH + ", minimum length 1");
        if (!isValidName(userToRemove))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username of removed user is not valid, maximun length" + User.MAX_USERNAME_LENGTH + ", minimum length 1");


        try {
            if (!hasAdminPrivileges(teamId, userName))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You dont have write access to this team");

            if (userName.compareTo(userToRemove) == 0)
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You cannot remove yourself from a team");

            teamdao.removeTeamMember(userToRemove, teamId);
        } catch (DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

        User user = userLogic.getUser(userToRemove);
        User teamAdmin = userLogic.getUser(userName);
        String profilePictureUrl = teamAdmin.getProfilePictureUrl();
        Team team = getTeam(userName, teamId);
        //set notification information
        String title = "You have been removed from a team";
        String description = String.format("%s removed you from team %s", userName, team.getTeamName());
        String linkToClick = "/team/" + teamId;

        saveAndSendNotification(userToRemove, user.getFcmToken(), userName, title, description, linkToClick, profilePictureUrl);
    }

    /**
     * @param userName the user who wants to access the team
     * @param teamId   id of the team
     * @return Team which matched with the given id or null
     */
    public Team getTeam(String userName, int teamId) throws HttpRequestException {
        try {
            Team team = teamdao.getTeam(teamId);

            if (team == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Team with team-id: " + teamId + "was not found");
            else {
                if (!hasViewPrivileges(teamId, userName)) //TODO write test for next line
                    throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You dont have rights to access this team");

                return team;
            }
        } catch (DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    /**
     * @param username userName that is logged in
     * @return List<Team> List with generic typ of Team which includes all teams matching with the searchword
     */
    public List<Team> getAllTeams(String username) throws HttpRequestException {
        if (username.length() > User.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is too long, maximum length: " + User.MAX_USERNAME_LENGTH);
        if (username.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is empty");

        return this.searchTeamsForUser(username, "");
    }

    public String setTeamPicture(int teamId, String picture) throws HttpRequestException{
        if(teamId < 0){
            throw new HttpRequestException(HttpStatus.NOT_ACCEPTABLE.value(), "teamId is null");
        }

        try{
            teamdao.setTeamPicture(teamId, picture);
            return "";
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage());
        }
    }


    /**
     * @param userName
     * @param searchword
     * @return
     * @throws HttpRequestException
     */
    public List<Team> searchTeamsForUser(String userName, String searchword) throws HttpRequestException {

        if (searchword == null || searchword.length() > Team.MAX_SEARCHWORD_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Searchword is too long or null ");
        if (userName == null || userName.length() == 0 || userName.length() > User.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is too long, empty or null ");

        try {

            Set<Team> searchResults = new HashSet<>(teamdao.findPublicTeams(searchword));

            List<Team> temp = teamdao.findTeamsUserInvited(userName, searchword);
            for (Team team : temp) {
                if (!searchResults.contains(team))
                    searchResults.add(team);
            }

            return new ArrayList<>(searchResults);

        } catch (DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    /**
     * Update the name of a team
     *
     * @param userName who wants to change the name
     * @param teamId   team to change
     * @param name     name to change
     */
    public void updateName(String userName, int teamId, String name) throws HttpRequestException {
        try {
            if (name == null || name.length() > Team.MAX_TEAMNAME_LENGHT || name.length() == 0)
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is too long or empty ");

            if (!hasAdminPrivileges(teamId, userName)) {
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "User: " + userName + " is no admin of this team");
            }

            teamdao.updateName(teamId, name);
        } catch (DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    /**
     * Update the description of a team
     *
     * @param userName    who wants to change the description
     * @param teamId      team to change
     * @param description description to change
     */
    public void updateDescription(String userName, int teamId, String description) throws HttpRequestException {
        try {
            if (description == null || description.length() > Team.MAX_DESCRIPTION_LENGHT || description.length() == 0)
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Description is too long or empty ");

            if (!hasAdminPrivileges(teamId, userName)) {
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "User: " + userName + " is no admin of this team");
            }

            teamdao.updateDescription(teamId, description);
        } catch (DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    /**
     * Checks if a user has privileges to change the team object
     *
     * @param teamId   id of the team to check
     * @param userName who wants to change
     * @return true if the user has permission, false if not
     */
    private boolean hasAdminPrivileges(int teamId, String userName) throws DatabaseException {
        return teamdao.hasAdminPrivileges(teamId, userName);
    }

    /**
     * Checks if a user has privileges to view the team object
     *
     * @param teamId   id of the team to check
     * @param userName who wants to change
     * @return true if the user has permission, false if not
     */
    private boolean hasViewPrivileges(int teamId, String userName) throws DatabaseException {
        return teamdao.hasViewPrivileges(teamId, userName);
    }

    public void leave(String userName, int teamId) throws HttpRequestException {

        if (!isValidName(userName))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is not valid, maximum length: " + User.MAX_USERNAME_LENGTH + ", minimum length 1");
        try {

            if (teamdao.getTeam(teamId) == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Team with team-id: " + teamId + ", was not found");

            User user = userLogic.getUser(userName);
            Team team = getTeam(userName, teamId);
            //set notification information
            String profilePictureUrl = user.getProfilePictureUrl();
            String title = "Team member left";
            String description = String.format("%s left your team %s", userName, team.getTeamName());
            String linkToClick = "/team/" + teamId;

            boolean isAdmin = hasAdminPrivileges(teamId, userName);
            teamdao.leave(userName, teamId);
            List<TeamMemberDataForReturn> members = teamdao.getInvitations(teamId);
            if (isAdmin) {
                //if there is only one member left than the team gets automatically deleted
                if (members.size() == 0) {
                    //cannot delete the team before someone leaves (dependencies)
                    teamdao.deleteTeam(teamId);
                } else {
                    //choose new admin
                    TeamMemberDataForReturn newAdmin = members.get(0);
                    teamdao.changeUserToAdmin(teamId, newAdmin.getUserName());
                    String newTitle = "You got promoted";
                    String newDescription = String.format("%s left your team %s and you have been chosen to be an admin." +
                            " You can change the description or the name of your team and remove members.", userName, team.getTeamName());

                    saveAndSendNotification(newAdmin.getUserName(), userLogic.getUser(newAdmin.getUserName()).getFcmToken(),
                            userName, newTitle, newDescription, linkToClick, profilePictureUrl);
                }


            }

            for(TeamMemberDataForReturn m : members) {
                saveAndSendNotification(m.getUserName(), userLogic.getUser(m.getUserName()).getFcmToken(),
                        userName, title, description, linkToClick, profilePictureUrl);
            }

        } catch (DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    /**
     * Join a public team
     *
     * @param userName user that wants to join the team
     * @param teamId   id of the team
     * @throws HttpRequestException
     */
    public void joinPublicTeam(String userName, int teamId) throws HttpRequestException {
        if (!isValidName(userName))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is not valid, maximum length: " + User.MAX_USERNAME_LENGTH + ", minimum length 1");

        try {
            Team team = teamdao.getTeam(teamId);
            if (team == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with event-id: " + teamId + ", was not found");

            if (!team.isPublic())
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "Team is not public!");

            //set notification information
            User user = userLogic.getUser(userName);
            String profilePictureUrl = user.getProfilePictureUrl();
            String title = "New team member";
            String description = String.format("%s joined your team %s", userName, team.getTeamName());
            String linkToClick = "/team/" + teamId;
            String receiverName;
            User receiverData;
            List<TeamMemberDataForReturn> members = teamdao.getInvitations(teamId);

            for (TeamMemberDataForReturn m : members) {
                receiverName = m.getUserName();
                receiverData = userLogic.getUser(receiverName);

                saveAndSendNotification(receiverName, receiverData.getFcmToken(), userName, title,
                        description, linkToClick, profilePictureUrl);
            }
            teamdao.addUserToTeam(teamId, userName);
        } catch (DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    private void saveAndSendNotification(String receiver, String receiverFcmToken, String builder, String title,
                                         String description, String linkToClick, String picturePath) throws HttpRequestException {

        //save notification
        userLogic.saveNotification(receiver, title, description, builder, linkToClick, picturePath);

        //send a notification to userToInvite
        NotificationOptions notificationOptions = userLogic.getNotificationOptions(receiver);
        if (notificationOptions == null || (notificationOptions.notificationsAllowed() && !notificationOptions.isTeamsBlocked())) {
            try {
                userLogic.sendNotification(receiverFcmToken, receiver, title, description, linkToClick, picturePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteUser(String username) throws DatabaseException{
        try{
            teamdao.deleteUser(username);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Count admins in a team
     *
     * @param members
     * @return the amount of admins that a team has
     */
    private int amountOfAdmins(List<TeamMemberDataForReturn> members) {
        int countAdmins = 0;

        for (TeamMemberDataForReturn member : members) {
            if (member.isAdmin())
                countAdmins++;
        }
        return countAdmins;
    }

    private boolean isValidName(String name) {
        return name.length() <= User.MAX_USERNAME_LENGTH && name.length() > 0;
    }


    @Autowired
    public void setTeamDao(TeamDao teamdao) {
        this.teamdao = teamdao;
    }

    @Autowired
    public void setUserLogic(UserLogic userLogic) {
        this.userLogic = userLogic;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
