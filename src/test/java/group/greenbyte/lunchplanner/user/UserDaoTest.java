package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.EventDao;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static group.greenbyte.lunchplanner.Utils.createString;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = AppConfig.class)
@ActiveProfiles("application-test.properties")
@Transactional
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    // ------------ CREATE USER --------------

    @Test
    public void test1CreateUserValidParam() throws Exception {
        String userName = createString(50);
        String mail = createString(50);
        String password = createString(200);

        userDao.createUser(userName, password, mail);
    }

    @Test(expected = DatabaseException.class)
    public void test2CreateUserTooLongUserName() throws Exception {
        String userName = createString(51);
        String mail = createString(50);
        String password = createString(200);

        userDao.createUser(userName, password, mail);
    }

    @Test(expected = DatabaseException.class)
    public void test3CreateUserTooLongPassword() throws Exception {
        String userName = createString(50);
        String mail = createString(50);
        String password = createString(201);

        userDao.createUser(userName, password, mail);
    }

    @Test(expected = DatabaseException.class)
    public void test4CreateUserTooLongMail() throws Exception {
        String userName = createString(50);
        String mail = createString(51);
        String password = createString(200);

        userDao.createUser(userName, password, mail);
    }
}