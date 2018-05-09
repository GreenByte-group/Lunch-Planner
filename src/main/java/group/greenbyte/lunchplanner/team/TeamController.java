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

            int teamId = teamlogic.createTeamWithParent(SessionManager.getUserName(), teamjson.getParent(), teamjson.getTeamName(), teamjson.getDescription());

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

    //TODO create team wihtout parent

    @Autowired
    public void setTeamLogic(TeamLogic teamlogic) {
        this.teamlogic = teamlogic;
    }
}
