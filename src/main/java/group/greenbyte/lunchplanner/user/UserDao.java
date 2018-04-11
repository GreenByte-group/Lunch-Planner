package group.greenbyte.lunchplanner.user;

import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    /**
     * Finds a user by his username
     *
     * @param userName the username of the user
     * @return the user with all his data
     */
    User getUser(String userName);

    /**
     * Creates a user
     *
     * @param userName his username
     * @param password the hashed password
     * @param mail the mail address
     */
    void createUser(String userName, String password, String mail);

}
