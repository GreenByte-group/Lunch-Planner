package group.greenbyte.lunchplanner.team;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static group.greenbyte.lunchplanner.Utils.createString;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
public class TeamLogicTest {

    @Autowired
    private TeamLogic teamLogic;

    // ------------------------- CREATE TEAM ------------------------------

    @Test
    public void test1CreateTeamWithNoDescription() throws Exception {
        String userName = "A";
        int parent = 1;
        String teamName = "A";
        String description = "";

        teamLogic.createTeam(userName, parent, teamName, description);
    }

    @Test
    public void test2CreateTeamWithNormalDescriptionMaxUserNameMaxTeamName() throws Exception {
        String userName = createString(50);
        int parent = 1;
        String teamName = createString(50);
        String description = "Super Team";

        teamLogic.createTeam(userName, parent, teamName, description);
    }

    @Test
    public void test3CreateTeamWithMaxDescriptionMaxUserNameMaxTeamName() throws Exception {
        String userName = createString(50);
        int parent = 1;
        String teamName = createString(50);
        String description = createString(1000);

        teamLogic.createTeam(userName, parent, teamName, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test4CreateTeamWithNoUserName() throws Exception {
        String userName = "";
        int parent = 1;
        String teamName = createString(50);
        String description = createString(1000);

        teamLogic.createTeam(userName, parent, teamName, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test5CreateTeamUserNameTooLong() throws Exception {
        String userName = createString(51);
        int parent = 1;
        String teamName = createString(50);
        String description = createString(1000);

        teamLogic.createTeam(userName, parent, teamName, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test6CreateTeamWithNoTeamName() throws Exception {
        String userName = createString(50);
        int parent = 1;
        String teamName = "";
        String description = createString(1000);

        teamLogic.createTeam(userName, parent, teamName, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test7CreateTeamTeamNameTooLong() throws Exception {
        String userName = createString(50);
        int parent = 1;
        String teamName = createString(51);
        String description = createString(1000);

        teamLogic.createTeam(userName, parent, teamName, description);
    }

    @Test(expected = HttpRequestException.class)
    public void test6CreateTeamDescriptionTooLong() throws Exception {
        String userName = createString(50);
        int parent = 1;
        String teamName = createString(50);
        String description = createString(1001);

        teamLogic.createTeam(userName, parent, teamName, description);
    }

    // ------------------------- CREATE TEAM ------------------------------

    @Test
    public void test1InviteTeamMemberWithMinLength() throws Exception {
        String userName = "A";
        String userToInvite = "A";
        int teamId = 1;

        teamLogic.inviteTeamMember(userName, userToInvite, teamId);
    }

    @Test
    public void test2InviteTeamMemberWithMaxLength() throws Exception {
        String userName = createString(50);
        String userToInvite = createString(50);
        int teamId = 1;

        teamLogic.inviteTeamMember(userName, userToInvite, teamId);
    }

    @Test(expected = HttpRequestException.class)
    public void test3InviteTeamMemberUserNameTooLong() throws Exception {
        String userName = createString(51);
        String userToInvite = createString(50);
        int teamId = 1;

        teamLogic.inviteTeamMember(userName, userToInvite, teamId);
    }

    @Test(expected = HttpRequestException.class)
    public void test4InviteTeamMemberWithNoUserName() throws Exception {
        String userName = "";
        String userToInvite = createString(50);
        int teamId = 1;

        teamLogic.inviteTeamMember(userName, userToInvite, teamId);
    }

    @Test(expected = HttpRequestException.class)
    public void test5InviteTeamMemberUserToInviteTooLong() throws Exception {
        String userName = createString(50);
        String userToInvite = createString(51);
        int teamId = 1;

        teamLogic.inviteTeamMember(userName, userToInvite, teamId);
    }

    @Test(expected = HttpRequestException.class)
    public void test6InviteTeamMemberWithNoUserToInvite() throws Exception {
        String userName = createString(50);
        String userToInvite = "";
        int teamId = 1;

        teamLogic.inviteTeamMember(userName, userToInvite, teamId);
    }
}