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
import group.greenbyte.lunchplanner.user.database.notifications.NotificationDatabase;
import group.greenbyte.lunchplanner.user.database.notifications.NotificationOptions;
import group.greenbyte.lunchplanner.user.database.notifications.Notifications;
import group.greenbyte.lunchplanner.user.database.notifications.OptionsJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.Console;
import java.io.File;
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

    private final Environment env;

    private String uploadsDirName;

    // This variable will be set over the setter Method by java spring
    private UserDao userDao;

    @Autowired
    public UserLogic(JwtService jwtService, Environment env) {
        this.jwtService = jwtService;
        this.env = env;
        uploadsDirName = this.env.getProperty("upload.location");
        if(uploadsDirName == null)
            uploadsDirName = "/tmp";
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
            System.out.println("LOGIC ADD FCM");
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

    public void saveNotification(String receiver, String title, String description, String creator, String linkToClick, String picturePath) throws HttpRequestException {
        //save notification
        try {
            userDao.saveNotificationIntoDatabase(receiver,title,description,creator,linkToClick, picturePath);
        } catch(DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public void sendNotification(String fcmToken, String receiver, String title, String description, String linkToClick, String picturePath) throws FirebaseMessagingException,HttpRequestException {
        if(!fcmInitialized) {
            try {
                initNotifications();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("FCM token null");
        if(fcmToken == null)
            return;

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .putData("title", title)
                .putData("body", description)
                .putData("tag", linkToClick)
                .putData("icon", "https://greenbyte.group/assets/images/logo.png")
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
     *
     *
     * @param username
     * @param notificationId
     * @param read
     */
    public void setNotificationRead(String username, int notificationId, boolean read) throws HttpRequestException {
        try {
            Notifications notification = userDao.getNotification(notificationId);
            if(notification == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Notification with id " + notificationId + " not found");

            if(!notification.getReceiverName().equals(username))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You don't have rights to access notification with id " + notificationId);

            userDao.setNotificationRead(notificationId, read);
        } catch (DatabaseException e) {
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
     * @param block_until
     * @param blockedForWork
     * @param start_working
     * @param stop_working
     * @param eventsBlocked
     * @param teamsBlocked
     * @param subscriptionsBlocked
     * @throws HttpRequestException
     */
    public void updateNotificationOptions(String userName, Boolean blockAll,
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

        if(block_until!=null) {
            map.put("block_until", block_until);
        }

        if(blockedForWork!=null)
            map.put("blocked_for_work",blockedForWork);

        if(start_working!=null) {
            try {
                int minutes = OptionsJson.getMinutesFromDate(start_working);
                map.put("start_working", minutes);
            } catch (NumberFormatException ignored) {}
        }

        if(stop_working!=null) {
            try {
                int minutes = OptionsJson.getMinutesFromDate(stop_working);
                map.put("stop_working", minutes);
            } catch (NumberFormatException ignored) {}
        }

        if(eventsBlocked!=null)
            map.put("events_blocked",eventsBlocked);

        if(teamsBlocked!=null)
            map.put("teams_blocked",teamsBlocked);

        if(subscriptionsBlocked!=null)
            map.put("subscriptions_blocked",subscriptionsBlocked);

        if(map.size() == 0)
            return;

        try {
            userDao.updateNotificationOptions(userName,map);
        } catch(DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

    }

    // --------------------- USER PROFILE ---------------------
    /**
     * Update profile picture
     *
     * @param userName user that wants to update their picture
     * @param imageFile path of the picture
     * @throws HttpRequestException
     */
    public void uploadProfilePicture(String userName, MultipartFile imageFile) throws HttpRequestException {


        System.out.println("Logic Erste Afrage: "+userName == null || userName.length() == 0);
        if(userName == null || userName.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "user name is empty");

        System.out.println("uernameLänge:"+userName.length());
        if(userName.length() > User.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "user name is too long");

        System.out.println("imagefile empty?: "+ imageFile.equals(null));
        if(!imageFile.isEmpty()) {
           try{
               System.out.println("conetntype: "+imageFile.getContentType());
               String contentType = imageFile.getContentType();
               String type = contentType.split("/")[0];
               if (!type.equalsIgnoreCase("image")) {
                   throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "the uploaded file is not an image");
               }

               String relativePath = "profilePictures/";

               //the path changes in a different context
               String absolutePath;
               System.out.println("mkdir name: "+ uploadsDirName);
               if(uploadsDirName.charAt(uploadsDirName.length() - 1) != '/') {
                   absolutePath = uploadsDirName + "/" + relativePath;
               } else {
                   System.out.println("im falschen zweig");
                   absolutePath = uploadsDirName + relativePath;
               }

               relativePath = "/" + relativePath;

               System .out.println("relatipath: "+ relativePath);
               System .out.println("absolutpath: "+ absolutePath);


               //create a new directory if it doesn't exist
               if(!new File(absolutePath).exists()) {
                   new File(absolutePath).mkdirs();
               }
               String fileName = userName;


               String path = absolutePath + File.separator + fileName;
               String[] stringAfterPoint = imageFile.getOriginalFilename().split("\\.");
               String fileExtension = "";
               if(stringAfterPoint.length > 0)
                    fileExtension = stringAfterPoint[stringAfterPoint.length - 1];
               String pathForDb = relativePath + fileName + "." + fileExtension;
               File destination = new File(path + "." + fileExtension);
               imageFile.transferTo(destination);
               userDao.savePicturePath(userName, pathForDb);


           } catch(IOException | DatabaseException e){
               throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error 500()");
           }

        } else {
            //TODO geht das?
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "uploaded file is empty");
        }
    }

    /**
     * Get the relative picture path
     *
     * @param userName user
     * @return absolute picture path
     */
    public String getPicturePath(String userName) throws HttpRequestException {
        if(userName == null || userName.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "user name is empty");

        if(userName.length() > User.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "user name is too long");

        try {
            System.out.println("getProfilepicture: "+ userDao.getUser(userName).getProfilePictureUrl());
            return userDao.getUser(userName).getProfilePictureUrl();
//            String absolutePath = request.getServletContext().getRealPath(path);
//            return absolutePath;
        } catch (DatabaseException e) {
            throw new HttpRequestException (HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    /**
     * Update user password
     *
     * @param userName user that wants to update their password
     * @param password
     * @throws HttpRequestException
     */
    public void updateUserPassword(String userName, String password) throws HttpRequestException {
        if(userName == null || userName.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "user name is empty");

        if(userName.length() > User.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "user name is too long");

        if(password == null || password.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "password is empty");

        try {
            userDao.saveNewPassword(userName, SecurityHelper.hashPassword(password));
        } catch(DatabaseException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

    }

    public void updateUserEmail(String userName, String mail) throws HttpRequestException {
        if(userName == null || userName.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "user name is empty");

        if(userName.length() > User.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "user name is too long");

        if(mail == null || mail.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "mail is empty");

        if(mail.length() > User.MAX_MAIL_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "mail is too long");

        if(!REGEX_MAIL.matcher(mail).matches())
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "mail is not valid");

        try {
            userDao.saveNewEmail(userName, mail);
        } catch (DatabaseException e) {
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

    public void unsubscribe(String subscriber, String location) throws HttpRequestException{
        try {
            userDao.unsubscribe(subscriber, location);
        } catch (DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
