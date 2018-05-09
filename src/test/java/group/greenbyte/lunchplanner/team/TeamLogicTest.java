package group.greenbyte.lunchplanner.team;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.team.database.Team;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.team.Utils.createTeamWithoutParent;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
@Transactional
public class TeamLogicTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TeamLogic teamLogic;

    @Autowired
    private UserLogic userLogic;

    private String userName;
    private int parent;

    private String teamName;
    private int teamId;
    private String description;

    @Before
    public void setUp() throws Exception {
        userName = createUserIfNotExists(userLogic, "dummy");
        parent = createTeamWithoutParent(teamLogic, userName, createString(10), createString(10));

        description = createString(50);
        teamName = createString(20);

        teamId = createTeamWithoutParent(teamLogic, userName, teamName, description);
    }

    // ------------------------- CREATE TEAM WITH PARENT ------------------------------

    @Test
    public void test1CreateTeamWithNoDescription() throws Exception {
        String teamName = "A";
        String description = "";

        teamLogic.createTeamWithParent(userName, parent, teamName, description);
    }

    @Test
    public void test2CreateTeamWithNormalDescriptionMaxUserNameMaxTeamName() throws Exception {
        String userName = createUserIfNotExists(userLogic, createString(50));
        int parent = createTeamWithoutParent(teamLogic, userName, createString(10), createString(10));
        String teamName = createString(50);
        String description = "Super Team";

        teamLogic.createTeamWithParent(userName, parent, teamName, description);
    }

    @Test
    public void test3CreateTeamWithMaxDescriptionMaxUserNameMaxTeamName() throws Exception {
        String userName = createUserIfNotExists(userLogic, createString(50));
        int parent = createTeamWithoutParent(teamLogic, userName, createString(10), createString(10));
        String teamName = createString(50);
        String description = createString(1000);

        teamLogic.createTeamWithParent(userName, parent, teamName, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test4CreateTeamWithNoUserName() throws Exception {
        String userName = "";
        String teamName = createString(50);
        String description = createString(1000);

        teamLogic.createTeamWithParent(userName, parent, teamName, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test5CreateTeamUserNameTooLong() throws Exception {
        String userName = createString(51);
        String teamName = createString(50);
        String description = createString(1000);

        teamLogic.createTeamWithParent(userName, parent, teamName, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test6CreateTeamWithNoTeamName() throws Exception {
        String teamName = "";
        String description = createString(1000);

        teamLogic.createTeamWithParent(userName, parent, teamName, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test7CreateTeamTeamNameTooLong() throws Exception {
        String teamName = createString(51);
        String description = createString(1000);

        teamLogic.createTeamWithParent(userName, parent, teamName, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test6CreateTeamDescriptionTooLong() throws Exception {
        String teamName = createString(50);
        String description = createString(1001);

        teamLogic.createTeamWithParent(userName, parent, teamName, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test7CreateTeamWithoutPermissionForParent() throws Exception {
        String userName = createUserIfNotExists(userLogic, createString(50));
        String teamName = createString(50);
        String description = createString(1000);

        teamLogic.createTeamWithParent(userName, parent, teamName, description);
    }

    // ------------------------- CREATE TEAM WITHOUT PARENT ------------------------------

    @Test
    public void test1CreateTeamNoParentWithNoDescription() throws Exception {
        String teamName = "A";
        String description = "";

        teamLogic.createTeamWithoutParent(userName, teamName, description);
    }

    @Test
    public void test2CreateTeamWithNoParentNormalDescriptionMaxUserNameMaxTeamName() throws Exception {
        String userName = createUserIfNotExists(userLogic, createString(50));
        int parent = createTeamWithoutParent(teamLogic, userName, createString(10), createString(10));
        String teamName = createString(50);
        String description = "Super Team";

        teamLogic.createTeamWithoutParent(userName, teamName, description);
    }

    @Test
    public void test3CreateTeamWithNoParentMaxDescriptionMaxUserNameMaxTeamName() throws Exception {
        String userName = createUserIfNotExists(userLogic, createString(50));
        int parent = createTeamWithoutParent(teamLogic, userName, createString(10), createString(10));
        String teamName = createString(50);
        String description = createString(1000);

        teamLogic.createTeamWithoutParent(userName, teamName, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test4CreateTeamWithNoParentNoUserName() throws Exception {
        String userName = "";
        String teamName = createString(50);
        String description = createString(1000);

        teamLogic.createTeamWithoutParent(userName, teamName, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test5CreateTeamNoParentUserNameTooLong() throws Exception {
        String userName = createString(51);
        String teamName = createString(50);
        String description = createString(1000);

        teamLogic.createTeamWithoutParent(userName, teamName, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test6CreateTeamWithNoParentNoTeamName() throws Exception {
        String teamName = "";
        String description = createString(1000);

        teamLogic.createTeamWithoutParent(userName, teamName, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test7CreateTeamNoParentTeamNameTooLong() throws Exception {
        String teamName = createString(51);
        String description = createString(1000);

        teamLogic.createTeamWithoutParent(userName, teamName, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test6CreateTeamNoParentDescriptionTooLong() throws Exception {
        String teamName = createString(50);
        String description = createString(1001);

        teamLogic.createTeamWithoutParent(userName, teamName, description);
    }

    // ------------------------- INVITE TEAM MEMBER ------------------------------

    @Test
    public void test1InviteTeamMemberWithMinLength() throws Exception {
        String userName = createUserIfNotExists(userLogic, createString(1));
        int parent = createTeamWithoutParent(teamLogic, userName, createString(10), createString(10));
        String userToInvite = createUserIfNotExists(userLogic, createString(1));

        teamLogic.inviteTeamMember(userName, userToInvite, parent);
    }

    @Test
    public void test2InviteTeamMemberWithMaxLength() throws Exception {
        String userName = createUserIfNotExists(userLogic, createString(50));
        int parent = createTeamWithoutParent(teamLogic, userName, createString(10), createString(10));
        String userToInvite = createUserIfNotExists(userLogic, createString(50));

        teamLogic.inviteTeamMember(userName, userToInvite, parent);
    }

    @Test(expected = HttpRequestException.class)
    public void test3InviteTeamMemberUserNameTooLong() throws Exception {
        String userName = createUserIfNotExists(userLogic, createString(51));
        String userToInvite = createUserIfNotExists(userLogic, createString(50));

        teamLogic.inviteTeamMember(userName, userToInvite, parent);
    }

    @Test(expected = HttpRequestException.class)
    public void test4InviteTeamMemberWithNoUserName() throws Exception {
        String userName = createUserIfNotExists(userLogic, createString(0));
        String userToInvite = createUserIfNotExists(userLogic, createString(50));

        teamLogic.inviteTeamMember(userName, userToInvite, parent);
    }

    @Test(expected = HttpRequestException.class)
    public void test5InviteTeamMemberUserToInviteTooLong() throws Exception {
        String userName = createUserIfNotExists(userLogic, createString(50));
        int parent = createTeamWithoutParent(teamLogic, userName, createString(10), createString(10));
        String userToInvite = createUserIfNotExists(userLogic, createString(51));

        teamLogic.inviteTeamMember(userName, userToInvite, parent);
    }

    @Test(expected = HttpRequestException.class)
    public void test6InviteTeamMemberWithNoUserToInvite() throws Exception {
        String userName = createUserIfNotExists(userLogic, createString(50));
        int parent = createTeamWithoutParent(teamLogic, userName, createString(10), createString(10));
        String userToInvite = createUserIfNotExists(userLogic, createString(0));

        teamLogic.inviteTeamMember(userName, userToInvite, parent);
    }

    @Test(expected = HttpRequestException.class)
    public void test6InviteTeamMemberNoAccessToTeam() throws Exception {
        String userName = createUserIfNotExists(userLogic, createString(50));
        String userToInvite = createUserIfNotExists(userLogic, createString(50));

        teamLogic.inviteTeamMember(userName, userToInvite, parent);
    }

    // ------------------ GET TEAM ------------------------

    @Test
    public void test1GetTeam() throws Exception {
        Team team = teamLogic.getTeam(userName,teamId);
        Assert.assertEquals(teamName, team.getTeamName());
        Assert.assertEquals(description, team.getDescription());
        Assert.assertEquals((int) teamId, (int) team.getTeamId());
    }

}