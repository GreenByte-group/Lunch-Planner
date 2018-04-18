package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.EventController;
import group.greenbyte.lunchplanner.event.EventJson;
import group.greenbyte.lunchplanner.event.EventLogic;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.location.LocationLogic;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
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


import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.Utils.getJsonFromObject;
import static group.greenbyte.lunchplanner.event.Utils.createEvent;
import static group.greenbyte.lunchplanner.location.Utils.createLocation;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private EventLogic eventLogic;

    @Autowired
    private UserLogic userLogic;

    @Autowired
    private LocationLogic locationLogic;

    private String userName;
    private int locationId;
    private int eventId;

    @Before
    public void setUp() throws Exception {
        userName = createUserIfNotExists(userLogic, "dummy");
        locationId = createLocation(locationLogic, userName, "Test location", "test description");
        eventId = createEvent(eventLogic, userName, locationId);

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        //mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    // ------------------ CREATE USER ------------------------
    @Test
    public void test1CreateUserValidParam() throws Exception{
        String userName = createString(50);
        String mail = "teeeaefst@yuyhinoal.dalk";
        String password = createString(80);

        UserJson userJson = new UserJson(userName, password, mail);

        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void test2CreateUserEmptyUsername() throws Exception{
        String userName = "";
        String mail = "teasdfast@yuyhinoal.dalk";
        String password = createString(80);

        UserJson userJson = new UserJson(userName, password, mail);
        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test3CreateUserTooLongUserName() throws Exception{
        String userName = createString(51);
        String mail = "teasdfst@yuyhinoal.dalk";
        String password = createString(80);

        UserJson userJson = new UserJson(userName, password, mail);

        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
  
    @Test
    public void test4CreateUserEmptyPassword() throws Exception{
        String userName = createString(50);
        String mail = "tesdaft@yuyhinoal.dalk";
        String password = "";

        UserJson userJson = new UserJson(userName, password, mail);

        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test5CreateUserTooLongPassword() throws Exception{
        String userName = createString(50);
        String mail = "teaaefst@yuyhinoal.dalk";
        String password = createString(81);

        UserJson userJson = new UserJson(userName, password, mail);

        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test6CreateUserMailEmpty() throws Exception{
        String userName = createString(50);
        String mail = "";
        String password = createString(80);

        UserJson userJson = new UserJson(userName, password, mail);

        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test7CreateUserTooLongEmail() throws Exception{
        String userName = createString(50);
        String mail = createString(50) + "@yuyhinoal.dalk";
        String password = createString(80);

        UserJson userJson = new UserJson(userName, password, mail);

        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test8CreateUserInvalidMail() throws Exception{
        String userName = createString(50);
        String mail = "ungueltige-mail.de";
        String password = createString(80);

        UserJson userJson = new UserJson(userName, password, mail);

        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // ------------------------- SEND INVITATION ------------------------------

//    @Test
//    public void test1GetInvitations() throws Exception{
//        mockMvc.perform(
//                MockMvcRequestBuilders.get("/invitations"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.eventId", is(1)))
//                //TODO change dummy to real username
//                .andExpect(MockMvcResultMatchers.jsonPath("$.username", is("dummy")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.toInviteUsername", is(userName)));
//    }



}