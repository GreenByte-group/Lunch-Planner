package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.user.UserLogic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.List;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.event.Utils.createEvent;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
public class EventLogicTest {

    @Autowired
    private EventLogic eventLogic;

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
                timeStart, timeEnd);

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
        timeStart, timeEnd);
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
                timeStart, timeEnd);
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
                timeStart, timeEnd);
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
              timeStart, timeEnd);
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
               timeStart, timeEnd);
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
           timeStart, timeEnd);
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
           timeStart, timeEnd);
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
           timeStart, timeEnd);
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

    @Autowired
    private UserLogic userLogic;


    // Event Name
    @Test
    public void updateEventName() throws Exception {
        //TODO create location and get id

        String eventName = createString(50);

        String userName = group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, 1);

        eventLogic.updateEventName(userName, eventId, eventName);

        //TODO check ob die daten auch geändert wurden
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventNameEmpty() throws Exception {
        //TODO create location and get id

        String eventName = "";

        String userName = createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, 1);

        eventLogic.updateEventName(userName, eventId, eventName);
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventNameTooLong() throws Exception {
        //TODO create location and get id

        String eventName = createString(51);

        String userName = createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, 1);

        eventLogic.updateEventName(userName, eventId, eventName);
    }

    // Event Description
    @Test
    public void updateEventDescription() throws Exception {
        //TODO create location and get id

        String eventDescription = createString(Event.MAX_DESCRITION_LENGTH);

        String userName = group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, 1);

        eventLogic.updateEventDescription(userName, eventId, eventDescription);

        //TODO check ob die daten auch geändert wurden
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventDescriptionEmpty() throws Exception {
        //TODO create location and get id

        String eventDescription = "";

        String userName = createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, 1);

        eventLogic.updateEventDescription(userName, eventId, eventDescription);
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventDescriptionTooLong() throws Exception {
        //TODO create location and get id

        String eventDescription = createString(Event.MAX_DESCRITION_LENGTH + 1);

        String userName = createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, 1);

        eventLogic.updateEventDescription(userName, eventId, eventDescription);
    }

    // Event location
    @Test
    public void updateEventLocation() throws Exception {
        //TODO create location and get id
        int locationId = 1;

        String userName = createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, 1);

        eventLogic.updateEventLoction(userName, eventId, locationId);

        //TODO check ob die daten auch geändert wurden
    }

    // Event Start time
    @Test
    public void updateEventStartTime() throws Exception {
        //TODO create location and get id
        int locationId = 1;

        String userName = createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, locationId);

        long startTime = System.currentTimeMillis() + 1000;

        eventLogic.updateEventTimeStart(userName, eventId, new Date(startTime));

        //TODO check ob die daten auch geändert wurden
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventStartTimeTooEarly() throws Exception {
        //TODO create location and get id
        int locationId = 1;

        String userName = createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, locationId);

        long startTime = System.currentTimeMillis() - 1000;

        eventLogic.updateEventTimeStart(userName, eventId, new Date(startTime));
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventStartTimeTooLate() throws Exception {
        //TODO create location and get id
        int locationId = 1;

        String userName = createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, locationId);

        long startTime = System.currentTimeMillis() + 10000000;

        eventLogic.updateEventTimeStart(userName, eventId, new Date(startTime));
    }

    // Event end time
    @Test
    public void updateEventEndTime() throws Exception {
        //TODO create location and get id
        int locationId = 1;

        String userName = createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, locationId);

        long endTime = System.currentTimeMillis() + 100000000;

        eventLogic.updateEventTimeEnd(userName, eventId, new Date(endTime));

        //TODO check ob die daten auch geändert wurden
    }

    @Test(expected = HttpRequestException.class)
    public void updateEventEndTimeTooEarly() throws Exception {
        //TODO create location and get id
        int locationId = 1;

        String userName = createUserIfNotExists(userLogic, "dummy");
        int eventId = createEvent(eventLogic, userName, locationId);

        long endTime = System.currentTimeMillis() - 1000;

        eventLogic.updateEventTimeEnd(userName, eventId, new Date(endTime));
    }
}