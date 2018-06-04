package group.greenbyte.lunchplanner.user;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.security.JwtService;
import group.greenbyte.lunchplanner.user.database.User;
import group.greenbyte.lunchplanner.user.database.notifications.NotificationOptions;
import group.greenbyte.lunchplanner.user.database.notifications.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class UserLogic {

    private final JwtService jwtService;

    private static final Pattern REGEX_MAIL = Pattern.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");

    // This variable will be set over the setter Method by java spring
    private UserDao userDao;

    @Autowired
    public UserLogic(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    // ------------------ JWT --------------------
    public User createUserToken(String username) {
        String token = jwtService.createToken(username, getExpirationDate());
        try {
            return userDao.setTokenForUser(username, token);
        } catch (DatabaseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User validateUser(String token) {
        try {
            String username = jwtService.getUsername(token);
            if (username != null ) {
                User user = userDao.getUser(username);

                if (user != null && token.equals(user.getToken()) && jwtService.isValid(token)) {
                    return user;
                }
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public Date getExpirationDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_WEEK, 1);
        return c.getTime();
    }


    // --------------------- JWT ENDE ----------------------

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

        if(userName.length() > User.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "user name is too long");

        if(password == null || password.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "password is empty");

        if(mail == null || mail.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "mail is empty");

        if(mail.length() > User.MAX_MAIL_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "mail is too long");

        if(!REGEX_MAIL.matcher(mail).matches())
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "mail is not valid");

        try {
            if(userDao.getUser(userName) != null)
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "user name already exists");

            userDao.createUser(userName, SecurityHelper.hashPassword(password), mail);
        } catch (DatabaseException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    /**
     *
     * @param searchword String for searching the Database
     * @return a List of User in natural sorted order
     * @throws HttpRequestException
     */
    public List<User> searchUserByName(String searchword) throws HttpRequestException{
        try{
            return userDao.searchUserByName(searchword);
        }catch (Exception e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    /**
     *
     * @param userName name of Username for searching in database
     * @return
     * @throws HttpRequestException
     */
    public User getUser(String userName) throws HttpRequestException {
        try {
            return userDao.getUser(userName);
        } catch (DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public void addFcmToken(String username, String fcmToken) throws HttpRequestException {
        try {
            userDao.setFcmForUser(username, fcmToken);
        } catch(DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    /**
     * Send an invitation to a user (async)
     *
     * @param userName who intivtes
     * @param toInviteUserName who is invited
     */
    public void sendInvitation(String userName, String toInviteUserName) {
        //ToDO send notfication to user
    }

    public void sendNotification(String fcmToken, String receiver, String title, String description, String linkToClick, String picturePath) throws FirebaseMessagingException,HttpRequestException {
        if(!fcmInitialized) {
            try {
                initNotifications();
            } catch (IOException e) {
                throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            }
        }

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .putData("title", title)
                .putData("body", description)
                .putData("click_action", linkToClick)
                .putData("picture", picturePath)
                .setToken(fcmToken)
                .build();

        // Send a message to the device corresponding to the provided
        // registration token.
        String response = FirebaseMessaging.getInstance().send(message);
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + response);

    }

    //TODO write tests for this function
    /**
     * Get all notifications for user
     *
     * @param userName receiver of the notifications
     * @return
     * @throws HttpRequestException
     */
    public List<Notifications> getNotifications(String userName) throws HttpRequestException{
        if(userName == null || userName.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "user name is empty");

        if(userName.length() > User.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "user name is too long");

        try {
            return userDao.getNotifications(userName);
        } catch(DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

    }

    /**
     * Get notification options for user
     *
     * @param userName receiver of the notifiction options
     * @return
     * @throws HttpRequestException
     */
    public NotificationOptions getNotificationOptions(String userName) throws HttpRequestException {
        if(userName == null || userName.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "user name is empty");

        if(userName.length() > User.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "user name is too long");

        try {
            return userDao.getNotificationOptions(userName);
        } catch(DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

    }

    /**
     * Update notification options
     *
     * @param userName
     * @param blockAll
     * @param blockedUntil
     * @param block_until
     * @param blockedForWork
     * @param start_working
     * @param stop_working
     * @param eventsBlocked
     * @param teamsBlocked
     * @param subscriptionsBlocked
     * @throws HttpRequestException
     */
    public void updateNotificationOptions(String userName, Boolean blockAll, Boolean blockedUntil,
                                            Date block_until, Boolean blockedForWork, Date start_working,
                                            Date stop_working, Boolean eventsBlocked, Boolean teamsBlocked,
                                            Boolean subscriptionsBlocked) throws HttpRequestException {

        if(userName == null || userName.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "user name is empty");

        if(userName.length() > User.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "user name is too long");



        Map<String, Object> map = new HashMap<>();

        if(blockAll!=null)
            map.put("block_all",blockAll);

        if(blockedUntil!=null)
            map.put("blocked_until",blockedUntil);

        if(block_until!=null) {
            if(block_until.before(new Date()))
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Datetime must be in the future");

            map.put("block_until", block_until);
        }

        if(blockedForWork!=null)
            map.put("blocked_for_work",blockedForWork);

        if(start_working!=null)
            map.put("start_working",start_working);

        if(stop_working!=null)
            map.put("stop_working",stop_working);

        if(eventsBlocked!=null)
            map.put("events_blocked",eventsBlocked);

        if(teamsBlocked!=null)
            map.put("teams_blocked",teamsBlocked);

        if(subscriptionsBlocked!=null)
            map.put("subscriptions_blocked",subscriptionsBlocked);

        try {
            userDao.updateNotificationOptions(userName,map);
        } catch(DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

    }

    private boolean fcmInitialized = false;
    private void initNotifications() throws IOException {
        Resource resource = new ClassPathResource("lunchplanner-private-fcm-config.json");
        InputStream serviceAccount = resource.getInputStream();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://lunch-planner-ac676.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
        fcmInitialized = true;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    // --------------------- SUBSCRIBE ----------------------

    public  List<String> getSubscribedLocations(String subscriber) throws HttpRequestException{
        if(subscriber == null || subscriber.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "subscriber name is empty");

        if(subscriber.length() > User.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "subscriber name is too long");

        try {
            return userDao.getSubscribedLocations(subscriber);
        } catch(DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public List<User> getSubscriber(String location) throws HttpRequestException{
        if(location == null || location.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "location is empty");
        try {
            return userDao.getSubscriber(location);
        } catch(DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public void subscribe(String subscriber, String location) throws HttpRequestException{
        try {
            userDao.subscribe(subscriber, location);
        } catch (DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
