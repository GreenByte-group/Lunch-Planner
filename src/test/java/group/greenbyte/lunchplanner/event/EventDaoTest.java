package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
<<<<<<< HEAD
import group.greenbyte.lunchplanner.location.LocationLogic;
=======
import group.greenbyte.lunchplanner.team.TeamLogic;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
import group.greenbyte.lunchplanner.user.UserLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
<<<<<<< HEAD
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
=======
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
<<<<<<< HEAD

import java.util.Date;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.event.Utils.createEvent;
import static group.greenbyte.lunchplanner.location.Utils.createLocation;
=======
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.event.Utils.createEvent;
import static group.greenbyte.lunchplanner.event.Utils.setEventPublic;
import static group.greenbyte.lunchplanner.team.Utils.createTeamWithoutParent;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = AppConfig.class)
@ActiveProfiles("application-test.properties")
<<<<<<< HEAD
=======
@Transactional
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
public class EventDaoTest {

    @Autowired
    private EventDao eventDao;

    @Autowired
<<<<<<< HEAD
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
=======
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
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    }

    // ------------------------- CREATE EVENT ------------------------------

    @Test
    public void test1insertEventLongDescription() throws Exception {
<<<<<<< HEAD
        String userName = createString(50);
        String eventName = createString(50);
        String description = createString(1000);
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        Event result = eventDao.insertEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date(timeEnd));
=======
        String eventName = createString(50);
        String description = createString(1000);
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        //ohne millisekunden
        timeStart = 1000 * (timeStart / 1000);
        timeEnd = 1000 * (timeEnd / 1000);

        Event result = eventDao.insertEvent(userName, eventName, description, location,
                new Date(timeStart));
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

        if(!(
                result.getEventName().equals(eventName) &&
                result.getEventDescription().equals(description) &&
<<<<<<< HEAD
                //result.getLocation().getLocationId() == locationId &&
                result.getStartDate().equals(new Date(timeStart)) &&
                result.getEndDate().equals(new Date(timeEnd)))) {
=======
                result.getLocation().equals(location) &&
                result.getStartDate().getTime() == timeStart) ) {
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
            Assert.fail("Event has not the right data");
        }
    }

    @Test(expected = DatabaseException.class)
    public void test2insertEventTooLongUserName() throws Exception {
        String userName = createString(51);
        String eventName = createString(50);
        String description = "";

<<<<<<< HEAD
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        Event result = eventDao.insertEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date(timeEnd));
=======
        long timeStart = System.currentTimeMillis() + 10000;

        Event result = eventDao.insertEvent(userName, eventName, description, location,
                new Date(timeStart));
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    }

    @Test(expected = DatabaseException.class)
    public void test3insertEventTooLongEventName() throws Exception {
        String userName = createString(50);
        String eventName = createString(51);
        String description = "";
<<<<<<< HEAD
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        Event result = eventDao.insertEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date(timeEnd));
=======
        long timeStart = System.currentTimeMillis() + 10000;

        Event result = eventDao.insertEvent(userName, eventName, description, location,
                new Date(timeStart));
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    }

    @Test(expected = DatabaseException.class)
    public void test4insertEventTooLongDescription() throws Exception {
        String userName = createString(50);
        String eventName = createString(50);
        String description = createString(1001);
<<<<<<< HEAD
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        Event result = eventDao.insertEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date(timeEnd));
=======
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        Event result = eventDao.insertEvent(userName, eventName, description, location,
                new Date(timeStart));
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    }

    // ---------------- UPDATE EVENT ----------------------

    // Event name
    @Test
    public void updateEventName() throws Exception {
        String eventName = createString(50);

        eventDao.updateEventName(eventId, eventName);

        Event event = eventDao.getEvent(eventId);
        if(!event.getEventName().equals(eventName))
            Assert.fail("Name was not updated");
    }

    @Test(expected = DatabaseException.class)
    public void updateEventNameTooLong() throws Exception {
        String eventName = createString(51);

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
<<<<<<< HEAD
        int newLocationId = createLocation(locationLogic, userName, "updated location", "update");

        eventDao.updateEventLocation(eventId, newLocationId);

        Event event = eventDao.getEvent(eventId);
        if(event.getLocation().getLocationId() != newLocationId)
            Assert.fail("Location was not updated");
    }

=======
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

>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
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

<<<<<<< HEAD
    // Event end time
    @Test
    public void updateEventEndTime() throws Exception {
        long timeEnd = System.currentTimeMillis() + 10000;

        /*
        In der Datenbank werden keine Millisekunden gespeichert. Zum Vergleichen der Zeit müssen also
        die Millisekunden ignoriert werden.
         */
        timeEnd = 1000 * (timeEnd / 1000);

        eventDao.updateEventTimeEnd(eventId, new Date(timeEnd));

        Event event = eventDao.getEvent(eventId);
        if(event.getEndDate().getTime() != timeEnd)
            Assert.fail("Time end was not updated");
    }

=======
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    // ------------------------- PUT USER INVITE TO EVENT ------------------------------

    @Test
    public void test1inviteMaxLengthToInviteUsername() throws Exception {
        // Database Exception da bei getEventById immer null zurück kommt, weil noch kein Eventobjekt in DB gespeichert ist
        // alle anderen Tests schlagen fehl, weil eine DB Exception geworfen wird und damit der Statuscode 400 verbunden ist.
        //EventJson event = new EventJson("dummy", "description", 1, System.currentTimeMillis()+1000, System.currentTimeMillis()+2000);

<<<<<<< HEAD
        int eventId = 1;

        String toInviteUsername = createString(50);
=======
        String toInviteUsername = createUserIfNotExists(userLogic, createString(50));
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

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
<<<<<<< HEAD
=======

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
        Assert.assertNull(eventDao.getEvent(eventId + 1000));
    }



    // ---------------------- SEARCH EVENT -------------------------------
    //all public
    @Test
    public void test1SearchPublicEventsShouldBeZero() throws Exception {
        String searchWord = createString(50);
        List<Event> events = eventDao.findPublicEvents(searchWord);
        Assert.assertEquals(0, events.size());
    }
    @Test
    public void test2SearchPublicEvents() throws Exception {
        String newEventName = createString(50);
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

        String newEventName = createString(50);
        int newEventId = createEvent(eventLogic, userName, newEventName, createString(50), location, new Date(eventTimeStart));
        eventDao.addTeamToEvent(newEventId, teamId);

        List<Event> events = eventDao.findEventsForTeam(teamId, newEventName);
        Assert.assertEquals((int) events.get(0).getEventId(), newEventId);
    }

    @Test
    public void test1SearchForTeam2() throws Exception {
        int teamId = createTeamWithoutParent(teamLogic, userName, createString(10), createString(10));
        int teamId2 = createTeamWithoutParent(teamLogic, userName, createString(10), createString(10));

        String newEventName = createString(50);
        int newEventId = createEvent(eventLogic, userName, newEventName, createString(50), location, new Date(eventTimeStart));
        int newEventId2 = createEvent(eventLogic, userName, newEventName, createString(50), location, new Date(eventTimeStart));
        eventDao.addTeamToEvent(newEventId, teamId);
        eventDao.addTeamToEvent(newEventId2, teamId2);

        List<Event> events = eventDao.findEventsForTeam(teamId, newEventName);
        Assert.assertEquals(1, events.size());
        Assert.assertEquals(newEventId, (int) events.get(0).getEventId());
    }

    // ------------------------- Reply Invitation ------------------------------

    @Test
    public void test1ReplyInvitationAccept() throws Exception {
        String userName = "A";
        int eventId = 1;

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

>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
}