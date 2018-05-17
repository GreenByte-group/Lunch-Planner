package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.AppConfig;
<<<<<<< HEAD
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
=======
import group.greenbyte.lunchplanner.event.EventLogic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
<<<<<<< HEAD
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
=======
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.Utils.getJsonFromObject;
import static group.greenbyte.lunchplanner.event.Utils.createEvent;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
<<<<<<< HEAD
=======
@Transactional
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private EventLogic eventLogic;

    @Autowired
    private UserLogic userLogic;

<<<<<<< HEAD
    @Autowired
    private LocationLogic locationLogic;

    private String userName;
    private int locationId;
=======
    private final String userName = "banane";
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    private int eventId;

    @Before
    public void setUp() throws Exception {
<<<<<<< HEAD
        userName = createUserIfNotExists(userLogic, "dummy");
        locationId = createLocation(locationLogic, userName, "Test location", "test description");
        eventId = createEvent(eventLogic, userName, locationId);
=======
        createUserIfNotExists(userLogic, userName);
        eventId = createEvent(eventLogic, userName, "Test location");
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        //mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    // ------------------ CREATE USER ------------------------
    @Test
    public void test1CreateUserValidParam() throws Exception{
        String userName = createString(50);
        String mail = "teeeaefst@yuyhinoal.dalk";
<<<<<<< HEAD
        String password = createString(80);

        UserJson userJson = new UserJson(userName, password, mail);

        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
=======
        String password = createString(200);

        UserJson userJson = new UserJson(userName, password, mail);
        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                post("/user").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    }

    @Test
    public void test2CreateUserEmptyUsername() throws Exception{
        String userName = "";
        String mail = "teasdfast@yuyhinoal.dalk";
<<<<<<< HEAD
        String password = createString(80);
=======
        String password = createString(200);
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

        UserJson userJson = new UserJson(userName, password, mail);
        String json = getJsonFromObject(userJson);

        mockMvc.perform(
<<<<<<< HEAD
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
=======
                post("/user").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    }

    @Test
    public void test3CreateUserTooLongUserName() throws Exception{
        String userName = createString(51);
        String mail = "teasdfst@yuyhinoal.dalk";
<<<<<<< HEAD
        String password = createString(80);

        UserJson userJson = new UserJson(userName, password, mail);

        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
=======
        String password = createString(200);

        UserJson userJson = new UserJson(userName, password, mail);
        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                post("/user").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    }
  
    @Test
    public void test4CreateUserEmptyPassword() throws Exception{
        String userName = createString(50);
        String mail = "tesdaft@yuyhinoal.dalk";
        String password = "";

        UserJson userJson = new UserJson(userName, password, mail);
<<<<<<< HEAD

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

=======
        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                post("/user").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test6CreateUserMailEmpty() throws Exception{
        String userName = createString(50);
        String mail = "";
        String password = createString(200);

        UserJson userJson = new UserJson(userName, password, mail);
        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                post("/user").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test7CreateUserTooLongEmail() throws Exception{
        String userName = createString(50);
        String mail = createString(50) + "@yuyhinoal.dalk";
        String password = createString(200);

        UserJson userJson = new UserJson(userName, password, mail);
        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                post("/user").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test8CreateUserInvalidMail() throws Exception{
        String userName = createString(50);
        String mail = "ungueltige-mail.de";
        String password = createString(200);

        UserJson userJson = new UserJson(userName, password, mail);
        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                post("/user").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test9CreateUserAlreadyExists() throws Exception {
        String mail = "mail@mail.de";
        String password = createString(200);

        UserJson userJson = new UserJson(userName, password, mail);
        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                post("/user").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    // ------------------------- SEARCH USER ------------------------------

    @Test
    @WithMockUser(username = userName)
    public void test1ValidParam() throws Exception {
        mockMvc.perform(
                get("/user/search/" + userName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].userName").value(userName));
    }

    @Test
    @WithMockUser(username = userName)
    public void test3SearchwordIsNull() throws Exception {
        mockMvc.perform(
                get("/user/search/"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = userName)
    public void test3SearchResultIsNoting() throws Exception {
        mockMvc.perform(
                get("/user/search/" + createString(10)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }


>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
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


<<<<<<< HEAD
=======
    // ------------------------- LOGIN USER ------------------------------

    //TODO test login
//    @Test
//    public void test1Login() throws Exception {
//        mockMvc
//                .perform(post("/login").param("username", userName).param("password", "1234"))
//                .andExpect(status().isOk())
//                .andExpect(authenticated().withUsername(userName));
//    }

//    @Test
//    public void test1LoginUser() throws Exception{
//        String userName = "A";
//        String password = "A";
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/login")
//                        .param("username", userName)
//                        .param("password", password))
//                .andExpect(MockMvcResultMatchers.status().isAccepted());
//    }
//
//    @Test
//    public void test2LoginUserMaxLength() throws Exception{
//        String userName = createString(50);
//        String password = createString(80);
//
//        UserJson userJson = new UserJson(userName, password);
//
//        String json = getJsonFromObject(userJson);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/user/loginUser").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
//                .andExpect(MockMvcResultMatchers.status().isAccepted());
//    }
//
//    @Test
//    public void test3LoginUserUserNameTooLong() throws Exception{
//        String userName = createString(51);
//        String password = createString(80);
//
//        UserJson userJson = new UserJson(userName, password);
//
//        String json = getJsonFromObject(userJson);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/user/loginUser").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//
//    @Test
//    public void test4LoginUserNoUserName() throws Exception{
//        String userName = "";
//        String password = createString(80);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/login")
//                        .requestAttr("username", userName).requestAttr("password", password))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//
//    @Test
//    public void test5LoginUserPasswordTooLong() throws Exception{
//        String userName = createString(50);
//        String password = createString(81);
//
//        UserJson userJson = new UserJson(userName, password);
//
//        String json = getJsonFromObject(userJson);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/user/loginUser").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//
//    @Test
//    public void test6LoginUserNoPassword() throws Exception{
//        String userName = createString(50);
//        String password = "";
//
//        UserJson userJson = new UserJson(userName, password);
//
//        String json = getJsonFromObject(userJson);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/user/loginUser").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }

>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

}