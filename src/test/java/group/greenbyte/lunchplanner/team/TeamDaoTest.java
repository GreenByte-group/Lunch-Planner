package group.greenbyte.lunchplanner.team;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.EventLogic;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.location.LocationLogic;
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
import org.springframework.transaction.annotation.Transactional;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.event.Utils.createEvent;
import static group.greenbyte.lunchplanner.location.Utils.createLocation;
import static group.greenbyte.lunchplanner.team.Utils.createTeamWithoutParent;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = AppConfig.class)
@ActiveProfiles("application-test.properties")
@Transactional
public class TeamDaoTest {

    @Autowired
    private TeamDao teamDao;

    @Autowired
    private TeamLogic teamLogic;

    @Autowired
    private EventLogic eventLogic;

    @Autowired
    private UserLogic userLogic;

    @Autowired
    private LocationLogic locationLogic;

    private String userName;
    private int locationId;
    private int eventId;
    private int parent;

    @Before
    public void setUp() throws Exception {
        userName = createUserIfNotExists(userLogic, "dummy");
        locationId = createLocation(locationLogic, userName, "Test location", "test description");
        eventId = createEvent(eventLogic, userName, locationId);
        parent = createTeamWithoutParent(teamLogic, userName, createString(10), createString(10));
    }

    @Test
    public void test1InsertTeamWithNoDescription() throws Exception {
        String teamName = "A";
        String description = "";

        teamDao.insertTeamWithParent(teamName, description, userName, parent);
    }

    @Test
    public void test2InsertTeamWithMaxAdminNameMaxTeamNameNormalDescription() throws Exception {
        String adminName = createUserIfNotExists(userLogic, createString(50));
        String teamName = createString(50);
        String description = "Super Team";

        teamDao.insertTeamWithParent(teamName, description, adminName, parent);
    }

    @Test
    public void test3InsertTeamWithMaxDescription() throws Exception {
        String adminName = createUserIfNotExists(userLogic, createString(50));
        String teamName = createString(50);
        String description = createString(1000);

        teamDao.insertTeamWithParent(teamName, description, adminName, parent);
    }

    @Test(expected = DatabaseException.class)
    public void test4InsertTeamWithNoAdminName() throws Exception {
        String adminName = "";
        String teamName = createString(50);
        String description = createString(1000);

        teamDao.insertTeamWithParent(teamName, description, adminName, parent);
    }

    @Test(expected = DatabaseException.class)
    public void test5InsertTeamAdminNameTooLong() throws Exception {
        String adminName = createString(51);
        String teamName = createString(50);
        String description = createString(1000);

        teamDao.insertTeamWithParent(teamName, description, adminName, parent);
    }

    @Test(expected = DatabaseException.class)
    public void test7InsertTeamTeamNameTooLong() throws Exception {
        String adminName = createUserIfNotExists(userLogic, createString(50));
        String teamName = createString(51);
        String description = createString(1000);

        teamDao.insertTeamWithParent(teamName, description, adminName, parent);
    }

    @Test(expected = DatabaseException.class)
    public void test8InsertTeamDescriptionTooLong() throws Exception {
        String adminName = createUserIfNotExists(userLogic, createString(50));
        String teamName = createString(50);
        String description = createString(1001);

        teamDao.insertTeamWithParent(teamName, description, adminName, parent);
    }

    // ------------------ PUT USER TEAM MEMBER ------------------------

    @Test
    public void test1PutUserTeamMemberWithMinLength() throws Exception {
       String userToInviteName = createUserIfNotExists(userLogic, createString(1));

       teamDao.addUserToTeam(parent, userToInviteName);
    }

    @Test
    public void test2PutUserTeamMemberWithMaxLength() throws Exception {
        String userToInviteName = createUserIfNotExists(userLogic, createString(50));

        teamDao.addUserToTeam(parent, userToInviteName);
    }

    @Test(expected = DatabaseException.class)
    public void test3PutUserTeamMemberUserToInviteTooLong() throws Exception {
        String userToInviteName = createUserIfNotExists(userLogic, createString(51));

        teamDao.addUserToTeam(parent, userToInviteName);
    }

    @Test(expected = DatabaseException.class)
    public void test4PutUserTeamMemberWithNoUserToInvite() throws Exception {
        String userToInviteName = createUserIfNotExists(userLogic, createString(0));

        teamDao.addUserToTeam(parent, userToInviteName);
    }




}