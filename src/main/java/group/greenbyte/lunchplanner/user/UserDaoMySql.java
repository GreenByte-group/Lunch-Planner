package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.user.database.User;
import group.greenbyte.lunchplanner.user.database.UserDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoMySql implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public static final String USER_TABLE = "user";
    public static final String USER_NAME = "user_name";
    public static final String USER_MAIL = "e_mail";
    public static final String USER_PASSWORD = "password";
    public static final String USER_TOKEN = "token";
    public static final String USER_FCM_TOKEN = "fcm_token";

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
