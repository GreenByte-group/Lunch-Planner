package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
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
                new Date(timeStart), new Date (timeEnd));

        if(!(
                result.getEventName().equals(eventName) &&
                result.getEventDescription().equals(description) &&
                result.getLocationId() == locationId &&
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
                new Date(timeStart), new Date (timeEnd));
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
                new Date(timeStart), new Date (timeEnd));
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
                new Date(timeStart), new Date (timeEnd));
    }
}