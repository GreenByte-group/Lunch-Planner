package group.greenbyte.lunchplanner.team;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.Utils.getJsonFromObject;

public class Utils {


    /**
     * Create an team
     * @return the id of the team
     */
    public static int createTeam (MockMvc mockMvc) throws Exception {

        TeamJson team = new TeamJson(0,createString(50), createString(1000));

        String json = getJsonFromObject(team);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/team").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();

        String response = result.getResponse().getContentAsString();

        return Integer.valueOf(response);
    }

    public static int createTeam(TeamLogic teamLogic, String userName, int parent) throws Exception {

        return teamLogic.createTeam(userName, parent, createString(10),createString(100));
    }

}
