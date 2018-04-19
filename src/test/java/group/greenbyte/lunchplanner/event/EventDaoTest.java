package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.location.LocationLogic;
import group.greenbyte.lunchplanner.user.UserLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.event.Utils.createEvent;
import static group.greenbyte.lunchplanner.location.Utils.createLocation;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = AppConfig.class)
@ActiveProfiles("application-test.properties")
public class EventDaoTest {

    @Autowired
    private EventDao eventDao;

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
    }

    // ------------------------- CREATE EVENT ------------------------------

    @Test
    public void test1insertEventLongDescription() throws Exception {
        String userName = createString(50);
        String eventName = createString(50);
        String description = createString(1000);
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        Event result = eventDao.insertEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date(timeEnd));

        if(!(
                result.getEventName().equals(eventName) &&
                result.getEventDescription().equals(description) &&
                //result.getLocation().getLocationId() == locationId &&
                result.getStartDate().equals(new Date(timeStart)) &&
                result.getEndDate().equals(new Date(timeEnd)))) {
            Assert.fail("Event has not the right data");
        }
    }

    @Test(expected = DatabaseException.class)
    public void test2insertEventTooLongUserName() throws Exception {
        String userName = createString(51);
        String eventName = createString(50);
        String description = "";

        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        Event result = eventDao.insertEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date(timeEnd));
    }

    @Test(expected = DatabaseException.class)
    public void test3insertEventTooLongEventName() throws Exception {
        String userName = createString(50);
        String eventName = createString(51);
        String description = "";
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        Event result = eventDao.insertEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date(timeEnd));
    }

    @Test(expected = DatabaseException.class)
    public void test4insertEventTooLongDescription() throws Exception {
        String userName = createString(50);
        String eventName = createString(50);
        String description = createString(1001);
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        Event result = eventDao.insertEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date(timeEnd));
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
        int newLocationId = createLocation(locationLogic, userName, "updated location", "update");

        eventDao.updateEventLocation(eventId, newLocationId);

        Event event = eventDao.getEvent(eventId);
        if(event.getLocation().getLocationId() != newLocationId)
            Assert.fail("Location was not updated");
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

    // ------------------------- PUT USER INVITE TO EVENT ------------------------------

    @Test
    public void test1inviteMaxLengthToInviteUsername() throws Exception {
        // Database Exception da bei getEventById immer null zurück kommt, weil noch kein Eventobjekt in DB gespeichert ist
        // alle anderen Tests schlagen fehl, weil eine DB Exception geworfen wird und damit der Statuscode 400 verbunden ist.
        //EventJson event = new EventJson("dummy", "description", 1, System.currentTimeMillis()+1000, System.currentTimeMillis()+2000);

        int eventId = 1;

        String toInviteUsername = createString(50);

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
    public void test3ReplyInvitationNoUserName() throws Exception {
        String userName = "";
        int eventId = 1;

        eventDao.replyInvitation(userName, eventId, InvitationAnswer.REJECT);

    }

    @Test (expected = DatabaseException.class)
    public void test4ReplyInvitationNoUserNameTooLong() throws Exception {
        String userName = createString(51);
        int eventId = 1;

        eventDao.replyInvitation(userName, eventId, InvitationAnswer.REJECT);

    }

    @Test (expected = DatabaseException.class)
    public void test5ReplyInvitationAnswerNull() throws Exception {
        String userName = createString(50);
        int eventId = 1;

        eventDao.replyInvitation(userName, eventId, null);

    }

}