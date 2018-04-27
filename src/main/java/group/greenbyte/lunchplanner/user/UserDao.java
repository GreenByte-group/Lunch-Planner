package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.team.database.Team;
import group.greenbyte.lunchplanner.user.database.User;

import java.util.List;
import java.util.Set;

public interface UserDao {

    /**
     * Finds a user by his username
     *
     * @param userName the username of the user
     * @return the user with all his data. null if the user does not exist
     * @throws DatabaseException when an unexpected error happens
     */
    User getUser(String userName) throws DatabaseException;

    /**
     * Creates a user
     *
     * @param userName his username
     * @param password the hashed password
     * @param mail the mail address
     * @throws DatabaseException when an unexpected error happens
     */
    void createUser(String userName, String password, String mail) throws DatabaseException;

    /**
     * Sets the jwt token for the user
     *
     * @param username
     * @param token
     * @return
     */
    User setTokenForUser(String username, String token) throws DatabaseException;
}
