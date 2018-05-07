package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.location.LocationLogic;
import group.greenbyte.lunchplanner.user.UserLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.Utils.getJsonFromObject;
import static group.greenbyte.lunchplanner.event.Utils.createEvent;
import static group.greenbyte.lunchplanner.location.Utils.createLocation;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
@Transactional
public class EventControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private EventLogic eventLogic;

    @Autowired
    private EventDao eventDao;

    @Autowired
    private UserLogic userLogic;

    @Autowired
    private LocationLogic locationLogic;

    private final String userName = "asdfsd";
    private int locationId;
    private int eventId;

    private String eventName;
    private String eventDescription;
    private long eventTimeStart;
    private long eventTimeEnd;
    private Authentication authentication;

    @Before
    public void setUp() throws Exception {
        eventName = createString(10);
        eventDescription = createString(10);
        eventTimeStart = System.currentTimeMillis() + 10000;
        eventTimeEnd = eventTimeStart + 10000;

        // ohne millisekunden
        eventTimeStart = 1000 * (eventTimeStart / 1000);
        eventTimeEnd = 1000 * (eventTimeEnd / 1000);

        createUserIfNotExists(userLogic, userName);
        locationId = createLocation(locationLogic, userName, createString(50), createString(10));
        eventId = createEvent(eventLogic, userName, eventName, eventDescription, locationId,
                new Date(eventTimeStart), new Date(eventTimeEnd));

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    // ------------------ CREATE EVENT ------------------------

    @Test
    @WithMockUser(username = userName)
    public void test1CreateEventNoDescription() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson(createString(50), "", locationId, new Date(timeStart), new Date(timeStart + 1000));

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
    @WithMockUser(username = userName)
    public void test2CreateEventNormalDescription() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson(createString(50), "Super Event", locationId, new Date(timeStart), new Date(timeStart + 1000));

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
    @WithMockUser(username = userName)
    public void test3CreateEventLongDescription() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson(createString(50), createString(1000), locationId, new Date(timeStart), new Date(timeStart + 1000));

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
    @WithMockUser(username = userName)
    public void test4CreateEventNoName() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson("", "", locationId, new Date(timeStart), new Date(timeStart + 1000));

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test5CreateEventNameTooLong() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson(createString(51), "", locationId, new Date(timeStart), new Date(timeStart + 1000));

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test6CreateEventDescriptionTooLong() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson("", "", locationId, new Date(timeStart), new Date(timeStart + 1000));

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test7CreateEventTimeStartTooLow() throws Exception {
        long timeStart = System.currentTimeMillis() - 100000;
        long timeEnd = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson("", "", locationId, new Date(timeStart), new Date(timeEnd));

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test8CreateEventTimeStartAfterTimeEnd() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;
        long timeEnd = System.currentTimeMillis() + 1000;

        EventJson event = new EventJson("test", "", locationId, new Date(timeStart), new Date(timeEnd));

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test9CreateEventLocationNotExists() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson("", "", locationId + 1000, new Date(timeStart), new Date(timeStart + 10000));

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    // ------------------ GET ALL ------------------------

    @Test
    @WithMockUser(username = userName)
    public void test1GetAllEvents() throws Exception {
        createEvent(eventLogic, userName, locationId);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/event"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = userName)
    public void test2SearchEventsForUserSearchwordToBig() throws Exception {
        String searchword = createString(51);
        String json = getJsonFromObject(searchword);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // ------------------ INVITE FRIEND ------------------------


    @Test
    @WithMockUser(username = userName)
    public void test1InviteFriend() throws Exception {
        String userName = createUserIfNotExists(userLogic, createString(50));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event/" + userName + "/invite/event/" + eventId))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(username = userName)
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
    @WithMockUser(username = userName)
    public void test3InviteFriendInvalidName() throws Exception {

        String userName = createUserIfNotExists(userLogic, createString(51));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event/" + userName + "/invite/event/" + eventId))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE));

    }

    @Test (expected = AssertionError.class)
    @WithMockUser(username = userName)
    public void test4InviteFriendEmptyName() throws Exception {

        String userName = createUserIfNotExists(userLogic, createString(1));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event/" + userName + "/invite/event/" + eventId))
                        .andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE));
    }

    // ------------------ UPDATE EVENT NAME ------------------------

    @Test
    @WithMockUser(username = userName)
    public void test1updateEventName() throws Exception{
        String eventName = createString(50);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/name")
                        .contentType(MediaType.TEXT_PLAIN_VALUE).content(eventName))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Event event = eventDao.getEvent(eventId);
        if(!event.getEventName().equals(eventName))
            Assert.fail("Name was not updated");
    }

    @Test
    @WithMockUser(username = userName)
    public void test2updateEventNameTooLong() throws Exception{
        String eventName = createString(51);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/name").contentType(MediaType.TEXT_PLAIN_VALUE).content(eventName))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test3updateEventNameEmpty() throws Exception{
        String eventName = "";

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/name").contentType(MediaType.TEXT_PLAIN_VALUE).content(eventName))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // ------------------- UPDATE EVENT DESCRIPTION -----------------

    @Test
    @WithMockUser(username = userName)
    public void test1updateEventDescription() throws Exception{
        String eventDescription = createString(50);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/description").contentType(MediaType.TEXT_PLAIN_VALUE).content(eventDescription))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Event event = eventDao.getEvent(eventId);
        if(!event.getEventDescription().equals(eventDescription))
            Assert.fail("Description was not updated");
    }

    @Test
    @WithMockUser(username = userName)
    public void test2updateEventDescriptionTooLong() throws Exception{
        String eventDescription = createString(Event.MAX_DESCRITION_LENGTH + 1);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/description").contentType(MediaType.TEXT_PLAIN_VALUE).content(eventDescription))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test3updateEventDescriptionEmpty() throws Exception{
        String eventDescription = "";

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/description").contentType(MediaType.TEXT_PLAIN_VALUE).content(eventDescription))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // ------------------ UPDATE EVENT LOCATION ------------------

    @Test
    @WithMockUser(username = userName)
    public void test1updateEventLocation() throws Exception{
        int newLocationId = createLocation(locationLogic, userName, "updated event", "updated description");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/location").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(newLocationId)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Event event = eventDao.getEvent(eventId);
        if(event.getLocation().getLocationId() != newLocationId)
            Assert.fail("Location was not updated");
    }

    @Test
    @WithMockUser(username = userName)
    public void test2updateEventLocationNoInt() throws Exception{
        int newLocationId = createLocation(locationLogic, userName, "updated event", "updated description");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/location").contentType(MediaType.TEXT_PLAIN_VALUE).content("string"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test3updateEventLocationNoValidLocationId() throws Exception{
        int newLocationId = createLocation(locationLogic, userName, "updated event", "updated description");

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/location").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(100000)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // ------------------ UPDATE EVENT TIMESTART -------------------------
    @Test
    @WithMockUser(username = userName)
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

        Event event = eventDao.getEvent(eventId);
        if(event.getStartDate().getTime() != startTime)
            Assert.fail("Time start was not updated");
    }

    @Test
    @WithMockUser(username = userName)
    public void test2updateEventStartTimeTooEarly() throws Exception{
        long startTime = System.currentTimeMillis() - 10000;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/timestart").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(startTime)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test3updateEventStartTimeTooLate() throws Exception{
        long startTime = System.currentTimeMillis() + 1000000000L;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/timestart").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(startTime)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test4updateEventStartTimeNoLongAsParameter() throws Exception{
        long startTime = System.currentTimeMillis() + 1000000000L;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/timestart").contentType(MediaType.TEXT_PLAIN_VALUE).content("string"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // ------------------ UPDATE EVENT TIMEEND -------------------------
    @Test
    @WithMockUser(username = userName)
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

        Event event = eventDao.getEvent(eventId);
        if(event.getEndDate().getTime() != endTime)
            Assert.fail("Time end was not updated");
    }

    @Test
    @WithMockUser(username = userName)
    public void test2updateEventEndTimeTooEarly() throws Exception{
        long endTime = System.currentTimeMillis() - 10000;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/timeend").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(endTime)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test3updateEventEndTimeNoLongAsParameter() throws Exception{
        long endTime = System.currentTimeMillis() - 10000;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/timeend").contentType(MediaType.TEXT_PLAIN_VALUE).content("string"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // -------------------- GET EVENT ---------------------

    @Test
    @WithMockUser(username = userName)
    public void test1GetEventValid() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/event/" + eventId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.eventId").value(eventId))
                .andExpect(jsonPath("$.eventName").value(eventName))
                .andExpect(jsonPath("$.eventDescription").value(eventDescription))
                .andExpect(jsonPath("$.location.locationId").value(locationId))
                .andExpect(jsonPath("$.startDate").value(eventTimeStart))
                .andExpect(jsonPath("$.endDate").value(eventTimeEnd));
    }

    @Test
    @WithMockUser(username = userName)
    public void test2GetEventInValid() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/event/" + eventId + 100))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "otherUser")
    public void test2GetEventInvalidUser() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/event/" + eventId))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    // ---------------- SEARCH EVENTS--------------------
    @Test
    @WithMockUser(username = userName)
    public void test1SearchEvents() throws Exception {
        String searchWord = eventName;

        mockMvc.perform(
                MockMvcRequestBuilders.get("/event/search/" + searchWord))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].eventId").value(eventId))
                .andExpect(jsonPath("$[0].eventName").value(eventName))
                .andExpect(jsonPath("$[0].eventDescription").value(eventDescription))
                .andExpect(jsonPath("$[0].location.locationId").value(locationId))
                .andExpect(jsonPath("$[0].startDate").value(eventTimeStart))
                .andExpect(jsonPath("$[0].endDate").value(eventTimeEnd));;
    }

    @Test
    @WithMockUser(username = userName)
    public void test1SearchEventsTooLongSearchWord() throws Exception {
        String searchWord = createString(51);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/event/search/" + searchWord))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test1SearchEventsNoSearchWord() throws Exception {
        String searchWord = "";

        mockMvc.perform(
                MockMvcRequestBuilders.get("/event/search/" + searchWord))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    // ------------------ REPLY -------------------------
    @Test
    @WithMockUser(username = userName)
    public void test1ReplyAccept() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/reply").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(InvitationAnswer.ACCEPT)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(username = userName)
    public void test2ReplyReject() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/reply").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(InvitationAnswer.REJECT)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(username = userName)
    public void test3ReplyInvalidAnswer() throws Exception {
        String answer = "keine lust";
        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/reply").contentType(MediaType.TEXT_PLAIN_VALUE).content(answer))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test4ReplyEventDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + 100 + "/reply").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(InvitationAnswer.ACCEPT)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    // ------------------ CREATE COMMENT -------------------------

    @Test
    @WithMockUser(username = userName)
    public void test1CreateComment() throws Exception {
        String comment = createString(1);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/comment").contentType(MediaType.TEXT_PLAIN_VALUE).content(comment))
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    @WithMockUser(username = userName)
    public void test2CreateCommentMaxComment() throws Exception {
        String comment = createString(100);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/comment").contentType(MediaType.TEXT_PLAIN_VALUE).content(comment))
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    @WithMockUser(username = userName)
    public void test1CreateCommentTooLong() throws Exception {
        String comment = createString(101);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/comment").contentType(MediaType.TEXT_PLAIN_VALUE).content(comment))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @WithMockUser(username = userName)
    public void test1CreateCommentNoComment() throws Exception {
        String comment = "";
        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/comment").contentType(MediaType.TEXT_PLAIN_VALUE).content(comment))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }





}