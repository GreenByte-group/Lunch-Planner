package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.user.UserLogic;
import org.junit.Assert;
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
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = AppConfig.class)
@ActiveProfiles("application-test.properties")
public class EventDaoTest {

    @Autowired
    private EventDao eventDao;

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

    @Autowired
    private EventLogic eventLogic;

    @Autowired
    private UserLogic userLogic;

    // Event name
    @Test
    public void updateEventName() throws Exception {
        //TODO create location and get id

        String eventName = createString(50);

        String userName = createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, 1);

        eventDao.updateEventName(eventId, eventName);

        //TODO check ob die daten auch geändert wurden
    }

    @Test(expected = DatabaseException.class)
    public void updateEventNameTooLong() throws Exception {
        //TODO create location and get id

        String eventName = createString(51);

        String userName = createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, 1);

        eventDao.updateEventName(eventId, eventName);
    }

    // Event description
    @Test
    public void updateEventDescription() throws Exception {
        //TODO create location and get id

        String eventDescription = createString(Event.MAX_DESCRITION_LENGTH);

        String userName = createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, 1);

        eventDao.updateEventDescription(eventId, eventDescription);

        //TODO check ob die daten auch geändert wurden
    }

    @Test(expected = DatabaseException.class)
    public void updateEventDescriptionTooLong() throws Exception {
        //TODO create location and get id

        String eventName = createString(Event.MAX_DESCRITION_LENGTH + 1);

        String userName = createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, 1);

        eventDao.updateEventDescription(eventId, eventName);
    }

    // Event location
    @Test
    public void updateEventLocation() throws Exception {
        //TODO create location and get id

        int locationId = 1;

        String userName = createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, 2);

        eventDao.updateEventLocation(eventId, locationId);

        //TODO check ob die daten auch geändert wurden
    }

    // Event start time
    @Test
    public void updateEventStartTime() throws Exception {
        //TODO create location and get id

        long timeStart = System.currentTimeMillis() + 1000;

        String userName = createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, 1);

        eventDao.updateEventTimeEnd(eventId, new Date(timeStart));

        //TODO check ob die daten auch geändert wurden
    }

    // Event end time
    @Test
    public void updateEventEndTime() throws Exception {
        //TODO create location and get id

        long timeEnd = System.currentTimeMillis() + 10000;

        String userName = createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, 1);

        eventDao.updateEventTimeEnd(eventId, new Date(timeEnd));

        //TODO check ob die daten auch geändert wurden
    }
}