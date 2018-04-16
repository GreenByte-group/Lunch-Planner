package group.greenbyte.lunchplanner.location;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.location.database.Coordinate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import sun.reflect.annotation.ExceptionProxy;

import static group.greenbyte.lunchplanner.Utils.createString;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = AppConfig.class)
@ActiveProfiles("application-test.properties")
public class LocationDaoTest {

    @Autowired
    private LocationDao locationDao;

    // ---------------------------- INSERT LOCATION ---------------------------

    @Test
    public void test1InsertLocationNoDescription() throws Exception {
        String locationName = "A";
        String locationDescription = "";
        String adminName = createString(50);
        Coordinate coordinate = new Coordinate(1.0, 1.0);

        int result = locationDao.insertLocation(locationName, coordinate,
                locationDescription, adminName);
    }

    @Test
    public void test2InsertLocationLongLocationName() throws Exception {
        String locationName = createString(50);
        String locationDescription = "Super Location";
        String adminName = createString(50);
        Coordinate coordinate = new Coordinate(1.0, 1.0);

        int result = locationDao.insertLocation(locationName, coordinate,
                locationDescription, adminName);
    }

    @Test
    public void test3InsertLocationLongDescription() throws Exception {
        String locationName = "A";
        String locationDescription = createString(1000);
        String adminName = createString(50);
        Coordinate coordinate = new Coordinate(1.0, 1.0);

        int result = locationDao.insertLocation(locationName, coordinate,
                locationDescription, adminName);
    }

    @Test(expected = DatabaseException.class)
    public void test4InsertLocationTooLongLocationName() throws Exception {
        String locationName = createString(51);
        String locationDescription = "";
        String adminName = createString(50);
        Coordinate coordinate = new Coordinate(1.0, 1.0);

        int result = locationDao.insertLocation(locationName, coordinate,
                locationDescription, adminName);
    }

    @Test(expected = DatabaseException.class)
    public void test5InsertLocationTooLongDescription() throws Exception {
        String locationName = "A";
        String locationDescription = createString(1001);
        String adminName = createString(50);
        Coordinate coordinate = new Coordinate(1.0, 1.0);

        int result = locationDao.insertLocation(locationName, coordinate,
                locationDescription, adminName);
    }

    @Test(expected = DatabaseException.class)
    public void test6InsertLocationTooLongAdminName() throws Exception {
        String locationName = "A";
        String locationDescription = "";
        String adminName = createString(51);
        Coordinate coordinate = new Coordinate(1.0, 1.0);

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