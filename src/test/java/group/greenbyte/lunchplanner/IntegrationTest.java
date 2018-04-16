package group.greenbyte.lunchplanner;

import group.greenbyte.lunchplanner.event.EventJson;
import group.greenbyte.lunchplanner.location.LocationJson;
import group.greenbyte.lunchplanner.user.UserJson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
public class IntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void createUserAndLocationAndEvent() throws Exception {
        int locationId;

        // -------------- CREATE USER ---------------
        String userName = "dummy";
        String mail = "mail@mail.de";
        String password = "1234";

        UserJson userJson = new UserJson(userName, password, mail);
        String jsonUser = getJsonFromObject(userJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonUser))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        //Create location
        String locationName = "Location 1";
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;

        LocationJson location = new LocationJson(locationName, xCoordinate, yCoordinate, "");
        String jsonLocation = getJsonFromObject(location);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/location").contentType(MediaType.APPLICATION_JSON).content(jsonLocation))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();

        locationId = Integer.parseInt(result.getResponse().getContentAsString());

        // ---------------- CREATE EVENT ----------------
        long timeStart = System.currentTimeMillis() + 100000;
        String eventName = "event1";

        EventJson event = new EventJson(eventName, "", locationId, timeStart, timeStart + 1000);

        String json = getJsonFromObject(event);

        MvcResult resultEvent = mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();

        int eventId = Integer.parseInt(result.getResponse().getContentAsString());

        // ------------------- GET EVENT AND USER  -------------------------

    }

}
