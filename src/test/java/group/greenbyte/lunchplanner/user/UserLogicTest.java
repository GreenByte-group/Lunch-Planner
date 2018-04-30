package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.user.database.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Calendar;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.user.Utils.createUserIfNotExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
public class UserLogicTest {

    @Autowired
    private UserLogic userLogic;

    // ------------- CREATE USER ------------------

    @Test
    public void test1CreateUserValidParam() throws Exception{
        String userName = createString(50);
        String mail = "nknakldsnf@jkldadsf.klen";
        String password = createString(80);

        userLogic.createUser(userName, password, mail);
    }

    @Test(expected = HttpRequestException.class)
    public void test2CreateUserEmptyUsername() throws Exception{
        String userName = "";
        String mail = createString(50);
        String password = createString(80);

        userLogic.createUser(userName, password, mail);
    }

    @Test(expected = HttpRequestException.class)
    public void test3CreateUserTooLongUserName() throws Exception{
        String userName = createString(51);
        String mail = createString(50);
        String password = createString(80);

        userLogic.createUser(userName, password, mail);
    }

    @Test(expected = HttpRequestException.class)
    public void test4CreateUserEmptyPassword() throws Exception{
        String userName = createString(50);
        String mail = createString(50);
        String password = "";

        userLogic.createUser(userName, password, mail);
    }

    @Test(expected = HttpRequestException.class)
    public void test5CreateUserTooLongPassword() throws Exception{
        String userName = createString(50);
        String mail = createString(50);
        String password = createString(81);

        userLogic.createUser(userName, password, mail);
    }

    @Test(expected = HttpRequestException.class)
    public void test6CreateUserMailEmpty() throws Exception{
        String userName = createString(50);
        String mail = "";
        String password = createString(80);

        userLogic.createUser(userName, password, mail);
    }

    @Test(expected = HttpRequestException.class)
    public void test7CreateUserTooLongEmail() throws Exception{
        String userName = createString(50);
        String mail = createString(51);
        String password = createString(80);

        userLogic.createUser(userName, password, mail);
    }

    @Test(expected = HttpRequestException.class)
    public void test1CreateUserInvalidMail() throws Exception{
        String userName = createString(50);
        String mail = "ungueltige-mail.de";
        String password = createString(80);

        userLogic.createUser(userName, password, mail);
    }

    // ------------------------ JWT ------------------------
    @Test
    public void testExpirationDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_WEEK, 1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(userLogic.getExpirationDate());

        assertEquals(c.get(Calendar.DAY_OF_WEEK), c2.get(Calendar.DAY_OF_WEEK));
        assertEquals(c.get(Calendar.DAY_OF_MONTH), c2.get(Calendar.DAY_OF_MONTH));
        assertEquals(c.get(Calendar.MONTH), c2.get(Calendar.MONTH));
        assertEquals(c.get(Calendar.YEAR), c2.get(Calendar.YEAR));
        assertEquals(c.get(Calendar.HOUR_OF_DAY), c2.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void testCreateUserToken() throws Exception {
        String userName = createUserIfNotExists(userLogic, "usernaem");
        User auth = userLogic.createUserToken(userName);
        assertNotNull(auth);
        assertEquals(userName, auth.getUserName());
    }

    @Test
    public void testValidateUser() throws Exception {
        String userName = createUserIfNotExists(userLogic, "usernaem");
        User auth = userLogic.createUserToken(userName);

        User authenticated = userLogic.validateUser(auth.getToken());

        assertEquals(auth.getUserName(), authenticated.getUserName());
        assertEquals(auth.getPassword(), authenticated.getPassword());
        assertEquals(auth.geteMail(), authenticated.geteMail());
        assertEquals(auth.getToken(), authenticated.getToken());

        User notAuth = userLogic.validateUser(auth.getToken() + "a");
        assertNull(notAuth);
    }
}