package group.greenbyte.lunchplanner.team;

import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.security.SessionManager;
import group.greenbyte.lunchplanner.team.database.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {

    private TeamLogic teamlogic;

    /**
     * Returns one team by his id
     *
     * @param teamId id of the team
     * @return the team
     */
    @RequestMapping(value = "/{teamId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTeam(@PathVariable("teamId") int teamId) {
        try {
            Team team = teamlogic.getTeam(SessionManager.getUserName(), teamId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(team);
        } catch (HttpRequestException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getErrorMessage());
        }
    }

    /**
     *
     * @param teamId id of the event
     */
    @RequestMapping(value = "/{teamId}/leave", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String leave(@PathVariable("teamId") int teamId, HttpServletResponse response){
        try {
            teamlogic.leave(SessionManager.getUserName(), teamId);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch(HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }
        return "";
    }

    /**
     * Get all teams that are visible for the user who created this request
     *
     * @return a list of all teams
     */
    @RequestMapping(value = "", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity getAllTeams() {

        try {
            List<Team> allTeams = teamlogic.getAllTeams(SessionManager.getUserName());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(allTeams);
        } catch (HttpRequestException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getErrorMessage());
        }
    }

    /**
     * only here for throwing an exception is no searchword is giving
     */
    @RequestMapping(value = "/search/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String searchTeamsNoSearchWord() {
        return "No searchword";
    }

    /**
     * Search teams that are visible for the user who created this request
     *
     * @param searchword what to search
     * @return all teams or an error message
     */
    @RequestMapping(value = "/search/{searchWord}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity searchTeams(@PathVariable("searchWord") String searchword){
        try{
            List<Team> searchingTeam = teamlogic.searchTeamsForUser(SessionManager.getUserName(), searchword);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(searchingTeam);
        } catch (HttpRequestException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getErrorMessage());
        }
    }

    /**
     * Create a Team with all the data given in TeamJson
     *
     * @param teamjson the object that describes the JSON object in java format
     * @return the id of the created team
     */
    @RequestMapping(value = "", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String createTeam(@RequestBody TeamJson teamjson, HttpServletResponse response) {
        try {
            int teamId;

            if(teamjson.getParent()!= null) {
                teamId = teamlogic.createTeamWithParent(SessionManager.getUserName(), teamjson.getParent(), teamjson.getTeamName(), teamjson.getDescription(), teamjson.isVisible());
            }else{
                teamId = teamlogic.createTeamWithoutParent(SessionManager.getUserName(),teamjson.getTeamName(), teamjson.getDescription(), teamjson.isVisible());
            }

            response.setStatus(HttpServletResponse.SC_CREATED);
            return String.valueOf(teamId);

        }catch(HttpRequestException e){

            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();

        }
    }

    /**
     * Invite a team meber
     *
     * @param userToInvite id of user to invite
     * @param teamId id of the team
     */
    @RequestMapping(value = "/{userToInvite}/invite/team/{teamId}", method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE )
    @ResponseBody
    public String inviteTeamMember(@PathVariable("userToInvite") String userToInvite, @PathVariable ("teamId") int teamId, HttpServletResponse response){
        try {
            teamlogic.inviteTeamMember(SessionManager.getUserName(), userToInvite, teamId);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }

        return "";
    }

    /**
     * Remove a team member from a team
     *
     * @param userToRemove user that is going to be removed
     * @param teamId id of the team
     * @param response response channel
     * @return
     */
    @RequestMapping(value = "/{userToRemove}/team/{teamId}/remove", method = RequestMethod.DELETE,
            produces = MediaType.TEXT_PLAIN_VALUE )
    @ResponseBody
    public String removeTeamMember(@PathVariable("userToRemove") String userToRemove, @PathVariable ("teamId") int teamId, HttpServletResponse response) {
        try {
            teamlogic.removeTeamMember(SessionManager.getUserName(), userToRemove, teamId);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }
        return "";
    }

    /**
     * Update the name for a team
     *
     * @return nothing or an error message
     */
    @RequestMapping(value = "/{teamId}/name", method = RequestMethod.POST,
            consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String updateName(@PathVariable("teamId") int teamId, @RequestBody String name, HttpServletResponse response) {
        try {
            teamlogic.updateName(SessionManager.getUserName(), teamId, name);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch(HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }

        return "";
    }

    /**
     * Update the name for a team
     *
     * @return nothing or an error message
     */
    @RequestMapping(value = "/{teamId}/description", method = RequestMethod.POST,
            consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String updateDescription(@PathVariable("teamId") int teamId, @RequestBody String description, HttpServletResponse response) {
        try {
            teamlogic.updateDescription(SessionManager.getUserName(), teamId, description);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch(HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }

        return "";
    }

    /**
     * Join a public team
     *
     * @param teamId id of the public team
     * @param response response channel
     * @return
     */
    @RequestMapping(value = "/{teamId}/join", method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public String joinPublicTeam(@PathVariable("teamId") int teamId, HttpServletResponse response) {
        try {
            teamlogic.joinPublicTeam(SessionManager.getUserName(), teamId);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch(HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }
        return "";
    }

    @Autowired
    public void setTeamLogic(TeamLogic teamlogic) {
        this.teamlogic = teamlogic;
    }
}
