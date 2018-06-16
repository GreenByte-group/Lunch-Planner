package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.team.TeamLogic;
import group.greenbyte.lunchplanner.user.UserLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.event.Utils.createEvent;
import static group.greenbyte.lunchplanner.event.Utils.setEventPublic;
import static group.greenbyte.lunchplanner.team.Utils.createTeamWithoutParent;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = AppConfig.class)
@ActiveProfiles("application.properties")

@Transactional
public class EventDaoTest {

    @Autowired
    private EventDao eventDao;

    @Autowired
    private TeamLogic teamLogic;

    @Autowired
    private EventLogic eventLogic;

    @Autowired
    private UserLogic userLogic;

    private String userName;
    private String location = "Location";
    private int eventId;

    private int teamId;

    private String eventName;
    private String eventDescription;
    private long eventTimeStart;
    private long eventTimeEnd;

    @Before
    public void setUp() throws Exception {
        eventName = createString(10);
        eventDescription = createString(10);
        eventTimeStart = System.currentTimeMillis() + 10000;
        eventTimeEnd = eventTimeStart + 10000;

        // ohne millisekunden
        eventTimeStart = 1000 * (eventTimeStart / 1000);
        eventTimeEnd = 1000 * (eventTimeEnd / 1000);

        userName = createUserIfNotExists(userLogic, "dummy");
        eventId = createEvent(eventLogic, userName, eventName, eventDescription, location,
                new Date(eventTimeStart));

        teamId = createTeamWithoutParent(teamLogic, userName, "name", "description");
    }

    // ------------------------- CREATE EVENT ------------------------------

    @Test
    public void test1insertEventLongDescription() throws Exception {
        String eventName = createString(255);
        String description = createString(1000);
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        //ohne millisekunden
        timeStart = 1000 * (timeStart / 1000);
        timeEnd = 1000 * (timeEnd / 1000);

        Event result = eventDao.insertEvent(userName, eventName, description, location,
                new Date(timeStart), false);

        if(!(
                result.getEventName().equals(eventName) &&
                result.getEventDescription().equals(description) &&
                result.getLocation().equals(location) &&
                result.getStartDate().getTime() == timeStart) ) {
            Assert.fail("Event has not the right data");
        }
    }

    @Test(expected = DatabaseException.class)
    public void test2insertEventTooLongUserName() throws Exception {
        String userName = createString(51);
        String eventName = createString(255);
        String description = "";

        long timeStart = System.currentTimeMillis() + 10000;

        Event result = eventDao.insertEvent(userName, eventName, description, location,
                new Date(timeStart), false);
    }

    @Test(expected = DatabaseException.class)
    public void test3insertEventTooLongEventName() throws Exception {
        String userName = createString(50);
        String eventName = createString(256);
        String description = "";
        long timeStart = System.currentTimeMillis() + 10000;

        Event result = eventDao.insertEvent(userName, eventName, description, location,
                new Date(timeStart), false);
    }

    @Test(expected = DatabaseException.class)
    public void test4insertEventTooLongDescription() throws Exception {
        String userName = createString(50);
        String eventName = createString(255);
        String description = createString(1001);
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        Event result = eventDao.insertEvent(userName, eventName, description, location,
                new Date(timeStart), false);
    }

    // ---------------- UPDATE EVENT ----------------------

    // Event name
    @Test
    public void updateEventName() throws Exception {
        String eventName = createString(255);

        eventDao.updateEventName(eventId, eventName);

        Event event = eventDao.getEvent(eventId);
        if(!event.getEventName().equals(eventName))
            Assert.fail("Name was not updated");
    }

    @Test(expected = DatabaseException.class)
    public void updateEventNameTooLong() throws Exception {
        String eventName = createString(256);

        eventDao.updateEventName(eventId, eventName);
    }

    // Event description
    @Test
    public void updateEventDescription() throws Exception {
        String eventDescription = createString(Event.MAX_DESCRITION_LENGTH);

        eventDao.updateEventDescription(eventId, eventDescription);

        Event event = eventDao.getEvent(eventId);
        if(!event.getEventDescription().equals(eventDescription))
            Assert.fail("Description was not updated");
    }

    @Test(expected = DatabaseException.class)
    public void updateEventDescriptionTooLong() throws Exception {
        String eventName = createString(Event.MAX_DESCRITION_LENGTH + 1);

        eventDao.updateEventDescription(eventId, eventName);
    }

    // Event location
    @Test
    public void updateEventLocation() throws Exception {
        String newLocation = "new Location";

        eventDao.updateEventLocation(eventId, newLocation);

        Event event = eventDao.getEvent(eventId);

        if(!event.getLocation().equals(newLocation))
            Assert.fail("Location was not updated");
    }

    @Test(expected = DatabaseException.class)
    public void updateEventLocationNotVaildLocation() throws Exception {
        eventDao.updateEventLocation(eventId, createString(256));
    }

    // Event start time
    @Test
    public void updateEventStartTime() throws Exception {
        long timeStart = System.currentTimeMillis() + 1000;

         /*
        In der Datenbank werden keine Millisekunden gespeichert. Zum Vergleichen der Zeit müssen also
        die Millisekunden ignoriert werden.
         */
        timeStart = 1000 * (timeStart / 1000);

        eventDao.updateEventTimeStart(eventId, new Date(timeStart));

        Event event = eventDao.getEvent(eventId);
        if(event.getStartDate().getTime() != timeStart)
            Assert.fail("Time start was not updated");
    }

