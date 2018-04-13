package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.user.database.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoMySql implements UserDao {
    @Override
    public User getUser(String userName) throws DatabaseException {
        return null;
    }

    @Override
    public void createUser(String userName, String password, String mail) throws DatabaseException {

    }
}
