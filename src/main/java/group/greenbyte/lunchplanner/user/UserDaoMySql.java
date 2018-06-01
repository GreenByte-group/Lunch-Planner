package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.user.database.notifications.*;
import group.greenbyte.lunchplanner.user.database.User;
import group.greenbyte.lunchplanner.user.database.UserDatabase;
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
    public static final String USER_NOTIFICATIONOPTIONS_ID = "id";
    public static final String USER_NOTIFICATIONOPTIONS_USER = "user_name";
    public static final String USER_NOTIFICATIONOPTIONS_BLOCKALL = "block_all";
    public static final String USER_NOTIFICATIONOPTIONS_BLOCKEDUNTIL = "blocked_until";
    public static final String USER_NOTIFICATIONOPTIONS_BLOCKUNTILDATE = "block_until_date";
    public static final String USER_NOTIFICATIONOPTIONS_BLOCKEDFORWORK = "block_workingTime";
    public static final String USER_NOTIFICATIONOPTIONS_STARTWORKING = "start_working";
    public static final String USER_NOTIFICATIONOPTIONS_STOPWORKING = "end_working";
    public static final String USER_NOTIFICATIONOPTIONS_BLOCKEVENTS = "block_events";
    public static final String USER_NOTIFICATIONOPTIONS_BLOCKTEAMS = "block_teams";
    public static final String USER_NOTIFICATIONOPTIONS_BLOCKSUBSCRIPTIONS = "block_subscriptions";

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


        try {
            simpleJdbcInsert.execute(new MapSqlParameterSource(parameters));
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    private void saveNotificationOptions(String userName, boolean blockAll, boolean blockedUntil,
        Date block_until, boolean blockedForWork, Date start_working, Date stop_working,
        boolean eventsBlocked, boolean teamsBlocked, boolean subscriptionsBlocked) throws DatabaseException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName(USER_NOTIFICATION_TABLE).usingGeneratedKeyColumns(USER_NOTIFICATIONOPTIONS_ID);
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
        parameters.put(USER_NOTIFICATIONOPTIONS_BLOCKSUBSCRIPTIONS, subscriptionsBlocked);


        try {
            simpleJdbcInsert.execute(new MapSqlParameterSource(parameters));
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public NotificationOptions getNotificationOptions(String userName) throws DatabaseException {
        try {
            String SQL = "SELECT * FROM " + USER_NOTIFICATIONOPTIONS_TABLE + " WHERE " + USER_NOTIFICATIONOPTIONS_USER + " LIKE ? ";

            List<NotificationOptionsDatabase> options = jdbcTemplate.query(SQL,
                    new BeanPropertyRowMapper<>(NotificationOptionsDatabase.class),
                    userName
                    );

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
          StringBuilder SQL = new StringBuilder(" UPDATE " + USER_NOTIFICATION_TABLE + " SET ");
          boolean first = true;
          for(Map.Entry entry : map.entrySet()) {
              String key = (String) entry.getKey();

              /*switch(key) {
                  case "block_all":
                      if(first)
                          first = false;
                      else
                          SQL.append(", ");

                      SQL.append(" block_all = ? ");
                      values.add(entry.getValue());
                      break;

                  case "blocked_until":
                      if(first)
                          first = false;
                      else
                          SQL.append(", ");

                      SQL.append(" blocked_until = ? ");
                      values.add(entry.getValue());
                      break;

                  case "block_until_date":
                      if(first)
                          first = false;
                      else
                          SQL.append(", ");

                      SQL.append(" block_until_date = ? ");
                      values.add(entry.getValue());
                      break;

                  case "block_workingTime":
                      if(first)
                          first = false;
                      else
                          SQL.append(", ");

                      SQL.append(" block_workingTime = ? ");
                      values.add(entry.getValue());
                      break;

                  case "start_working":
                      if(first)
                          first = false;
                      else
                          SQL.append(", ");

                      SQL.append(" start_working = ? ");
                      values.add(entry.getValue());
                      break;

                  case "stop_working":
                      if(first)
                          first = false;
                      else
                          SQL.append(", ");

                      SQL.append(" stop_working = ? ");
                      values.add(entry.getValue());
                      break;

                  case "block_events":
                      if(first)
                          first = false;
                      else
                          SQL.append(", ");

                      SQL.append(" block_events = ? ");
                      values.add(entry.getValue());
                      break;

                  case "block_teams":
                      if(first)
                          first = false;
                      else
                          SQL.append(", ");

                      SQL.append(" block_teams = ? ");
                      values.add(entry.getValue());
                      break;

                  case "block_subscriptions":
                      if(first)
                          first = false;
                      else
                          SQL.append(", ");

                      SQL.append(" block_subscriptions = ? ");
                      values.add(entry.getValue());
                      break;
              }*/
              if(first)
                  first = false;
              else
                  SQL.append(", ");

              SQL.append(" " + key + " = ? ");
              

          }
          SQL.append(" WHERE " + USER_NOTIFICATIONOPTIONS_USER + " LIKE ? ");
          values.add(userName);

          jdbcTemplate.update(SQL.toString(), values);
      }catch(Exception e){
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
