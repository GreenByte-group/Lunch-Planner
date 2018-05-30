package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.user.database.NotificationDatabase;
import group.greenbyte.lunchplanner.user.database.Notifications;
import group.greenbyte.lunchplanner.user.database.User;
import group.greenbyte.lunchplanner.user.database.UserDatabase;
import org.hibernate.dialect.Database;
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

    public static final String USER_NOTIFICATION_TABLE = "notifications";
    public static final String USER_NOTIFICATION_ID = "notificationsId";
    public static final String USER_NOTIFICATION_TITEL = "titel";
    public static final String USER_NOTIFICATION_MESSAGE = "message";
    public static final String USER_NOTIFICATION_RECEIVER = "receiver";
    public static final String USER_NOTIFICATION_BUILDER = "builder";
    public static final String USER_NOTIFICATION_LINK = "link";
    public static final String USER_NOTIFICATION_PICTURE = "picture";
    public static final String USER_NOTIFICATION_DATE = "date";

    public static final String USER_NOTIFICATIONOPTIONS_TABLE = "notificationOptions";
    public static final String USER_NOTIFICATIONOPTIONS_ID = "user_name";
    public static final String USER_NOTIFICATIONOPTIONS_OPTION1 = "blocked_permanently";
    public static final String USER_NOTIFICATIONOPTIONS_OPTION2 = "blocked_today";
    public static final String USER_NOTIFICATIONOPTIONS_OPTION2_DATE = "date";
    public static final String USER_NOTIFICATIONOPTIONS_OPTION3 = "blocked_timeInterval";
    public static final String USER_NOTIFICATIONOPTIONS_OPTION3_START = "start";
    public static final String USER_NOTIFICATIONOPTIONS_OPTION3_END = "end";



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
        String SQL = " SELECT * FROM " + USER_NOTIFICATION_TABLE + " WHERE " + USER_NOTIFICATION_BUILDER + " = ?";

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
            , String builder, String linkToClick, String picturePath) throws DatabaseException{

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


        try {
            simpleJdbcInsert.execute(new MapSqlParameterSource(parameters));
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

        try {
            simpleJdbcInsert.execute(new MapSqlParameterSource(parameters));
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
