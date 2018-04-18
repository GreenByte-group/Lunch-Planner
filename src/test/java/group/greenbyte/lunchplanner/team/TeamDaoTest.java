package group.greenbyte.lunchplanner.team;

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

import static group.greenbyte.lunchplanner.Utils.createString;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = AppConfig.class)
@ActiveProfiles("application-test.properties")
public class TeamDaoTest {

    @Autowired
    private TeamDao teamDao;

    // ------------------ CREATE TEAM ------------------------

    @Test
    public void test1InsertTeamWithNoDescription() throws Exception {
        String adminName = "A";
        int parent = 1;
        String teamName = "A";
        String description = "";

        teamDao.insertTeam(teamName, description, adminName, parent);
    }

    @Test
    public void test2InsertTeamWithMaxAdminNameMaxTeamNameNormalDescription() throws Exception {
        String adminName = createString(50);
        int parent = 1;
        String teamName = createString(50);
        String description = "Super Team";

        teamDao.insertTeam(teamName, description, adminName, parent);
    }

    @Test
    public void test3InsertTeamWithMaxDescription() throws Exception {
        String adminName = createString(50);
        int parent = 1;
        String teamName = createString(50);
        String description = createString(1000);

        teamDao.insertTeam(teamName, description, adminName, parent);
    }

    @Test(expected = DatabaseException.class)
    public void test4InsertTeamWithNoAdminName() throws Exception {
        String adminName = "";
        int parent = 1;
        String teamName = createString(50);
        String description = createString(1000);

        teamDao.insertTeam(teamName, description, adminName, parent);
    }

    @Test(expected = DatabaseException.class)
    public void test5InsertTeamAdminNameTooLong() throws Exception {
        String adminName = createString(51);
        int parent = 1;
        String teamName = createString(50);
        String description = createString(1000);

        teamDao.insertTeam(teamName, description, adminName, parent);
    }

    @Test(expected = DatabaseException.class)
    public void test6InsertTeamWithNoTeamName() throws Exception {
        String adminName = createString(50);
        int parent = 1;
        String teamName = "";
        String description = createString(1000);

        teamDao.insertTeam(teamName, description, adminName, parent);
    }

    @Test(expected = DatabaseException.class)
    public void test7InsertTeamTeamNameTooLong() throws Exception {
        String adminName = createString(50);
        int parent = 1;
        String teamName = createString(51);
        String description = createString(1000);

        teamDao.insertTeam(teamName, description, adminName, parent);
    }

    @Test(expected = DatabaseException.class)
    public void test8InsertTeamDescriptionTooLong() throws Exception {
        String adminName = createString(50);
        int parent = 1;
        String teamName = createString(50);
        String description = createString(1001);

        teamDao.insertTeam(teamName, description, adminName, parent);
    }

    // ------------------ PUT USER TEAM MEMBER ------------------------

    @Test
    public void test1PutUserTeamMemberWithMinLength() throws Exception {
       int teamId = 1;
       String userToInviteName = "A";

       teamDao.putUserTeamMember(userToInviteName,teamId);
    }

    @Test
    public void test2PutUserTeamMemberWithMaxLength() throws Exception {
        int teamId = 1;
        String userToInviteName = createString(50);

        teamDao.putUserTeamMember(userToInviteName,teamId);
    }

    @Test(expected = DatabaseException.class)
    public void test3PutUserTeamMemberUserToInviteTooLong() throws Exception {
        int teamId = 1;
        String userToInviteName = createString(51);

        teamDao.putUserTeamMember(userToInviteName,teamId);
    }

    @Test(expected = DatabaseException.class)
    public void test4PutUserTeamMemberWithNoUserToInvite() throws Exception {
        int teamId = 1;
        String userToInviteName = "";

        teamDao.putUserTeamMember(userToInviteName,teamId);
    }




}