package group.greenbyte.lunchplanner.location;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.EventLogic;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.location.database.Coordinate;
import group.greenbyte.lunchplanner.user.UserLogic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import sun.reflect.annotation.ExceptionProxy;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.event.Utils.createEvent;
import static group.greenbyte.lunchplanner.location.Utils.createLocation;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = AppConfig.class)
@ActiveProfiles("application-test.properties")
public class LocationDaoTest {

    @Autowired
    private LocationDao locationDao;

    @Autowired
    private EventLogic eventLogic;

    @Autowired
    private UserLogic userLogic;

    @Autowired
    private LocationLogic locationLogic;

    private String userName;

    @Before
    public void setUp() throws Exception {
        userName = createUserIfNotExists(userLogic, "dummy");
    }

    // ---------------------------- INSERT LOCATION ---------------------------

    @Test
    public void test1InsertLocationNoDescription() throws Exception {
        String locationName = "A";
        String locationDescription = "";
        Coordinate coordinate = new Coordinate(1.0, 1.0);

        int result = locationDao.insertLocation(locationName, coordinate,
                locationDescription, userName);
    }

    @Test
    public void test2InsertLocationLongLocationName() throws Exception {
        String locationName = createString(50);
        String locationDescription = "Super Location";
        Coordinate coordinate = new Coordinate(1.0, 1.0);

        int result = locationDao.insertLocation(locationName, coordinate,
                locationDescription, userName);
    }

    @Test
    public void test3InsertLocationLongDescription() throws Exception {
        String locationName = "A";
        String locationDescription = createString(1000);
        Coordinate coordinate = new Coordinate(1.0, 1.0);

        int result = locationDao.insertLocation(locationName, coordinate,
                locationDescription, userName);
    }

    @Test(expected = DatabaseException.class)
    public void test4InsertLocationTooLongLocationName() throws Exception {
        String locationName = createString(51);
        String locationDescription = "";
        Coordinate coordinate = new Coordinate(1.0, 1.0);

        int result = locationDao.insertLocation(locationName, coordinate,
                locationDescription, userName);
    }

    @Test(expected = DatabaseException.class)
    public void test5InsertLocationTooLongDescription() throws Exception {
        String locationName = "A";
        String locationDescription = createString(1001);
        Coordinate coordinate = new Coordinate(1.0, 1.0);

        int result = locationDao.insertLocation(locationName, coordinate,
                locationDescription, userName);
    }

    @Test(expected = DatabaseException.class)
    public void test6InsertLocationTooLongAdminName() throws Exception {
        String locationName = "A";
        String locationDescription = "";
        Coordinate coordinate = new Coordinate(1.0, 1.0);
        String adminName = createUserIfNotExists(userLogic, createString(51));

        int result = locationDao.insertLocation(locationName, coordinate,
                locationDescription, adminName);
    }

    // ------------------ GET LOCATION ----------------------
    @Test
    public void test1GetLocation() throws Exception {
        int locationId = 1;

        locationDao.getLocation(locationId);
    }
}