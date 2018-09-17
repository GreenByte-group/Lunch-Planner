package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.user.database.SubscribeDatabase;
import group.greenbyte.lunchplanner.user.database.User;
import group.greenbyte.lunchplanner.user.database.UserDatabase;
import group.greenbyte.lunchplanner.user.database.notifications.NotificationDatabase;
import group.greenbyte.lunchplanner.user.database.notifications.NotificationOptions;
import group.greenbyte.lunchplanner.user.database.notifications.NotificationOptionsDatabase;
import group.greenbyte.lunchplanner.user.database.notifications.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserDaoMySql implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public static final String USER_TABLE = "user";
    public static final String USER_NAME = "user_name";
    public static final String USER_MAIL = "e_mail";
    public static final String USER_PASSWORD = "password";
    public static final String USER_TOKEN = "token";
    public static final String USER_FCM_TOKEN = "fcm_token";
    public static final String USER_PICTURE = "profile_picture_url";

    public static final String USER_NOTIFICATION_TABLE = "notifications";
    public static final String USER_NOTIFICATION_ID = "notification_id";
    public static final String USER_NOTIFICATION_TITEL = "titel";
    public static final String USER_NOTIFICATION_MESSAGE = "message";
    public static final String USER_NOTIFICATION_RECEIVER = "user_name";
    public static final String USER_NOTIFICATION_BUILDER = "builder";
    public static final String USER_NOTIFICATION_LINK = "link";
    public static final String USER_NOTIFICATION_PICTURE = "picture";
    public static final String USER_NOTIFICATION_DATE = "date";
    public static final String USER_NOTIFICATION_READ = "is_read";

    public static final String USER_SUBSCRIBE_TABLE = "subscribe";
    public static final String USER_SUBSCRIBE_SUBSCRIBER = "user_name";
    public static final String USER_SUBSCRIBE_LOCATION = "location";

    public static final String USER_NOTIFICATIONOPTIONS_TABLE = "notification_options";
    public static final String USER_NOTIFICATIONOPTIONS_ID = "id";
    public static final String USER_NOTIFICATIONOPTIONS_USER = "user_name";
    public static final String USER_NOTIFICATIONOPTIONS_BLOCKALL = "block_all";
    public static final String USER_NOTIFICATIONOPTIONS_BLOCKEDUNTIL = "blocked_until";
    public static final String USER_NOTIFICATIONOPTIONS_BLOCKUNTILDATE = "block_until";
    public static final String USER_NOTIFICATIONOPTIONS_BLOCKEDFORWORK = "blocked_for_work";
    public static final String USER_NOTIFICATIONOPTIONS_STARTWORKING = "start_working";
    public static final String USER_NOTIFICATIONOPTIONS_STOPWORKING = "stop_working";
    public static final String USER_NOTIFICATIONOPTIONS_BLOCKEVENTS = "events_blocked";
    public static final String USER_NOTIFICATIONOPTIONS_BLOCKTEAMS = "teams_blocked";
    public static final String USER_NOTIFICATIONOPTIONS_BLOCKSUBSCRIPTIONS = "subscriptions_blocked";


    @Autowired
    public UserDaoMySql(JdbcTemplate jdbcTemplateObject) {
        this.jdbcTemplate = jdbcTemplateObject;
    }

    @Override
    public User getUser(String userName) throws DatabaseException {
        try {
            String SQL = "SELECT * FROM " + USER_TABLE + " WHERE " + USER_NAME + " = ?";

            List<UserDatabase> users = jdbcTemplate.query(SQL,
                    new BeanPropertyRowMapper<>(UserDatabase.class),
                    userName);

            if (users.size() == 0)
                return null;
            else {
                return users.get(0).getUser();
            }
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<Notifications> getNotifications(String userName) throws DatabaseException{
        String SQL = " SELECT * FROM " + USER_NOTIFICATION_TABLE + " WHERE " + USER_NOTIFICATION_RECEIVER + " = ?";

//        List<BringServiceDatabase> serviceList = jdbcTemplate.query(SQL,
//                new BeanPropertyRowMapper<>(BringServiceDatabase.class),
//                eventId);
//
//        List<BringService> serviceReturn = new ArrayList<>(serviceList.size());
//        for(BringServiceDatabase bringServiceDatabase : serviceList){
//            BringService bringService = bringServiceDatabase.getBringService();
//            serviceReturn.add(bringService);
//        }
//
//        return serviceReturn;
//    }catch(Exception e){
//        throw new DatabaseException(e);
//    }
//
        try{
        List<NotificationDatabase> notificationList = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(NotificationDatabase.class),
                userName);

        List<Notifications> notificationReturn = new ArrayList<>(notificationList.size());
        for (NotificationDatabase noti : notificationList) {
            Notifications noti2 = noti.getNotification();
            notificationReturn.add(noti2);
        }
        return notificationReturn;
        }catch(Exception e){
            throw new DatabaseException(e);
        }
    }

    @Override
    public void saveNotificationIntoDatabase(String receiver, String title, String description
            , String builder, String linkToClick, String picturePath) throws DatabaseException {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName(USER_NOTIFICATION_TABLE).usingGeneratedKeyColumns(USER_NOTIFICATION_ID);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(USER_NOTIFICATION_BUILDER,builder);
        parameters.put(USER_NOTIFICATION_TITEL,title);
        parameters.put(USER_NOTIFICATION_MESSAGE,description);
        parameters.put(USER_NOTIFICATION_RECEIVER,receiver);
        parameters.put(USER_NOTIFICATION_LINK,linkToClick);
        parameters.put(USER_NOTIFICATION_PICTURE,picturePath);
        parameters.put(USER_NOTIFICATION_DATE,new Date());
        parameters.put(USER_NOTIFICATION_READ, false);

        try {
            simpleJdbcInsert.execute(new MapSqlParameterSource(parameters));
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Notifications getNotification(int notificationId) throws DatabaseException {
        try {
            String SQL = "SELECT * FROM " + USER_NOTIFICATION_TABLE + " WHERE " +
                    USER_NOTIFICATION_ID + " = ?";

            List<NotificationDatabase> notificationList =
                    jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(NotificationDatabase.class),
                    notificationId);

            if(notificationList.size() == 0)
                return null;
            else {
                return notificationList.get(0).getNotification();
            }
        } catch(Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void setNotificationRead(int notificationId, boolean read) throws DatabaseException {
        try {
            String SQL = "UPDATE " + USER_NOTIFICATION_TABLE + " SET " + USER_NOTIFICATION_READ +
                    " = ? WHERE " + USER_NOTIFICATION_ID + " = ?";

            jdbcTemplate.update(SQL, read, notificationId);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

   private void saveNotificationOptions(String userName, boolean blockAll, boolean blockedUntil,
        Date block_until, boolean blockedForWork, Date start_working, Date stop_working,
        boolean eventsBlocked, boolean teamsBlocked, boolean subscriptionsBlocked) throws DatabaseException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName(USER_NOTIFICATIONOPTIONS_TABLE).usingGeneratedKeyColumns(USER_NOTIFICATIONOPTIONS_ID);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(USER_NOTIFICATIONOPTIONS_USER, userName);
        parameters.put(USER_NOTIFICATIONOPTIONS_BLOCKALL, blockAll);
        parameters.put(USER_NOTIFICATIONOPTIONS_BLOCKEDUNTIL, blockedUntil);
        parameters.put(USER_NOTIFICATIONOPTIONS_BLOCKUNTILDATE, block_until);
        parameters.put(USER_NOTIFICATIONOPTIONS_BLOCKEDFORWORK, blockedForWork);
        parameters.put(USER_NOTIFICATIONOPTIONS_STARTWORKING, start_working);
        parameters.put(USER_NOTIFICATIONOPTIONS_STOPWORKING, stop_working);
        parameters.put(USER_NOTIFICATIONOPTIONS_BLOCKEVENTS, eventsBlocked);
        parameters.put(USER_NOTIFICATIONOPTIONS_BLOCKTEAMS, teamsBlocked);
        parameters.put(USER_NOTIFICATIONOPTIONS_BLOCKSUBSCRIPTIONS, false);


        try {
            simpleJdbcInsert.execute(new MapSqlParameterSource(parameters));
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
}

   
  @Override
    public List<String> getSubscribedLocations(String subscriber) throws DatabaseException {
        try {
            String SQL = "SELECT * FROM " + USER_SUBSCRIBE_TABLE + " WHERE " + USER_SUBSCRIBE_SUBSCRIBER + " = ?";

            List<SubscribeDatabase> locations = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(SubscribeDatabase.class), subscriber);

            List<String> locationList = new ArrayList<>(locations.size());
            for(SubscribeDatabase subscribeDatabase: locations) {
                locationList.add(subscribeDatabase.getLocation());
            }
            return locationList;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<User> getSubscriber(String location) throws DatabaseException {
        try{
            String SQL = "SELECT * FROM " + USER_SUBSCRIBE_TABLE + " WHERE " + USER_SUBSCRIBE_LOCATION + " = ?";

            List<SubscribeDatabase> users = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(SubscribeDatabase.class), location);

            List<User> userList = new ArrayList<>(users.size());
            for(SubscribeDatabase subscribeDatabase: users) {
                userList.add(getUser(subscribeDatabase.getUserName()));
            }
            return userList;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void subscribe(String subscriber, String location) throws DatabaseException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName(USER_SUBSCRIBE_TABLE);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(USER_SUBSCRIBE_SUBSCRIBER, subscriber);
        parameters.put(USER_SUBSCRIBE_LOCATION, location);
        try {
            simpleJdbcInsert.execute(new MapSqlParameterSource(parameters));
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void unsubscribe(String subscriber, String location) throws DatabaseException {
        try {
            String SQL_DELETE = "DELETE FROM " + USER_SUBSCRIBE_TABLE + " WHERE " + USER_SUBSCRIBE_LOCATION + " = ? AND "
                    + USER_SUBSCRIBE_SUBSCRIBER + " = ?";

            jdbcTemplate.update(SQL_DELETE, location, subscriber);
        } catch(Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public NotificationOptions getNotificationOptions(String userName) throws DatabaseException {
        try {
            String SQL = "SELECT * FROM " + USER_NOTIFICATIONOPTIONS_TABLE + " WHERE " + USER_NOTIFICATIONOPTIONS_USER + " LIKE ? ";

            List<NotificationOptionsDatabase> options = jdbcTemplate.query(SQL,
                    new BeanPropertyRowMapper<>(NotificationOptionsDatabase.class), userName);

            if (options.size() == 0)
                return null;
            else {
                NotificationOptions notificationOptions = options.get(0).getNotificationOptions();
                return notificationOptions;
            }
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateNotificationOptions(String userName,Map<String,Object> map) throws DatabaseException {

      try {
          List<Object> values = new ArrayList<>();
          StringBuilder SQL = new StringBuilder(" UPDATE " + USER_NOTIFICATIONOPTIONS_TABLE + " SET ");
          boolean first = true;
          for(Map.Entry entry : map.entrySet()) {
              String key = (String) entry.getKey();
              if(first)
                  first = false;
              else
                  SQL.append(", ");

              SQL.append(" " + key + " = ? ");
              values.add(entry.getValue());


          }
          SQL.append(" WHERE " + USER_NOTIFICATIONOPTIONS_USER + " LIKE ? ");
          values.add(userName);

          jdbcTemplate.update(SQL.toString(), values.toArray());
      }catch(Exception e){
          throw new DatabaseException(e);
      }

    }

    /**
     * Put the hashed password into the database
     *
     * @param userName       user that updates their password
     * @param hashedPassword new hashed password
     * @throws DatabaseException
     */
    @Override
    public void saveNewPassword(String userName, String hashedPassword) throws DatabaseException {

        String SQL = "UPDATE " + USER_TABLE + " SET " + USER_PASSWORD + " = ? WHERE " + USER_NAME + " = ?";

        try {
            jdbcTemplate.update(SQL, hashedPassword, userName);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Put the new e-mail into the database
     *
     * @param userName
     * @param eMail
     * @throws DatabaseException
     */
    @Override
    public void saveNewEmail(String userName, String eMail) throws DatabaseException {

        String SQL = " UPDATE " + USER_TABLE + " SET " + USER_MAIL + " = ? WHERE " + USER_NAME + " = ? ";

        try {
            jdbcTemplate.update(SQL, eMail, userName);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Put the picture path into database
     *
     * @param userName
     * @param picturePath relative path to the picture
     * @throws DatabaseException
     */
    @Override
    public void savePicturePath(String userName, String picturePath) throws DatabaseException {
        String SQL = " UPDATE " + USER_TABLE + " SET " + USER_PICTURE + " = ? WHERE " + USER_NAME + " = ? ";

        try {
            System.out.println("username:"+userName);
            System .out.println("path: "+ picturePath);
            jdbcTemplate.update(SQL, picturePath, userName);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void createUser(String userName, String password, String mail) throws DatabaseException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName(USER_TABLE);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(USER_NAME, userName);
        parameters.put(USER_MAIL, mail);
        parameters.put(USER_PASSWORD, password);
        parameters.put(USER_PICTURE, "upload.location");

        try {
            simpleJdbcInsert.execute(new MapSqlParameterSource(parameters));
        } catch (Exception e) {
            throw new DatabaseException(e);
        }

        // set default notificationOptions for each user
        try {
            saveNotificationOptions(userName,false, false,
                    null, false, null, null,
                    false,false,false);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public User setTokenForUser(String username, String token) throws DatabaseException {
        String SQL = "UPDATE " + USER_TABLE + " SET " + USER_TOKEN + " = ? WHERE " + USER_NAME + " = ?";

        try {
            jdbcTemplate.update(SQL, token, username);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }

        return getUser(username);
    }

    @Override
    public List<User> searchUserByName(String searchword) throws DatabaseException {
        try {

            String SQL = "SELECT * FROM " + USER_TABLE + " WHERE " + USER_NAME + " LIKE ?";

            List<UserDatabase> users = jdbcTemplate.query(SQL,
                    new BeanPropertyRowMapper<>(UserDatabase.class),
                    "%"+searchword+"%");

            if (users.size() == 0)
                return new ArrayList<>();
            else {
                List<User> listOfUser = new ArrayList<>();

                for(UserDatabase a:users){
                    listOfUser.add(a.getUser());
                }
                return  listOfUser;
            }

        }catch (Exception e){
            throw new DatabaseException(e);
        }
    }

    @Override
    public void setFcmForUser(String username, String fcmToken) throws DatabaseException {
        String SQL = "UPDATE " + USER_TABLE + " SET " + USER_FCM_TOKEN + " = ? WHERE " + USER_NAME + " = ?";

        try {
            jdbcTemplate.update(SQL, fcmToken, username);
        } catch (Exception e) {

            throw new DatabaseException(e);
        }
    }


}
