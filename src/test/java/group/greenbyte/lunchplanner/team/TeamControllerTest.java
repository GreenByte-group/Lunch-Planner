package group.greenbyte.lunchplanner.team;

import group.greenbyte.lunchplanner.AppConfig;
<<<<<<< HEAD
=======
import group.greenbyte.lunchplanner.event.EventLogic;
import group.greenbyte.lunchplanner.user.UserLogic;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
<<<<<<< HEAD
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
<<<<<<< HEAD
import org.springframework.web.context.WebApplicationContext;
import static org.junit.Assert.*;
import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.Utils.getJsonFromObject;
=======
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.Utils.getJsonFromObject;
import static group.greenbyte.lunchplanner.team.Utils.createTeamWithoutParent;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
<<<<<<< HEAD
=======
@Transactional
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
public class TeamControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

<<<<<<< HEAD
    @Before
    public void setUp() throws Exception {
=======
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

>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        //mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    // ------------------ CREATE TEAM ------------------------

    @Test
<<<<<<< HEAD
    public void test1CreateTeamWithNoDescription() throws Exception {
        int parent = 1;

        TeamJson teamJson = new TeamJson(parent, "A", "");
=======
    @WithMockUser(username = userName)
    public void test1CreateTeamWithNoDescription() throws Exception {
        TeamJson teamJson = new TeamJson(teamId, "A", "");
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

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
<<<<<<< HEAD
    public void test2CreateTeamWithNormalDescriptionAndMaxTeamname() throws Exception {
        int parent = 1;

        TeamJson teamJson = new TeamJson(parent, createString(50), "Super Team");
=======
    @WithMockUser(username = userName)
    public void test2CreateTeamWithNormalDescriptionAndMaxTeamname() throws Exception {
        TeamJson teamJson = new TeamJson(teamId, createString(50), "Super Team");
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

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
<<<<<<< HEAD
    public void test3CreateTeamWithNormalDescriptionAndMaxTeamName() throws Exception {
        int parent = 1;

        TeamJson teamJson = new TeamJson(parent, createString(50), createString(1000));
=======
    @WithMockUser(username = userName)
    public void test3CreateTeamWithNormalDescriptionAndMaxTeamName() throws Exception {
        TeamJson teamJson = new TeamJson(teamId, createString(50), createString(1000));
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

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
<<<<<<< HEAD
    public void test4CreateTeamWithNoTeamName() throws Exception {
        int parent = 1;

        TeamJson teamJson = new TeamJson(parent, "", createString(1000));
=======
    @WithMockUser(username = userName)
    public void test4CreateTeamWithNoTeamName() throws Exception {
        TeamJson teamJson = new TeamJson(teamId, "", createString(1000));
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

        String json = getJsonFromObject(teamJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/team").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isNotExtended());

    }

    @Test
<<<<<<< HEAD
    public void test5CreateTeamTeamNameTooLong() throws Exception {
        int parent = 1;

        TeamJson teamJson = new TeamJson(parent,createString(51), createString(1000));
=======
    @WithMockUser(username = userName)
    public void test5CreateTeamTeamNameTooLong() throws Exception {
        TeamJson teamJson = new TeamJson(teamId,createString(51), createString(1000));
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

        String json = getJsonFromObject(teamJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/team").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
<<<<<<< HEAD
    public void test6CreateTeamDescriptionTooLong() throws Exception {
        int parent = 1;

        TeamJson teamJson = new TeamJson(parent,createString(50), createString(1001));
=======
    @WithMockUser(username = userName)
    public void test6CreateTeamDescriptionTooLong() throws Exception {
        TeamJson teamJson = new TeamJson(teamId,createString(50), createString(1001));
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

        String json = getJsonFromObject(teamJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/team").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

<<<<<<< HEAD


=======
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
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
}