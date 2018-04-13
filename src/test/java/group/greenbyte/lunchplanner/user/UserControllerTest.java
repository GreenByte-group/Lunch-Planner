package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.EventController;
import group.greenbyte.lunchplanner.event.EventJson;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.Utils.getJsonFromObject;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
public class UserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private EventController eventController;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController)
                .build();
    }

    // ------------------ CREATE USER ------------------------


    String usernameEmpty = "";
    String username50Chars = createString(50);
    String username51Chars = createString(51);
    String passwordEmpty = "";
    String password80Chars = createString(80);
    String getPassword81Chars = createString(81);
    String emailEmpty ="";
    String email50Chars =createString(50);
    String email51Chars =createString(51);
    String emailInvalid = "ungültige-mail.de";


    @Test
    public void test1CreateUserValidParam() throws Exception{

        String user = "{username:" + username50Chars + ", password:" + password80Chars + ", email:" + email50Chars +"}";

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(user))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
    }

    @Test
    public void test2CreateUserEmptyUsername() throws Exception{

        String user = "{username:" + usernameEmpty + ", password:" + password80Chars + ", email:" + email50Chars +"}";

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(user))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
    }
}