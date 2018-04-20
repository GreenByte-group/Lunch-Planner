package group.greenbyte.lunchplanner.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.location.LocationLogic;
import group.greenbyte.lunchplanner.user.UserLogic;
import group.greenbyte.lunchplanner.user.TestInvitePersonJson;
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

import java.util.Date;

import java.io.Serializable;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.Utils.getJsonFromObject;
import static group.greenbyte.lunchplanner.event.Utils.createEvent;
import static group.greenbyte.lunchplanner.location.Utils.createLocation;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
public class EventControllerTest {

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

    // ------------------ CREATE EVENT ------------------------

    @Test
    public void test1CreateEventNoDescription() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson(createString(50), "", 1, new Date(timeStart), new Date(timeStart + 1000));

        String json = getJsonFromObject(event);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();

        String response = result.getResponse().getContentAsString();

        try {
            Integer.valueOf(response);
        } catch(NumberFormatException e) {
            Assert.fail("Result is not a number");
        }
    }

    @Test
    public void test2CreateEventNormalDescription() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson(createString(50), "Super Event", 1, new Date(timeStart), new Date(timeStart + 1000));

        String json = getJsonFromObject(event);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
                .andReturn();

        String response = result.getResponse().getContentAsString();

        try {
            Integer.valueOf(response);
        } catch(NumberFormatException e) {
            Assert.fail("Result is not a number");
        }
    }

    @Test
    public void test3CreateEventLongDescription() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson(createString(50), createString(1000), 1, new Date(timeStart), new Date(timeStart + 1000));

        String json = getJsonFromObject(event);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
                .andReturn();

        String response = result.getResponse().getContentAsString();

        try {
            Integer.valueOf(response);
        } catch(NumberFormatException e) {
            Assert.fail("Result is not a number");
        }
    }

    @Test
    public void test4CreateEventNoName() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson("", "", 1, new Date(timeStart), new Date(timeStart + 1000));

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test5CreateEventNameTooLong() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson(createString(51), "", 1, new Date(timeStart), new Date(timeStart + 1000));

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test6CreateEventDescriptionTooLong() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson("", "", 1, new Date(timeStart), new Date(timeStart + 1000));

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test7CreateEventTimeStartTooLow() throws Exception {
        long timeStart = System.currentTimeMillis() - 100000;
        long timeEnd = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson("", "", 1, new Date(timeStart), new Date(timeEnd));

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test8CreateEventTimeStartAfterTimeEnd() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;
        long timeEnd = System.currentTimeMillis() + 1000;

        EventJson event = new EventJson("", "", 1, new Date(timeStart), new Date(timeEnd));

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    // ------------------ GET ALL ------------------------

    @Test
    public void test1GetAllEvents() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/event"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void test2SearchEventsForUserSearchwordToBig() throws Exception {
        String searchword = createString(51);
        String json = getJsonFromObject(searchword);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // ------------------ INVITE FRIEND ------------------------


    @Test
    public void test1InviteFriend() throws Exception {
        String userName = createUserIfNotExists(userLogic, createString(50));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event/" + userName + "/invite/event/" + eventId))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void test2InviteFriendMaxUser() throws Exception {

        String userName = createUserIfNotExists(userLogic, createString(50));

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/event/" + userName + "/invite/event/" + eventId))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
                .andReturn();

        String response = result.getResponse().getContentAsString();
    }

    @Test
    public void test3InviteFriendInvalidName() throws Exception {

        String userName = createUserIfNotExists(userLogic, createString(51));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event/" + userName + "/invite/event/" + eventId))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE));

    }

    @Test (expected = AssertionError.class)
    public void test4InviteFriendEmptyName() throws Exception {

        String userName = createUserIfNotExists(userLogic, createString(1));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event/" + userName + "/invite/event/" + eventId))
                        .andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE));
    }





    // ------------------ UPDATE EVENT NAME ------------------------

    @Test
    public void test1updateEventName() throws Exception{
        String eventName = createString(50);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/name").contentType(MediaType.TEXT_PLAIN_VALUE).content(eventName))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Event event = eventLogic.getEvent(eventId);
        if(!event.getEventName().equals(eventName))
            Assert.fail("Name was not updated");
    }

    @Test
    public void test2updateEventNameTooLong() throws Exception{
        String eventName = createString(51);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/name").contentType(MediaType.TEXT_PLAIN_VALUE).content(eventName))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test3updateEventNameEmpty() throws Exception{
        String eventName = "";

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/name").contentType(MediaType.TEXT_PLAIN_VALUE).content(eventName))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // ------------------- UPDATE EVENT DESCRIPTION -----------------

    @Test
    public void test1updateEventDescription() throws Exception{
        String eventDescription = createString(50);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/description").contentType(MediaType.TEXT_PLAIN_VALUE).content(eventDescription))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Event event = eventLogic.getEvent(eventId);
        if(!event.getEventDescription().equals(eventDescription))
            Assert.fail("Description was not updated");
    }

    @Test
    public void test2updateEventDescriptionTooLong() throws Exception{
        String eventDescription = createString(Event.MAX_DESCRITION_LENGTH + 1);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/description").contentType(MediaType.TEXT_PLAIN_VALUE).content(eventDescription))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test3updateEventDescriptionEmpty() throws Exception{
        String eventDescription = "";

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/description").contentType(MediaType.TEXT_PLAIN_VALUE).content(eventDescription))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // ------------------ UPDATE EVENT LOCATION ------------------

    @Test
    public void test1updateEventLocation() throws Exception{
        int newLocationId = createLocation(locationLogic, userName, "updated event", "updated description");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/location").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(newLocationId)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Event event = eventLogic.getEvent(eventId);
        if(event.getLocation().getLocationId() != newLocationId)
            Assert.fail("Location was not updated");
    }

    @Test
    public void test2updateEventLocationNoInt() throws Exception{
        int newLocationId = createLocation(locationLogic, userName, "updated event", "updated description");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/location").contentType(MediaType.TEXT_PLAIN_VALUE).content("string"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test3updateEventLocationNoValidLocationId() throws Exception{
        int newLocationId = createLocation(locationLogic, userName, "updated event", "updated description");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/location").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(100000)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // ------------------ UPDATE EVENT TIMESTART -------------------------
    @Test
    public void test1updateEventStartTime() throws Exception{
        long startTime = System.currentTimeMillis() + 10000;

        /*
        In der Datenbank werden keine Millisekunden gespeichert. Zum Vergleichen der Zeit müssen also
        die Millisekunden ignoriert werden.
         */
        startTime = 1000 * (startTime / 1000);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/timestart").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(startTime)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Event event = eventLogic.getEvent(eventId);
        if(event.getStartDate().getTime() != startTime)
            Assert.fail("Time start was not updated");
    }

    @Test
    public void test2updateEventStartTimeTooEarly() throws Exception{
        long startTime = System.currentTimeMillis() - 10000;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/timestart").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(startTime)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test3updateEventStartTimeTooLate() throws Exception{
        long startTime = System.currentTimeMillis() + 1000000000L;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/timestart").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(startTime)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test4updateEventStartTimeNoLongAsParameter() throws Exception{
        long startTime = System.currentTimeMillis() + 1000000000L;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/timestart").contentType(MediaType.TEXT_PLAIN_VALUE).content("string"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // ------------------ UPDATE EVENT TIMEEND -------------------------
    @Test
    public void test1updateEventEndTime() throws Exception{
        long endTime = System.currentTimeMillis() + 1000000;

        /*
        In der Datenbank werden keine Millisekunden gespeichert. Zum Vergleichen der Zeit müssen also
        die Millisekunden ignoriert werden.
         */
        endTime = 1000 * (endTime / 1000);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/timeend").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(endTime)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Event event = eventLogic.getEvent(eventId);
        if(event.getEndDate().getTime() != endTime)
            Assert.fail("Time end was not updated");
    }

    @Test
    public void test2updateEventEndTimeTooEarly() throws Exception{
        long endTime = System.currentTimeMillis() - 10000;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/timeend").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(endTime)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test3updateEventEndTimeNoLongAsParameter() throws Exception{
        long endTime = System.currentTimeMillis() - 10000;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/timeend").contentType(MediaType.TEXT_PLAIN_VALUE).content("string"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}