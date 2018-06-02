package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.EventLogic;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.Utils.getJsonFromObject;
import static group.greenbyte.lunchplanner.event.Utils.createEvent;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
@Transactional
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private EventLogic eventLogic;

    @Autowired
    private UserLogic userLogic;

    private final String userName = "banane";
    private int eventId;

    @Before
    public void setUp() throws Exception {
        createUserIfNotExists(userLogic, userName);
        eventId = createEvent(eventLogic, userName, "Test location");

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        //mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    // ------------------ CREATE USER ------------------------
    @Test
    public void test1CreateUserValidParam() throws Exception{
        String userName = createString(50);
        String mail = "teeeaefst@yuyhinoal.dalk";
        String password = createString(200);

        UserJson userJson = new UserJson(userName, password, mail);
        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                post("/user").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    public void test2CreateUserEmptyUsername() throws Exception{
        String userName = "";
        String mail = "teasdfast@yuyhinoal.dalk";
        String password = createString(200);

        UserJson userJson = new UserJson(userName, password, mail);
        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                post("/user").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test3CreateUserTooLongUserName() throws Exception{
        String userName = createString(51);
        String mail = "teasdfst@yuyhinoal.dalk";
        String password = createString(200);

        UserJson userJson = new UserJson(userName, password, mail);
        String json = getJsonFromObject(userJson);

        mockMvc.perform(
                post("/user").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }
  
    @Test
    public void test4CreateUserEmptyPassword() throws Exception{
        String userName = createString(50);
        String mail = "tesdaft@yuyhinoal.dalk";
        String password = "";

        UserJson userJson = new UserJson(userName, password, mail);
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
    public void test3SearchResultIsNoting() throws Exception {
        mockMvc.perform(
                get("/user/search/" + createString(10)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    // ------------------ FCM TOKEN -----------------------
    @Test
    @WithMockUser(username = userName)
    public void test1setFcmToken() throws Exception {
        String fcmToken = "fcmToken";
        mockMvc.perform(
                post("/user/fcm").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fcmToken\":\"" + fcmToken + "\"}"))
                .andExpect(status().isNoContent());

        assertEquals(fcmToken, userLogic.getUser(userName).getFcmToken());
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


}