    // ------------------------- PUT USER INVITE TO EVENT ------------------------------

    @Test
    public void test1inviteMaxLengthToInviteUsername() throws Exception {
        // Database Exception da bei getEventById immer null zurück kommt, weil noch kein Eventobjekt in DB gespeichert ist
        // alle anderen Tests schlagen fehl, weil eine DB Exception geworfen wird und damit der Statuscode 400 verbunden ist.
        //EventJson event = new EventJson("dummy", "description", 1, System.currentTimeMillis()+1000, System.currentTimeMillis()+2000);

        String toInviteUsername = createUserIfNotExists(userLogic, createString(50));

        Event result = eventDao.putUserInviteToEvent(toInviteUsername, eventId);

    }

    @Test(expected = DatabaseException.class)
    public void test2inviteInvalidToInviteUsername() throws Exception {
        int eventId = 1;
        String toInviteUsername = createString(51);

        Event result = eventDao.putUserInviteToEvent(toInviteUsername, eventId);
    }

    @Test(expected = DatabaseException.class)
    public void test3inviteEmptyToInviteUsername() throws Exception {
        int eventId = 1;
        String toInviteUsername = createString(0);

        Event result = eventDao.putUserInviteToEvent(toInviteUsername, eventId);
    }

    // -------------------- GET EVENT --------------------------
    @Test
    public void test1GetEvent() throws Exception {
        Event event = eventDao.getEvent(eventId);
        Assert.assertEquals(eventName, event.getEventName());
        Assert.assertEquals(eventDescription, event.getEventDescription());
        Assert.assertEquals((int) eventId, (int) event.getEventId());
        Assert.assertEquals(location, event.getLocation());
        Assert.assertEquals(new Date(eventTimeStart), event.getStartDate());
    }

    @Test
    public void test2GetEventNull() throws Exception {
        assertNull(eventDao.getEvent(eventId + 1000));
    }



    // ---------------------- SEARCH EVENT -------------------------------
    //all public
    @Test
    public void test1SearchPublicEventsShouldBeZero() throws Exception {
        String searchWord = createString(255);
        List<Event> events = eventDao.findPublicEvents(searchWord);
        Assert.assertEquals(0, events.size());
    }
    @Test
    public void test2SearchPublicEvents() throws Exception {
        String newEventName = createString(255);
        int publicEventId = createEvent(eventLogic, userName, newEventName, eventDescription, location, new Date(eventTimeStart));
        setEventPublic(eventDao, publicEventId);
        String searchWord = newEventName;
        List<Event> events = eventDao.findPublicEvents(searchWord);
        Assert.assertEquals(1, events.size());
    }

    //all for teams
    @Test
    public void test1SearchForTeam() throws Exception {
        int teamId = createTeamWithoutParent(teamLogic, userName, createString(10), createString(10));

        String newEventName = createString(255);
        int newEventId = createEvent(eventLogic, userName, newEventName, createString(50), location, new Date(eventTimeStart));
        eventDao.addTeamToEvent(newEventId, teamId);

        List<Event> events = eventDao.findEventsForTeam(teamId, newEventName);
        Assert.assertEquals((int) events.get(0).getEventId(), newEventId);
    }

    @Test
    public void test1SearchForTeam2() throws Exception {
        int teamId = createTeamWithoutParent(teamLogic, userName, createString(10), createString(10));

        String newEventName = createString(255);
        int newEventId = createEvent(eventLogic, userName, newEventName, createString(50), location, new Date(eventTimeStart));
        eventDao.addTeamToEvent(newEventId, teamId);

        List<Event> events = eventDao.findEventsForTeam(teamId, newEventName);
        Assert.assertEquals(1, events.size());
        Assert.assertEquals(newEventId, (int) events.get(0).getEventId());
    }

    // ------------------------- Reply Invitation ------------------------------

    @Test
    public void test1ReplyInvitationAccept() throws Exception {
        eventDao.replyInvitation(userName, eventId, InvitationAnswer.ACCEPT);
    }

    @Test
    public void test2ReplyInvitationRejectMaxUsername() throws Exception {
        String userName = createString(50);
        int eventId = 1;

        eventDao.replyInvitation(userName, eventId, InvitationAnswer.REJECT);
    }

    @Test (expected = DatabaseException.class)
    public void test5ReplyInvitationAnswerNull() throws Exception {
        String userName = createString(50);
        int eventId = 1;

        eventDao.replyInvitation(userName, eventId, null);

    }

    // -------------------- Add Team to Event --------------------------
    @Test(expected = DatabaseException.class)
    public void test1AddTeamNotExistingTeam() throws Exception {
        eventDao.addTeamToEvent(eventId, 10000);
    }

    @Test(expected = DatabaseException.class)
    public void test1AddTeamNotExistingEvent() throws Exception {
        eventDao.addTeamToEvent(10000, teamId);
    }

    // ---------------- Delete Event ---------------------
    @Test
    public void test1DeleteEvent() throws Exception{
        int eventId = createEvent(eventLogic, userName, "location");

        eventDao.deleteEvent(eventId);

        assertNull(eventDao.getEvent(eventId));
    }

}