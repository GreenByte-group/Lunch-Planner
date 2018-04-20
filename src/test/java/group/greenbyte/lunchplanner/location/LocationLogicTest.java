package group.greenbyte.lunchplanner.location;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.EventLogic;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.user.UserLogic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.event.Utils.createEvent;
import static group.greenbyte.lunchplanner.location.Utils.createLocation;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
public class LocationLogicTest {

    @Autowired
    private LocationLogic locationLogic;

    @Autowired
    private EventLogic eventLogic;

    @Autowired
    private UserLogic userLogic;

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
    public void test1CreateLocationWithNoDescription() throws Exception{
        String locationName = createString(1);
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;
        String description = "";

        locationLogic.createLocation(userName, locationName, xCoordinate, yCoordinate, description);
    }

    @Test
    public void test2CreateLocationWithMaxUserMaxLocationAndNormalDescription() throws Exception{
        String locationName = createString(50);
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;
        String description = "Super Location";

        locationLogic.createLocation(userName, locationName, xCoordinate, yCoordinate, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test3CreateLocationWithNoUserMaxLocationMaxDescription() throws Exception{
        String userName = "";
        String locationName = createString(50);
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;
        String description = createString(1000);

        locationLogic.createLocation(userName, locationName, xCoordinate, yCoordinate, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test4CreateLocationUserTooLong() throws Exception {
        String userName = createString(51);
        String locationName = createString(50);
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;
        String description = createString(1000);

        locationLogic.createLocation(userName, locationName, xCoordinate, yCoordinate, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test5CreateLocationWithNoLocation() throws Exception {
        String locationName = "";
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;
        String description = createString(1000);

        locationLogic.createLocation(userName, locationName, xCoordinate, yCoordinate, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test6CreateLocationLocationTooLong() throws Exception {
        String locationName = createString(51);
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;
        String description = createString(1000);

        locationLogic.createLocation(userName, locationName, xCoordinate, yCoordinate, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test7CreateDescriptionTooLong() throws Exception {
        String locationName = createString(50);
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;
        String description = createString(1001);

        locationLogic.createLocation(userName, locationName, xCoordinate, yCoordinate, description);
    }

    // ---------------------- GET LOCATION ----------------------
    @Test
    public void test1GetLocation() throws Exception {
        locationLogic.getLocation(userName, locationId);
    }

    @Test(expected = HttpRequestException.class)
    public void test2GetLocationNoUserRight() throws Exception {
        String userName = createUserIfNotExists(userLogic, createString(20));
        locationLogic.getLocation(userName, locationId);
    }

}