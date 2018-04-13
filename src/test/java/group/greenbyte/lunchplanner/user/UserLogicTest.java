package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.Utils.getJsonFromObject;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
public class UserLogicTest {

    @Autowired
    private UserLogic userLogic;

    @Test
    public void test1CreateUserValidParam() throws Exception{
        String userName = createString(50);
        String mail = createString(50);
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
}