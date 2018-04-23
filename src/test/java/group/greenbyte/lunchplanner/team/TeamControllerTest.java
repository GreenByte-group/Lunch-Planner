package group.greenbyte.lunchplanner.team;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.EventLogic;
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
import org.springframework.web.context.WebApplicationContext;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.Utils.getJsonFromObject;
import static group.greenbyte.lunchplanner.team.Utils.createTeamWithoutParent;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
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
    private int eventId;
    private int teamId;

    @Before
    public void setUp() throws Exception {

        createUserIfNotExists(userLogic, userName);

        teamId = createTeamWithoutParent(teamLogic, userName, createString(50), createString(50));

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

}