package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserLogic {

    private static final Pattern REGEX_MAIL = Pattern.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");

    // This variable will be set over the setter Method by java spring
    private UserDao userDao;

    /**
     * Create a user
     *
     * @param userName his username
     * @param password the unhashed password
     * @param mail mail-adress
     *
     * @throws HttpRequestException when an parameter is not valid or user already exists or an DatabaseError happens
     */
    void createUser(String userName, String password, String mail) throws HttpRequestException {
        if(userName == null || userName.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "user name is empty");

        if(password == null || password.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "password is empty");

        if(mail == null || mail.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "mail is empty");

        if(!REGEX_MAIL.matcher(mail).matches())
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "mail is not valid");

        try {
            //ToDo hash password
            userDao.createUser(userName, password, mail);
        } catch (DatabaseException e) {
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
