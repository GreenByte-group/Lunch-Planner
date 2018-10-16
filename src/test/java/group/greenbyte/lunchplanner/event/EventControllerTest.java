package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.database.BringService;
import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.team.TeamLogic;
import group.greenbyte.lunchplanner.user.UserLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.List;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.Utils.getJsonFromObject;
import static group.greenbyte.lunchplanner.event.Utils.createEvent;
import static group.greenbyte.lunchplanner.team.Utils.createTeamWithoutParent;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application.properties")

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
    private TeamLogic teamLogic;

    private final String userName = "asdfsd";
    private String location = "Test";
    private int eventId;
    private String eventShareToken;

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
        eventId = createEvent(eventLogic, userName, eventName, eventDescription, location,
                new Date(eventTimeStart));

        eventShareToken = eventLogic.getShareToken(eventId);

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    // ------------------ CREATE EVENT ------------------------

    @Test
    @WithMockUser(username = userName)
    public void test1CreateEventNoDescription() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson(createString(255), "", location, new Date(timeStart), false);

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

        EventJson event = new EventJson(createString(255), "Super Event", location, new Date(timeStart), false);

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

        EventJson event = new EventJson(createString(255), createString(1000), location, new Date(timeStart), false);

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

        EventJson event = new EventJson("", "", location, new Date(timeStart), false);

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test5CreateEventNameTooLong() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson(createString(256), "", location, new Date(timeStart),false);

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test6CreateEventDescriptionTooLong() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson("", "", location, new Date(timeStart),false);

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test7CreateEventTimeStartTooLow() throws Exception {
        long timeStart = System.currentTimeMillis() - 100000;

        EventJson event = new EventJson("", "", location, new Date(timeStart),false);

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test7CreateEventLocationEmpty() throws Exception {
        long timeStart = System.currentTimeMillis() - 100000;

        EventJson event = new EventJson("name", "des", " ", new Date(timeStart),false);

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test7CreateEventLocationTooLong() throws Exception {
        long timeStart = System.currentTimeMillis() - 100000;

        EventJson event = new EventJson("name", "des", createString(256), new Date(timeStart),false);

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // ------------------ GET ALL ------------------------

    @Test
    @WithMockUser(username = userName)
    public void test1GetAllEvents() throws Exception {
        createEvent(eventLogic, userName, location);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/event"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = userName)
    public void test2SearchEventsForUserSearchwordToBig() throws Exception {
        String searchword = createString(256);
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

    @Test
    @WithMockUser(username = "other User")
    public void test4InviteFriendNoPermission() throws Exception {

        String userName = createUserIfNotExists(userLogic, createString(1));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event/" + userName + "/invite/event/" + eventId))
                        .andExpect(MockMvcResultMatchers.status().isForbidden())
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
        String eventName = createString(256);

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
        String eventDescription = createString(Event.MAX_DESCRIPTION_LENGTH + 1);

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
        String newLocation = "new";

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/location").contentType(MediaType.TEXT_PLAIN_VALUE).content(newLocation))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Event event = eventDao.getEvent(eventId);
        if(!event.getLocation().equals(newLocation))
            Assert.fail("Location was not updated");
    }

    @Test
    @WithMockUser(username = userName)
    public void test3updateEventLocationNoValidLocation() throws Exception{
        String newLocation = " ";

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/location").contentType(MediaType.TEXT_PLAIN_VALUE).content(newLocation))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test3updateEventLocationTooLongLocation() throws Exception{
        String newLocation = createString(256);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/location").contentType(MediaType.TEXT_PLAIN_VALUE).content(newLocation))
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
                .andExpect(jsonPath("$.location").value(location))
                .andExpect(jsonPath("$.startDate").value(eventTimeStart));
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
                .andExpect(jsonPath("$[0].location").value(location))
                .andExpect(jsonPath("$[0].startDate").value(eventTimeStart));
    }

    @Test
    @WithMockUser(username = userName)
    public void test1SearchEventsTooLongSearchWord() throws Exception {
        String searchWord = createString(256);

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
    public void test2ReplyMaybe() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/reply").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(InvitationAnswer.MAYBE)))
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

    @Test
    @WithMockUser(username = "otherUser")
    public void test1CreateCommentNoPrivileges() throws Exception {
        String comment = "test";
        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/comment").contentType(MediaType.TEXT_PLAIN_VALUE).content(comment))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    @WithMockUser(username = userName)
    public void test1CreateCommentEventNotExists() throws Exception {
        String comment = "test";
        int eventId = 10000;
        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/comment").contentType(MediaType.TEXT_PLAIN_VALUE).content(comment))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }


    // ------------------ GET ALL COMMENTS -------------------------

    @Test
    @WithMockUser(username = userName)
    public void test1GetAllComments() throws Exception {

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/event/" + eventId + "/getComments"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser(username = userName)
    public void test1GetAllCommentsEventNotExists() throws Exception {
        eventId = 10000;

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/event/" + eventId + "/getComments"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    @WithMockUser(username = "otherUser")
    public void test1GetAllCommentsNoPermission() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/event/" + eventId + "/getComments"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    // ------------------ INVITE TEAM ------------------------

    @Test
    @WithMockUser(username = userName)
    public void test1InviteTeam() throws Exception {
       String teamName = createString(20);
       String description = createString(50);
       String teamCreator = createUserIfNotExists(userLogic, createString(50));
       int teamId = createTeamWithoutParent(teamLogic, teamCreator, teamName, description);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event/" + eventId + "/inviteTeam/" + teamId))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(username = userName)
    public void test2InviteTeamMaxUser() throws Exception {
        String teamName = createString(20);
        String description = createString(50);
        String teamCreator = createUserIfNotExists(userLogic, createString(50));

        int teamId = createTeamWithoutParent(teamLogic, teamCreator, teamName, description);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/event/" + eventId + "/inviteTeam/" + teamId))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
                .andReturn();
    }

    @Test
    @WithMockUser(username = userName)
    public void getTokenForEvent() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/event/" + eventId + "/token"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE));
    }

    @Test
    public void getEventByToken() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/event/token/" + eventShareToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.eventId").value(eventId));
    }

    // ------------------ SERVICE --------------------------
    @Test
    @WithMockUser(username = userName)
    public void test1PutService() throws Exception {
        String food = createString(BringService.MAX_NAME_LENGTH);
        String description = createString(BringService.MAX_DESCRIPTION_LENGTH);

        BringServiceJson bringServiceJson = new BringServiceJson(food, description);
        String json = getJsonFromObject(bringServiceJson);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/service").contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE));

        boolean serviceFound = false;
        List<BringService> bringServices = eventLogic.getService(userName, eventId);
        for(BringService service : bringServices) {
            if(service.getFood().equals(food)
                    && service.getDescription().equals(description)
                && service.getCreatorName().equals(userName)
                && service.getEventId().equals(eventId)) {
                serviceFound = true;
            }
        }

        assertTrue(serviceFound);
    }

    @Test
    @WithMockUser(username = userName)
    public void test2PutServiceTooLongName() throws Exception {
        String food = createString(BringService.MAX_NAME_LENGTH + 1);
        String description = createString(BringService.MAX_DESCRIPTION_LENGTH);

        BringServiceJson bringServiceJson = new BringServiceJson(food, description);
        String json = getJsonFromObject(bringServiceJson);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/service").contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = userName)
    public void test3PutServiceTooLongDescription() throws Exception {
        String food = createString(BringService.MAX_NAME_LENGTH);
        String description = createString(BringService.MAX_DESCRIPTION_LENGTH + 1);

        BringServiceJson bringServiceJson = new BringServiceJson(food, description);
        String json = getJsonFromObject(bringServiceJson);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/service").contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "otherUser")
    public void test4PutServiceNoPermission() throws Exception {
        String food = createString(BringService.MAX_NAME_LENGTH);
        String description = createString(BringService.MAX_DESCRIPTION_LENGTH);

        BringServiceJson bringServiceJson = new BringServiceJson(food, description);
        String json = getJsonFromObject(bringServiceJson);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/service").contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = userName)
    public void test1GetServices() throws Exception {
        int eventId = createEvent(eventLogic, userName, "location");
        String food = createString(BringService.MAX_NAME_LENGTH);
        String description = createString(BringService.MAX_DESCRIPTION_LENGTH);

        eventLogic.putService(userName, eventId, food, description);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/event/" + eventId + "/service"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].eventId").value(eventId))
                .andExpect(jsonPath("$[0].food").value(food))
                .andExpect(jsonPath("$[0].description").value(description))
                .andExpect(jsonPath("$[0].creatorName").value(userName));
    }

    @Test
    @WithMockUser(username = "otherUser")
    public void test2GetServicesNoPermission() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/event/" + eventId + "/service"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = userName)
    public void test3GetServicesEventNotExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/event/" + eventId + 10000 + "/service"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = userName)
    public void test1SetAcceptorServices() throws Exception {
        int eventId = createEvent(eventLogic, userName, "location");
        String otherUser = createUserIfNotExists(userLogic, "user2");

        eventLogic.inviteFriend(userName, otherUser, eventId);

        String food = createString(BringService.MAX_NAME_LENGTH);
        String description = createString(BringService.MAX_DESCRIPTION_LENGTH);

        eventLogic.putService(otherUser, eventId, food, description);
        List<BringService> bringServices = eventLogic.getService(userName, eventId);

        String url = "/event/" + eventId + "/service/" + bringServices.get(0).getServiceId();
        mockMvc.perform(
                MockMvcRequestBuilders.post(url))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        bringServices = eventLogic.getService(userName, eventId);
        assertEquals(userName, bringServices.get(0).getAccepter());
    }

    @Test
    @WithMockUser(username = userName)
    public void test2SetAcceptorServicesAlreadyAccepted() throws Exception {
        int eventId = createEvent(eventLogic, userName, "location");
        String otherUser = createUserIfNotExists(userLogic, "user2");

        eventLogic.inviteFriend(userName, otherUser, eventId);

        String food = createString(BringService.MAX_NAME_LENGTH);
        String description = createString(BringService.MAX_DESCRIPTION_LENGTH);

        eventLogic.putService(otherUser, eventId, food, description);
        List<BringService> bringServices = eventLogic.getService(userName, eventId);

        String url = "/event/" + eventId + "/service/" + bringServices.get(0).getServiceId();
        mockMvc.perform(
                MockMvcRequestBuilders.post(url))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(
                MockMvcRequestBuilders.post(url))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}