package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.user.database.Notifications;
import group.greenbyte.lunchplanner.user.database.User;

import java.util.List;

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

    /**
     *
     * @param searchword
     * @return
     * @throws DatabaseException
     */
    List<User> searchUserByName(String searchword) throws DatabaseException;

    /**
     * //TODO write tests for this
     * Sets the fcm token for a user
     * @param userName
     * @param fcmToken
     */
    void setFcmForUser(String userName, String fcmToken) throws DatabaseException;

    /**
     *
     * @param userName
     * @return
     * @throws DatabaseException
     */
    List<Notifications> getNotifications(String userName) throws DatabaseException;

    /**
     *
     * @param receiver
     * @param title
     * @param description
     * @param builder
     * @param linkToClick
     * @param picturePath
     */
    void  saveNotificationIntoDatabase(String receiver, String title, String description
                                        ,String builder, String linkToClick, String picturePath) throws DatabaseException;


    /**
     *
     * @param userName
     * @return
     * @throws DatabaseException
     */
    List<String> getSubscribedLocations(String userName) throws DatabaseException;

    /**
     *
     * @param location
     * @return
     * @throws DatabaseException
     */
    List<User> getSubscriber(String location) throws DatabaseException;

    /**
     *
     * @param userName
     * @param location
     * @throws DatabaseException
     */
    void subscribe(String userName, String location) throws DatabaseException;
}
