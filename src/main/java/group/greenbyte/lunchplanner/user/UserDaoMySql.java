package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.user.database.User;
import group.greenbyte.lunchplanner.user.database.UserDatabaseConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoMySql implements UserDao {

    private final UserDatabaseConnector userDatabaseConnector;

    @Autowired
    public UserDaoMySql(UserDatabaseConnector userDatabaseConnector) {
        this.userDatabaseConnector = userDatabaseConnector;
    }

    @Override
    public User getUser(String userName) throws DatabaseException {
        try {
            return this.userDatabaseConnector.findUserByUserName(userName);
        } catch (Exception e) {
            throw new DatabaseException();
        }
    }

    @Override
    public void createUser(String userName, String password, String mail) throws DatabaseException {
        User user = new User();
        user.setUserName(userName);
        user.seteMail(mail);
        user.setPassword(password);

        try {
            userDatabaseConnector.save(user);
        } catch(Exception e) {
            throw new DatabaseException();
        }
    }
}
