package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.location.LocationLogic;
import group.greenbyte.lunchplanner.user.UserLogic;
import org.junit.Assert;
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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.event.Utils.createEvent;
import static group.greenbyte.lunchplanner.location.Utils.createLocation;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;
import static group.greenbyte.lunchplanner.Utils.getJsonFromObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
public class EventLogicTest {

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
    }

    // ------------------------- CREATE EVENT ------------------------------

    @Test
    public void test1createEventNoDescription() throws Exception{
        String userName = createString(50);
        String eventName = createString(50);
        String description = "";
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        int result = eventLogic.createEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date(timeEnd));

    }

    @Test
    public void test2createEventLongDescription() throws Exception {
        String userName = createString(50);
        String eventName = createString(50);
        String description = createString(1000);
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        int result = eventLogic.createEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date(timeEnd));
    }

    @Test(expected = HttpRequestException.class)
    public void test3createEventEmptyUserName() throws Exception {
        String userName = "";
        String eventName = createString(50);
        String description = "";
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        int result = eventLogic.createEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date(timeEnd));
    }

    @Test(expected = HttpRequestException.class)
    public void test4createEventTooLongUserName() throws Exception {
        String userName = createString(51);
        String eventName = createString(50);
        String description = "";
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        int result = eventLogic.createEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date(timeEnd));
    }

    @Test(expected = HttpRequestException.class)
    public void test5createEventEmptyEventName() throws Exception {
        String userName = createString(50);
        String eventName = "";
        String description = "";
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        int result = eventLogic.createEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date(timeEnd));
    }

    @Test(expected = HttpRequestException.class)
    public void test6createEventTooLongEventName() throws Exception {
        String userName = createString(50);
        String eventName = createString(51);
        String description = "";
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        int result = eventLogic.createEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date(timeEnd));
    }

    @Test(expected = HttpRequestException.class)
    public void test7createEventTooLongDescription() throws Exception {
        String userName = createString(50);
        String eventName = createString(50);
        String description = createString(1001);
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        int result = eventLogic.createEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date(timeEnd));
    }

    @Test(expected = HttpRequestException.class)
    public void test8createEventTimeStartTooEarly() throws Exception {
        String userName = createString(50);
        String eventName = createString(50);
        String description = "";
        int locationId = 1;
        long timeStart = System.currentTimeMillis() - 10000;
        long timeEnd = timeStart + 10000;

        int result = eventLogic.createEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date(timeEnd));
    }

    @Test(expected = HttpRequestException.class)
    public void test4createEventTimeStartAfterTimeEnd() throws Exception {
        String userName = createString(50);
        String eventName = createString(50);
        String description = "";
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart - 10000;

        int result = eventLogic.createEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date(timeEnd));
    }


    // ------------------------- GET ALL EVENTS ------------------------------

    @Test(expected = HttpRequestException.class)
    public void test1getAllEventsEmptyUsername() throws Exception {
        String userName  = createString(0);

        List<Event> result = eventLogic.getAllEvents(userName);
    }

    @Test (expected = HttpRequestException.class)
    public void test2getAllEventsUsernameIsToLong()throws Exception{
        String userName = createString(51);

        List<Event> result = eventLogic.getAllEvents(userName);
    }

    @Test
    public void test5getAllEventsOk() throws Exception {
        String userName  = createString(50);

        List<Event> result = eventLogic.getAllEvents(userName);
    }

    // ------------------ UPDATE EVENT  ------------------------

    // Event Name
    @Test
    public void updateEventName() throws Exception {
        String eventName = createString(50);

        eventLogic.updateEventName(userName, eventId, eventName);

        Event event = eventLogic.getEvent(eventId);
        if(!event.getEventName().equals(eventName))
            Assert.fail("Name was not updated");
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventNameEmpty() throws Exception {
        String eventName = "";

        eventLogic.updateEventName(userName, eventId, eventName);
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventNameTooLong() throws Exception {
        String eventName = createString(51);

        eventLogic.updateEventName(userName, eventId, eventName);
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventNameOnNotExistingEvent() throws Exception {
        String eventName = createString(50);

        eventLogic.updateEventName(userName, 10000, eventName);
    }

    // Event Description
    @Test
    public void updateEventDescription() throws Exception {
        String eventDescription = createString(Event.MAX_DESCRITION_LENGTH);

        eventLogic.updateEventDescription(userName, eventId, eventDescription);

        Event event = eventLogic.getEvent(eventId);
        if(!event.getEventDescription().equals(eventDescription))
            Assert.fail("Description was not updated");
    }

    @Test
    public void updateEventDescriptionEmpty() throws Exception {
        String eventDescription = "";

        eventLogic.updateEventDescription(userName, eventId, eventDescription);

        Event event = eventLogic.getEvent(eventId);
        if(!event.getEventDescription().equals(eventDescription))
            Assert.fail("Description was not updated");
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventDescriptionTooLong() throws Exception {
        String eventDescription = createString(Event.MAX_DESCRITION_LENGTH + 1);

        eventLogic.updateEventDescription(userName, eventId, eventDescription);
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventDescriptionOnNotExistingEvent() throws Exception {
        String eventDescription = createString(50);

        eventLogic.updateEventDescription(userName, 10000, eventDescription);
    }

    // Event location
    @Test
    public void updateEventLocation() throws Exception {
        int newLocationId = createLocation(locationLogic, userName, "updated event", "updated description");

        eventLogic.updateEventLoction(userName, eventId, newLocationId);

        Event event = eventLogic.getEvent(eventId);
        if(event.getLocation().getLocationId() != newLocationId)
            Assert.fail("Location was not updated");
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventLocationOnNotExistingEvent() throws Exception {
        int newLocationId = createLocation(locationLogic, userName, "updated event", "updated description");

        eventLogic.updateEventLoction(userName, 10000, newLocationId);
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventLocationWithNonExistingLocation() throws Exception {
        int newLocationId = 10000;
        eventLogic.updateEventLoction(userName, eventId, newLocationId);
    }

    // Event Start time
    @Test
    public void updateEventStartTime() throws Exception {
        long startTime = System.currentTimeMillis() + 1000;

        /*
        In der Datenbank werden keine Millisekunden gespeichert. Zum Vergleichen der Zeit müssen also
        die Millisekunden ignoriert werden.
         */
        startTime = 1000 * (startTime / 1000);

        eventLogic.updateEventTimeStart(userName, eventId, new Date(startTime));

        Event event = eventLogic.getEvent(eventId);
        if(event.getStartDate().getTime() != startTime)
            Assert.fail("Time start was not updated");
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventStartTimeTooEarly() throws Exception {
        long startTime = System.currentTimeMillis() - 1000;

        eventLogic.updateEventTimeStart(userName, eventId, new Date(startTime));
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventStartTimeTooLate() throws Exception {
        long startTime = System.currentTimeMillis() + 10000000;

        eventLogic.updateEventTimeStart(userName, eventId, new Date(startTime));
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventStartTimeOnNonExistingEvent() throws Exception {
        long startTime = System.currentTimeMillis() + 1000;

        eventLogic.updateEventTimeStart(userName, 100000, new Date(startTime));
    }

    // Event end time
    @Test
    public void updateEventEndTime() throws Exception {
        long endTime = System.currentTimeMillis() + 10000;

        /*
        In der Datenbank werden keine Millisekunden gespeichert. Zum Vergleichen der Zeit müssen also
        die Millisekunden ignoriert werden.
         */
        endTime = 1000 * (endTime / 1000);

        eventLogic.updateEventTimeEnd(userName, eventId, new Date(endTime));

        Event event = eventLogic.getEvent(eventId);
        if(event.getEndDate().getTime() != endTime)
            Assert.fail("Time end was not updated");
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventEndTimeTooEarly() throws Exception {
        long endTime = System.currentTimeMillis() - 1000;

        eventLogic.updateEventTimeEnd(userName, eventId, new Date(endTime));
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventEndTimeOnNonExistingEvent() throws Exception {
        long endTime = System.currentTimeMillis() + 1000000;

        eventLogic.updateEventTimeEnd(userName, 100000, new Date(endTime));
    }

    // ------------------------- INVITE FRIEND ------------------------------

    @Test
    public void test1InviteMaxUsernameLength() throws Exception {
        String userName = createString(50);
        String toInviteUsername = createString(50);
        int eventId = 1;

        eventLogic.inviteFriend(userName, toInviteUsername, eventId);
    }

    @Test (expected = HttpRequestException.class)
    public void test2InviteInvalidUsername() throws Exception {
        String userName = createString(51);
        String toInviteUsername = createString(50);
        int eventId = 1;

        eventLogic.inviteFriend(userName, toInviteUsername, eventId);
    }

    @Test (expected = HttpRequestException.class)
    public void test3InviteEmptyUsername() throws Exception {
        String userName = createString(0);
        String toInviteUsername = createString(50);
        int eventId = 1;

        eventLogic.inviteFriend(userName, toInviteUsername, eventId);
    }

    @Test (expected = HttpRequestException.class)
    public void test4InviteInvalidToInviteUsername() throws Exception {
        String userName = createString(50);
        String toInviteUsername = createString(51);
        int eventId = 1;

        eventLogic.inviteFriend(userName, toInviteUsername, eventId);
    }

    @Test (expected = HttpRequestException.class)
    public void test5InviteEmptyToInviteUsername() throws Exception {
        String userName = createString(50);
        String toInviteUsername = createString(0);
        int eventId = 1;

        eventLogic.inviteFriend(userName, toInviteUsername, eventId);
    }

    // ------------------------- SEND INVITATION ------------------------------

    @Test
    public void test1SendInvitation() throws Exception {

        String myUsername = createString(50);
        String userToInvite = createString(50);

        eventLogic.inviteFriend(myUsername, userToInvite, 1);

    }

    @Test (expected = HttpRequestException.class)
    public void test2SendInvitationEmptyUsername() throws Exception {

        String myUsername = createString(0);
        String userToInvite = createString(50);

        eventLogic.inviteFriend(myUsername, userToInvite, 1);

    }

    @Test (expected = HttpRequestException.class)
    public void test3SendInvitationInvalidUsername() throws Exception {

        String myUsername = createString(51);
        String userToInvite = createString(50);

        eventLogic.inviteFriend(myUsername, userToInvite, 1);
    }

    @Test (expected = HttpRequestException.class)
    public void test4SendInvitationEmptyToInvitedUsername() throws Exception {

        String myUsername = createString(50);
        String userToInvite = createString(0);

        eventLogic.inviteFriend(myUsername, userToInvite, 1);

    }

    @Test (expected = HttpRequestException.class)
    public void test5SendInvitationInvalidToInvitedUsername() throws Exception {

        String myUsername = createString(50);
        String userToInvite = createString(51);

        eventLogic.inviteFriend(myUsername, userToInvite, 1);

    }

    // ------------------------- INVITE FRIEND ------------------------------





}