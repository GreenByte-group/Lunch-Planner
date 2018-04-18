package group.greenbyte.lunchplanner.team;

import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/team")
public class TeamController {

    private TeamLogic teamlogic;
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

            int teamId = teamlogic.createTeam("dummy", teamjson.getParent(), teamjson.getTeamName(), teamjson.getDescription());

            response.setStatus(HttpServletResponse.SC_CREATED);
            return String.valueOf(teamId);

        }catch(HttpRequestException e){

            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();

        }
    }

    @Autowired
    public void setTeamLogic(TeamLogic teamlogic) {
        this.teamlogic = teamlogic;
    }
}
