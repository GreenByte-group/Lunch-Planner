package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.AppConfig;
<<<<<<< HEAD
import group.greenbyte.lunchplanner.event.EventDao;
=======
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
<<<<<<< HEAD

import static group.greenbyte.lunchplanner.Utils.createString;
import static org.junit.Assert.*;
=======
import org.springframework.transaction.annotation.Transactional;

import static group.greenbyte.lunchplanner.Utils.createString;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = AppConfig.class)
@ActiveProfiles("application-test.properties")
<<<<<<< HEAD
=======
@Transactional
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    // ------------ CREATE USER --------------

    @Test
    public void test1CreateUserValidParam() throws Exception {
        String userName = createString(50);
        String mail = createString(50);
<<<<<<< HEAD
        String password = createString(80);
=======
        String password = createString(200);
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

        userDao.createUser(userName, password, mail);
    }

    @Test(expected = DatabaseException.class)
    public void test2CreateUserTooLongUserName() throws Exception {
        String userName = createString(51);
        String mail = createString(50);
<<<<<<< HEAD
        String password = createString(80);
=======
        String password = createString(200);
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

        userDao.createUser(userName, password, mail);
    }

    @Test(expected = DatabaseException.class)
    public void test3CreateUserTooLongPassword() throws Exception {
        String userName = createString(50);
        String mail = createString(50);
<<<<<<< HEAD
        String password = createString(81);
=======
        String password = createString(201);
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

        userDao.createUser(userName, password, mail);
    }

    @Test(expected = DatabaseException.class)
    public void test4CreateUserTooLongMail() throws Exception {
        String userName = createString(50);
        String mail = createString(51);
<<<<<<< HEAD
        String password = createString(80);

        userDao.createUser(userName, password, mail);
    }
=======
        String password = createString(200);

        userDao.createUser(userName, password, mail);
    }



    @Test
    public void test1SearchUserValidParam() throws Exception {
        String userName = createString(50);
        String mail = "gueltige@mail.de";
        String password = createString(50);
        String searchword =userName;
        userDao.createUser(userName, password, mail);
        userDao.searchUserByName(searchword);
    }

    @Test(expected = DatabaseException.class)
    public void test2SearchUserTooLongSearchword() throws Exception {
        String userName = createString(50);
        String mail = createString(51);
        String password = createString(50);
        String searchword = userName+"a";

        userDao.createUser(userName, password, mail);
        userDao.searchUserByName(searchword);

    }

    @Test(expected = DatabaseException.class)
    public void test3SearchUserSearchwordIsNull() throws Exception {
        String userName = createString(50);
        String mail = createString(51);
        String password = createString(50);
        String searchword = null;

        userDao.createUser(userName, password, mail);
        userDao.searchUserByName(searchword);

    }
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
}