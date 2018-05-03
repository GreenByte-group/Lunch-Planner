package group.greenbyte.lunchplanner.location;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.EventLogic;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.user.UserLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static group.greenbyte.lunchplanner.event.Utils.createEvent;
import static group.greenbyte.lunchplanner.location.Utils.createLocation;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
public class LocationControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private LocationLogic locationLogic;

    @Autowired
    private EventLogic eventLogic;

    @Autowired
    private UserLogic userLogic;

    private final String userName = "ajkbsJDKFLnlknasdfknNFnk";
    private final String otherUser = "NKDNLFnkandsfklnKNKnknkoqoalnksd";
    private int locationId;
    private String locationName = createString(50);
    private String locationDescription = createString(50);
    private int eventId;

    @Before
    public void setUp() throws Exception {
        createUserIfNotExists(userLogic, userName);
        createUserIfNotExists(userLogic, otherUser);
        locationId = createLocation(locationLogic, userName, locationName, locationDescription);
        eventId = createEvent(eventLogic, userName, locationId);

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    // ------------------ CREATE LOCATION ------------------------

    @Test
    @WithMockUser(username = userName)
    public void test1CreateLocationWithNoDescription() throws Exception {
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;

        LocationJson location = new LocationJson(createString(1), xCoordinate, yCoordinate, "");

        String json = getJsonFromObject(location);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/location").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();

        String response = result.getResponse().getContentAsString();

        try {
            Integer.valueOf(response);
        } catch (NumberFormatException e) {
            Assert.fail("Result is not a number");
        }

    }

    @Test
    @WithMockUser(username = userName)
    public void test2CreateLocationWithNormalDescriptionAndMaxLocationName() throws Exception {
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;

        LocationJson location = new LocationJson(createString(50), xCoordinate, yCoordinate, "Super Location");

        String json = getJsonFromObject(location);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/location").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();

        String response = result.getResponse().getContentAsString();

        try {
            Integer.valueOf(response);
        } catch (NumberFormatException e) {
            Assert.fail("Result is not a number");
        }

    }

    @Test
    @WithMockUser(username = userName)
    public void test3CreateLocationWithMaxDescriptionAndMaxLocationName() throws Exception {
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;

        LocationJson location = new LocationJson(createString(50), xCoordinate, yCoordinate, createString(1000));

        String json = getJsonFromObject(location);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/location").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();

        String response = result.getResponse().getContentAsString();

        try {
            Integer.valueOf(response);
        } catch (NumberFormatException e) {
            Assert.fail("Result is not a number");
        }

    }

    @Test
    @WithMockUser(username = userName)
    public void test4CreateLocationWithNoLocationName() throws Exception {
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;

        LocationJson location = new LocationJson("", xCoordinate, yCoordinate, createString(1000));

        String json = getJsonFromObject(location);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/location").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(status().isNotExtended());
    }

    @Test
    @WithMockUser(username = userName)
    public void test5CreateLocationNameTooLong() throws Exception {
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;

        LocationJson location = new LocationJson(createString(51), xCoordinate, yCoordinate, createString(1000));


        String json = getJsonFromObject(location);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/location").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test6CreateLocationDescriptionTooLong() throws Exception {
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;

        LocationJson location = new LocationJson(createString(50), xCoordinate, yCoordinate, createString(1001));

        String json = getJsonFromObject(location);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/location").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(status().isBadRequest());
    }

    // --------------------- GET LOCATION -------------------
    @Test
    @WithMockUser(username = userName)
    public void test1GetLocation() throws Exception {
        String locationName = createString(50);
        String locationDescription = createString(1000);
        int locationId = createLocation(locationLogic, userName, locationName, locationDescription);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/location/" + locationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locationName").value(locationName))
                .andExpect(jsonPath("$.locationDescription").value(locationDescription))
                .andExpect(jsonPath("$.locationId").value(locationId));
    }

    @Test
    @WithMockUser(username = userName)
    public void test2GetLocationNotFound() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/location/" + locationId + 100))
                .andExpect(status().isNotFound());
    }

    // ----------------------- SEARCH LOCATION ----------------------
    @Test
    @WithMockUser(username = userName)
    public void searchLocationValidUser() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("location/search/" + locationName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].locationName").value(locationName))
                .andExpect(jsonPath("$[0].locationDescription").value(locationDescription))
                .andExpect(jsonPath("$[0].locationId").value(locationId));
    }

    @Test
    @WithMockUser(username = otherUser)
    public void searchLocationUserNoRights() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("location/search/" + locationName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }
}