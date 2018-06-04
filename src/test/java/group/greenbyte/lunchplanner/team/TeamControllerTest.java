package group.greenbyte.lunchplanner.team;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.EventLogic;
import group.greenbyte.lunchplanner.team.database.Team;
import group.greenbyte.lunchplanner.user.UserLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.Utils.getJsonFromObject;
import static group.greenbyte.lunchplanner.team.Utils.createTeamWithoutParent;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
@Transactional
public class TeamControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private EventLogic eventLogic;

    @Autowired
    private UserLogic userLogic;

    @Autowired
    private TeamLogic teamLogic;

    private final String userName = "banane";
    private int locationId;


    private int teamId;
    private String description;
    private String teamName;

    @Before
    public void setUp() throws Exception {
        teamName = createString(20);
        description = createString(50);

        createUserIfNotExists(userLogic, userName);

        teamId = createTeamWithoutParent(teamLogic, userName, teamName, description);

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        //mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    // ------------------ CREATE TEAM ------------------------

    @Test
    @WithMockUser(username = userName)
    public void test1CreateTeamWithNoDescription() throws Exception {
        TeamJson teamJson = new TeamJson(teamId, "A", "");

        String json = getJsonFromObject(teamJson);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/team").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
                .andReturn();

        String response = result.getResponse().getContentAsString();

        try {
            Integer.valueOf(response);
        }catch (NumberFormatException e) {
            Assert.fail("Result is not a number");
        }

    }

    @Test
    @WithMockUser(username = userName)
    public void test2CreateTeamWithNormalDescriptionAndMaxTeamname() throws Exception {
        TeamJson teamJson = new TeamJson(teamId, createString(50), "Super Team");

        String json = getJsonFromObject(teamJson);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/team").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
                .andReturn();

        String response = result.getResponse().getContentAsString();

        try {
            Integer.valueOf(response);
        }catch (NumberFormatException e) {
            Assert.fail("Result is not a number");
        }

    }

    @Test
    @WithMockUser(username = userName)
    public void test3CreateTeamWithNormalDescriptionAndMaxTeamName() throws Exception {
        TeamJson teamJson = new TeamJson(teamId, createString(50), createString(1000));

        String json = getJsonFromObject(teamJson);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/team").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
                .andReturn();

        String response = result.getResponse().getContentAsString();

        try {
            Integer.valueOf(response);
        }catch (NumberFormatException e) {
            Assert.fail("Result is not a number");
        }

    }

    @Test
    @WithMockUser(username = userName)
    public void test4CreateTeamWithNoTeamName() throws Exception {
        TeamJson teamJson = new TeamJson(teamId, "", createString(1000));

        String json = getJsonFromObject(teamJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/team").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isNotExtended());

    }

    @Test
    @WithMockUser(username = userName)
    public void test5CreateTeamTeamNameTooLong() throws Exception {
        TeamJson teamJson = new TeamJson(teamId,createString(51), createString(1000));

        String json = getJsonFromObject(teamJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/team").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @WithMockUser(username = userName)
    public void test6CreateTeamDescriptionTooLong() throws Exception {
        TeamJson teamJson = new TeamJson(teamId,createString(50), createString(1001));

        String json = getJsonFromObject(teamJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/team").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    // ------------------- UPDATE TEAM --------------------------

    @Test
    @WithMockUser(username = userName)
    public void test1UpdateTeamName() throws Exception {
        String newName = "new Name";

        mockMvc.perform(
                MockMvcRequestBuilders.put("/team/" + teamId + "/name").contentType(MediaType.TEXT_PLAIN_VALUE).content(newName))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Team team = teamLogic.getTeam(userName, teamId);
        assertEquals(newName, team.getTeamName());
    }

    @Test
    @WithMockUser(username = "otherUser")
    public void test2UpdateTeamNameUnauthorized() throws Exception {
        String newName = "new Name";

        mockMvc.perform(
                MockMvcRequestBuilders.put("/team/" + teamId + "/name").contentType(MediaType.TEXT_PLAIN_VALUE).content(newName))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = userName)
    public void test3UpdateTeamNameTooLong() throws Exception {
        String newName = createString(Team.MAX_TEAMNAME_LENGHT + 1);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/team/" + teamId + "/name").contentType(MediaType.TEXT_PLAIN_VALUE).content(newName))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test1UpdateTeamDescription() throws Exception {
        String newDescription = "new Description";

        mockMvc.perform(
                MockMvcRequestBuilders.put("/team/" + teamId + "/description").contentType(MediaType.TEXT_PLAIN_VALUE).content(newDescription))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Team team = teamLogic.getTeam(userName, teamId);
        assertEquals(newDescription, team.getDescription());
    }

    @Test
    @WithMockUser(username = "otherUser")
    public void test2UpdateTeamNameDescription() throws Exception {
        String newDescription = "new Description";

        mockMvc.perform(
                MockMvcRequestBuilders.put("/team/" + teamId + "/description").contentType(MediaType.TEXT_PLAIN_VALUE).content(newDescription))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = userName)
    public void test3UpdateTeamDescriptionTooLong() throws Exception {
        String newDescription = createString(Team.MAX_DESCRIPTION_LENGHT + 1);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/team/" + teamId + "/description").contentType(MediaType.TEXT_PLAIN_VALUE).content(newDescription))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // ------------------ INVITE TEAM MEMBER ------------------------

    @Test
    @WithMockUser(username = userName)
    public void test1inviteTeamMember() throws Exception {
        String newUserName = createUserIfNotExists(userLogic, createString(10));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/team/" + newUserName + "/invite/team/" + teamId))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(username = userName)
    public void test2InviteTeamMemberMaxUser() throws Exception {

        String userName = createUserIfNotExists(userLogic, createString(50));

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/team/" + userName + "/invite/team/" + teamId))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
                .andReturn();

        String response = result.getResponse().getContentAsString();
    }

    @Test
    @WithMockUser(username = userName)
    public void test3InviteTeamMemberInvalidName() throws Exception {

        String userName = createUserIfNotExists(userLogic, createString(51));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/team/" + userName + "/invite/team/" + teamId))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE));
    }

    @Test (expected = AssertionError.class)
    @WithMockUser(username = userName)
    public void test4InviteTeamMemberEmptyName() throws Exception {
        String userName = createUserIfNotExists(userLogic, createString(1));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/team/" + userName + "/invite/team/" + teamId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE));

    }

    // ------------------ GET TEAM -------------------------

    @Test
    @WithMockUser(username = userName)
    public void test1GetTeamValid() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/team/" + teamId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.teamId").value(teamId))
                .andExpect(jsonPath("$.teamName").value(teamName))
                .andExpect(jsonPath("$.description").value(description));

    }

    @Test
    @WithMockUser(username = userName)
    public void test2GetTeamInValid() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/team/" + teamId + 100))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "otherUser")
    public void test3GetTeamInvalidUser() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/team/" + teamId))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    // ------------------ GET ALL TEAMS -------------------------

    @Test
    @WithMockUser(username = userName)
    public void test1GetAllTeams() throws Exception {
        createTeamWithoutParent(teamLogic, userName, teamName, description);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/team"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = userName)
    public void test2SearchTeamsForUserSearchwordToBig() throws Exception {
        String searchword = createString(51);
        String json = getJsonFromObject(searchword);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/team").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // ------------------ SEARCH TEAM -------------------------
    @Test
    @WithMockUser(username = userName)
    public void test1SearchTeams() throws Exception {
        String searchWord = teamName;

        mockMvc.perform(
                MockMvcRequestBuilders.get("/team/search/" + searchWord))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].teamId").value(teamId))
                .andExpect(jsonPath("$[0].teamName").value(teamName))
                .andExpect(jsonPath("$[0].description").value(description));
    }

    @Test
    @WithMockUser(username = userName)
    public void test1SearchTeamsTooLongSearchWord() throws Exception {
        String searchWord = createString(51);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/team/search/" + searchWord))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test1SearchTeamsNoSearchWord() throws Exception {
        String searchWord = "";

        mockMvc.perform(
                MockMvcRequestBuilders.get("/team/search/" + searchWord))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // ------------------ REMOVE MEMBER -------------------------

    @Test
    @WithMockUser(username = userName)
    public void test1RemoveTeamMember() throws Exception {
        String userName = createUserIfNotExists(userLogic, createString(1));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/team/" + userName + "/remove/team/" + teamId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(username = userName)
    public void test2RemoveTeamMemberMaxUserName() throws Exception {

        String userName = createUserIfNotExists(userLogic, createString(50));

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/team/" + userName + "/remove/team/" + teamId))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
                .andReturn();

        String response = result.getResponse().getContentAsString();
    }

    @Test
    @WithMockUser(username = userName)
    public void test3RemoveTeamMemberUserNameTooLong() throws Exception {

        String userName = createUserIfNotExists(userLogic, createString(51));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/team/" + userName + "/invite/team/" + teamId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE));
    }

    @Test
    @WithMockUser(username = userName)
    public void test4RemoveTeamMemberEmptyUserName() throws Exception {
        String userName = "";

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/team/" + userName + "/remove/team/" + teamId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE));

    }
}