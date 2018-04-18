package group.greenbyte.lunchplanner.team;

import group.greenbyte.lunchplanner.AppConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
import static org.junit.Assert.*;
import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.Utils.getJsonFromObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
public class TeamControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        //mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    // ------------------ CREATE TEAM ------------------------

    @Test
    public void test1CreateTeamWithNoDescription() throws Exception {
        int parent = 1;

        TeamJson teamJson = new TeamJson(parent, "A", "");

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
    public void test2CreateTeamWithNormalDescriptionAndMaxTeamname() throws Exception {
        int parent = 1;

        TeamJson teamJson = new TeamJson(parent, createString(50), "Super Team");

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
    public void test3CreateTeamWithNormalDescriptionAndMaxTeamName() throws Exception {
        int parent = 1;

        TeamJson teamJson = new TeamJson(parent, createString(50), createString(1000));

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
    public void test4CreateTeamWithNoTeamName() throws Exception {
        int parent = 1;

        TeamJson teamJson = new TeamJson(parent, "", createString(1000));

        String json = getJsonFromObject(teamJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/team").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isNotExtended());

    }

    @Test
    public void test5CreateTeamTeamNameTooLong() throws Exception {
        int parent = 1;

        TeamJson teamJson = new TeamJson(parent,createString(51), createString(1000));

        String json = getJsonFromObject(teamJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/team").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void test6CreateTeamDescriptionTooLong() throws Exception {
        int parent = 1;

        TeamJson teamJson = new TeamJson(parent,createString(50), createString(1001));

        String json = getJsonFromObject(teamJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/team").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }



}