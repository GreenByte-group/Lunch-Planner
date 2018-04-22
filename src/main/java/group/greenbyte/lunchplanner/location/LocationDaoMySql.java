package group.greenbyte.lunchplanner.location;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.event.database.EventDatabase;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.location.database.Coordinate;
import group.greenbyte.lunchplanner.location.database.Location;
import group.greenbyte.lunchplanner.location.database.LocationDatabase;
import group.greenbyte.lunchplanner.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LocationDaoMySql implements LocationDao {

    private UserDao userDao;

    private final JdbcTemplate jdbcTemplate;

    private static final String LOCATION_TABLE = "location";
    private static final String LOCATION_NAME = "location_name";
    private static final String LOCATION_DESCRIPTION = "location_description";
    private static final String LOCATION_PUBLIC = "is_public";
    private static final String LOCATION_XCOORDINATE = "x_coordinate";
    private static final String LOCATION_YCOORDINATE = "y_coordinate";
    private static final String LOCATION_ID = "location_id";

    private static final String LOCATION_ADMIN_TABLE = "location_admin";
    private static final String LOCATION_ADMIN_USER = "user_name";
    private static final String LOCATION_ADMIN_LOCATION_ID = "location_id";

    @Autowired
    public LocationDaoMySql(JdbcTemplate jdbcTemplate, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }

    @Override
    public int insertLocation(String locationName, Coordinate coordinate, String description,
                              String adminName) throws DatabaseException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName(LOCATION_TABLE).usingGeneratedKeyColumns(LOCATION_ID);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(LOCATION_NAME, locationName);
        parameters.put(LOCATION_DESCRIPTION, description);
        parameters.put(LOCATION_XCOORDINATE, coordinate.getxCoordinate());
        parameters.put(LOCATION_YCOORDINATE, coordinate.getyCoordinate());

        try {
            Number key = simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

            addAdminToLocation(key.intValue(), adminName);

            return key.intValue();
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Location getLocation(int locationId) throws DatabaseException {
        try {
            String SQL = "SELECT * FROM " + LOCATION_TABLE + " WHERE " + LOCATION_ID + " = ?";

            List<LocationDatabase> locations = jdbcTemplate.query(SQL,
                    new BeanPropertyRowMapper<>(LocationDatabase.class),
                    locationId);

            if (locations.size() == 0)
                return null;
            else
                return locations.get(0).getLocation();
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void addAdminToLocation(int locationId, String userName) throws DatabaseException {
        try {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            simpleJdbcInsert.withTableName(LOCATION_ADMIN_TABLE);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put(LOCATION_ADMIN_USER, userName);
            parameters.put(LOCATION_ADMIN_LOCATION_ID, locationId);

            simpleJdbcInsert.execute(new MapSqlParameterSource(parameters));
        } catch(Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean hasAdminPrivileges(int locationId, String userName) throws DatabaseException {
        try {
            String SQL = "SELECT count(*) FROM "  + LOCATION_ADMIN_TABLE + " WHERE " +
                    LOCATION_ADMIN_LOCATION_ID + " = ? AND " +
                    LOCATION_ADMIN_USER + " = ?";

            int count = jdbcTemplate.queryForObject(SQL,
                    Integer.class,
                    locationId, userName);

            return count != 0;
        } catch (Exception e)  {
            throw new DatabaseException(e);
        }
    }
}
