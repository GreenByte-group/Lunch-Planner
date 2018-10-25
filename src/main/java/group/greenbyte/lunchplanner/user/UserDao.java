package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.user.database.Debts;
import group.greenbyte.lunchplanner.user.database.notifications.NotificationOptions;
import group.greenbyte.lunchplanner.user.database.notifications.Notifications;
import group.greenbyte.lunchplanner.user.database.User;

import java.util.List;
import java.util.Map;

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
     * @param userName
     */
    void deleteUser(String userName) throws DatabaseException;
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


    void terminateUser(String username) throws DatabaseException;

    Notifications getNotification(int notificationId) throws DatabaseException;


    void setNotificationRead(int notificationId, boolean read) throws DatabaseException;

    /**
     * Gets notification options of a user
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
    void subscribe(String userName, String location, String locationName) throws DatabaseException;
    NotificationOptions getNotificationOptions(String userName) throws DatabaseException;

    /**
     * Update notification options for user
     * @param userName user that updates his options
     * @param map contains column names and option values
     * @throws DatabaseException
     */
    void updateNotificationOptions(String userName,Map<String,Object> map) throws DatabaseException;

    /**
     * Put the hashed password into the database
     *
     * @param userName user that updates their password
     * @param hashedPassword new hashed password
     * @throws DatabaseException
     */
    void saveNewPassword(String userName, String hashedPassword) throws DatabaseException;

    /**
     * Put the new e-mail into the database
     *
     * @param userName
     * @param eMail
     * @throws DatabaseException
     */
    void saveNewEmail(String userName, String eMail) throws DatabaseException;

    /**
     * Put the picture path into database
     *
     * @param userName
     * @param picturePath relative path to the picture
     * @throws DatabaseException
     */
    void savePicturePath(String userName, String picturePath) throws DatabaseException;

    void unsubscribe(String subscriber, String location) throws DatabaseException;

    void setDebts(String creditor, String debtor, Float sum) throws DatabaseException;

    List<Debts> getDebts(String creditor) throws DatabaseException;

    List<Debts> getLiab(String creditor) throws DatabaseException;

    Debts getDebt(int debtId) throws DatabaseException;

    void deleteDebts(String creditor, int debtsId) throws DatabaseException;


}
