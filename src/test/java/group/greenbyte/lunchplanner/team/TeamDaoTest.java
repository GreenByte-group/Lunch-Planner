package group.greenbyte.lunchplanner.team;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.EventLogic;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;


import group.greenbyte.lunchplanner.team.database.Team;

import group.greenbyte.lunchplanner.team.database.TeamMemberDataForReturn;
import group.greenbyte.lunchplanner.user.UserLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.event.Utils.createEvent;
import static group.greenbyte.lunchplanner.team.Utils.createTeamWithoutParent;
import static group.greenbyte.lunchplanner.team.Utils.setTeamPublic;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;
import static junit.framework.TestCase.fail;

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

    private String userName;
    private int eventId;
    private int parent;

    private String teamName;
    private String description;
    private int teamId;

    @Before
    public void setUp() throws Exception {
        userName = createUserIfNotExists(userLogic, "dummy");
        eventId = createEvent(eventLogic, userName, "Test location");
        parent = createTeamWithoutParent(teamLogic, userName, createString(10), createString(10));

        description = createString(50);
        teamName = createString(20);

        teamId = createTeamWithoutParent(teamLogic, userName, teamName, description);
    }

    @Test
    public void test1InsertTeamWithNoDescription() throws Exception {
        String teamName = "A";
        String description = "";

        teamDao.insertTeamWithParent(teamName, description, userName, true, parent);
    }

    @Test
    public void test2InsertTeamWithMaxAdminNameMaxTeamNameNormalDescription() throws Exception {
        String adminName = createUserIfNotExists(userLogic, createString(50));
        String teamName = createString(50);
        String description = "Super Team";

        teamDao.insertTeamWithParent(teamName, description, adminName, false, parent);
    }

    @Test
    public void test3InsertTeamWithMaxDescription() throws Exception {
        String adminName = createUserIfNotExists(userLogic, createString(50));
        String teamName = createString(50);
        String description = createString(1000);

        teamDao.insertTeamWithParent(teamName, description, adminName, true, parent);
    }

    @Test(expected = DatabaseException.class)
    public void test4InsertTeamWithNoAdminName() throws Exception {
        String adminName = "";
        String teamName = createString(50);
        String description = createString(1000);

        teamDao.insertTeamWithParent(teamName, description, adminName, true, parent);
    }

    @Test(expected = DatabaseException.class)
    public void test5InsertTeamAdminNameTooLong() throws Exception {
        String adminName = createString(51);
        String teamName = createString(50);
        String description = createString(1000);

        teamDao.insertTeamWithParent(teamName, description, adminName, true, parent);
    }

    @Test(expected = DatabaseException.class)
    public void test7InsertTeamTeamNameTooLong() throws Exception {
        String adminName = createUserIfNotExists(userLogic, createString(50));
        String teamName = createString(51);
        String description = createString(1000);

        teamDao.insertTeamWithParent(teamName, description, adminName, true, parent);
    }

    @Test(expected = DatabaseException.class)
    public void test8InsertTeamDescriptionTooLong() throws Exception {
        String adminName = createUserIfNotExists(userLogic, createString(50));
        String teamName = createString(50);
        String description = createString(1001);

        teamDao.insertTeamWithParent(teamName, description, adminName, true, parent);
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

    // ------------------ GET TEAM ------------------------

    @Test
    public void test1GetTeam() throws Exception {
        String adminName = createUserIfNotExists(userLogic, createString(50));
        String teamName = createString(50);
        String description = createString(1000);
        int teamId = teamDao.insertTeam(teamName, description, adminName, true);
        Team team = teamDao.getTeam(teamId);
        Assert.assertEquals(teamName, team.getTeamName());
        Assert.assertEquals(description, team.getDescription());
        Assert.assertEquals((int) teamId, (int) team.getTeamId());

    }

    @Test
    public void test2GetTeam() throws Exception {
        Assert.assertNull(teamDao.getTeam(teamId + 1000));
    }

    // ------------------ SEARCH TEAM ------------------------

    @Test
    public void test1FindPublicTeams() throws Exception {
        String searchWord = createString(50);
        List<Team> teams = teamDao.findPublicTeams(searchWord);
        Assert.assertEquals(0, teams.size());

    }

    @Test
    public void test2SearchPublicTeams() throws Exception {
        String newTeamName = createString(50);
        int publicTeamId = createTeamWithoutParent(teamLogic, userName, newTeamName, description);
        setTeamPublic(teamDao, publicTeamId);
        String searchWord = newTeamName;
        List<Team> events = teamDao.findPublicTeams(searchWord);
        Assert.assertEquals(1, events.size());
    }

    // ------------------ REMOVE TEAM MEMBER ------------------------

    @Test
    public void test1RemoveTeamMemberWithMinLength() throws Exception {
        String userToRemove = createUserIfNotExists(userLogic, createString(1));

        teamDao.removeTeamMember(userToRemove, parent);
    }

    @Test
    public void test2RemoveTeamMemberWithMaxLength() throws Exception {
        String userToRemove = createUserIfNotExists(userLogic, createString(50));

        teamDao.removeTeamMember(userToRemove, parent);
    }

    @Test
    public void test3RemoveTeamMemberRemovingUser() throws Exception {
        String userToRemove = createUserIfNotExists(userLogic, createString(50));
        teamLogic.inviteTeamMember(userName, userToRemove, parent);

        teamDao.removeTeamMember(userToRemove, parent);
        List<TeamMemberDataForReturn> members = teamDao.getInvitations(parent);

        if(members.size() > 1) {
            fail("Member was not removed!");
        }

    }

}