package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.event.database.EventDatabase;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.location.LocationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.*;

@Repository
public class EventDaoMySql implements EventDao {

    private LocationDao locationDao;

    private static final String EVENT_INVITATION_TABLE = "event_invitation";
    private static final String EVENT_INVITATION_ADMIN = "is_admin";
    private static final String EVENT_INVITATION_REPLY = "answer";
    private static final String EVENT_INVITATION_USER = "user_name";
    private static final String EVENT_INVITATION_EVENT = "event_id";

    private static final String EVENT_TABLE = "event";
    private static final String EVENT_ID = "event_id";
    private static final String EVENT_NAME = "event_name";
    private static final String EVENT_DESCRIPTION = "event_description";
    private static final String EVENT_START_DATE = "start_date";
    private static final String EVENT_END_DATE = "end_date";
    private static final String EVENT_IS_PUBLIC = "is_public";
    private static final String EVENT_LOCATION = "location_id";

    private static final String EVENT_TEAM_TABLE = "event_team_visible";
    private static final String EVENT_TEAM_TEAM = "team_id";
    private static final String EVENT_TEAM_EVENT = "event_id";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EventDaoMySql(JdbcTemplate jdbcTemplateObject,
                         LocationDao locationDao) {
        this.jdbcTemplate = jdbcTemplateObject;
        this.locationDao = locationDao;
    }

    @Override
    public Event insertEvent(String userName, String eventName, String description, int locationId, Date timeStart, Date timeEnd) throws DatabaseException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName(EVENT_TABLE).usingGeneratedKeyColumns(EVENT_ID);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(EVENT_NAME, eventName);
        parameters.put(EVENT_DESCRIPTION, description);
        parameters.put(EVENT_LOCATION, locationId);
        parameters.put(EVENT_START_DATE, timeStart);
        parameters.put(EVENT_END_DATE, timeEnd);
        parameters.put(EVENT_IS_PUBLIC, false);

        try {
            Number key = simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

            putUserInviteToEventAsAdmin(userName, key.intValue());

            return getEvent(key.intValue());
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Event getEvent(int eventId) throws DatabaseException {
        try {
            String SQL = "SELECT * FROM " + EVENT_TABLE + " WHERE " + EVENT_ID + " = ?";

            List<EventDatabase> events = jdbcTemplate.query(SQL, new Object[] {eventId}, new BeanPropertyRowMapper<>(EventDatabase.class));

            if (events.size() == 0)
                return null;
            else {
                Event event = events.get(0).getEvent();
                event.setLocation(locationDao.getLocation(events.get(0).getLocationId()));

                return event;
            }
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Event updateEventName(int eventId, String eventName) throws DatabaseException {
        String SQL = "UPDATE " + EVENT_TABLE + " SET " + EVENT_NAME + " = ? WHERE " + EVENT_ID + " = ?";

        try {
            jdbcTemplate.update(SQL, eventName, eventId);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }

        return getEvent(eventId);
    }

    @Override
    public Event updateEventDescription(int eventId, String description) throws DatabaseException {
        String SQL = "UPDATE " + EVENT_TABLE + " SET " + EVENT_DESCRIPTION + " = ? WHERE " + EVENT_ID + " = ?";

        try {
            jdbcTemplate.update(SQL, description, eventId);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }

        return getEvent(eventId);
    }

    @Override
    public Event updateEventLocation(int eventId, int locationId) throws DatabaseException {
        String SQL = "UPDATE " + EVENT_TABLE + " SET " + EVENT_LOCATION + " = ? WHERE " + EVENT_ID + " = ?";

        try {
            jdbcTemplate.update(SQL, locationId, eventId);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }

        return getEvent(eventId);
    }

    @Override
    public Event updateEventTimeStart(int eventId, Date timeStart) throws DatabaseException {
        String SQL = "UPDATE " + EVENT_TABLE + " SET " + EVENT_START_DATE + " = ? WHERE " + EVENT_ID + " = ?";

        try {
            jdbcTemplate.update(SQL, timeStart, eventId);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }

        return getEvent(eventId);
    }

    @Override
    public Event updateEventTimeEnd(int eventId, Date timeEnd) throws DatabaseException {
        String SQL = "UPDATE " + EVENT_TABLE + " SET " + EVENT_END_DATE + " = ? WHERE " + EVENT_ID + " = ?";

        try {
            jdbcTemplate.update(SQL, timeEnd, eventId);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }

        return getEvent(eventId);
    }

    @Override
    public void updateEventIsPublic(int eventId, boolean isPublic) throws DatabaseException {

    }

    @Override
    public Event putUserInviteToEvent(String userToInviteName, int eventId) throws DatabaseException {
        return putUserInvited(userToInviteName, eventId, false);
    }

    @Override
    public List<Event> findPublicEvents(String searchword) throws DatabaseException {
        try {
            String SQL = "SELECT * FROM " + EVENT_TABLE + " WHERE ((" +
                    EVENT_NAME + " LIKE ?" +
                    " OR " + EVENT_DESCRIPTION + " LIKE ?)" +
                    " AND " + EVENT_IS_PUBLIC + " = ?)";

            List<EventDatabase> events = jdbcTemplate.query(SQL,
                    new BeanPropertyRowMapper<>(EventDatabase.class),
                    "%" + searchword + "%",
                    "%" + searchword + "%",
                    1);

            List<Event> eventsReturn = new ArrayList<>(events.size());
            for(EventDatabase eventDatabase: events) {
                Event event = eventDatabase.getEvent();
                event.setLocation(locationDao.getLocation(eventDatabase.getLocationId()));

                eventsReturn.add(event);
            }

            return eventsReturn;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<Event> findEventsForTeam(int teamId, String searchword) throws DatabaseException {
        try {
            String SQL = "select " + EVENT_TEAM_TABLE + "." + EVENT_TEAM_EVENT + " from " + EVENT_TEAM_TABLE +
                    " INNER JOIN " + EVENT_TABLE + " " + EVENT_TABLE + " ON " + EVENT_TABLE + "." + EVENT_ID + " = " +
                    EVENT_TEAM_TABLE + "." + EVENT_TEAM_EVENT + " WHERE (" +
                    EVENT_NAME + " LIKE ?" +
                    " OR " + EVENT_DESCRIPTION + " LIKE ?" +
                    ") AND " + EVENT_TEAM_TEAM + " = ?";

            List<Integer> eventIds = jdbcTemplate.queryForList(SQL,
                    Integer.class,
                    "%" + searchword + "%", "%" + searchword + "%", teamId);

            List<Event> events = new ArrayList<>();

            for(Integer id : eventIds) {
                events.add(getEvent(id));
            }

            return events;
        } catch(Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<Event> findEventsUserInvited(String userName, String searchword) throws DatabaseException {
        try {
            String SQL = "select * from " + EVENT_TABLE + " inner join " + EVENT_INVITATION_TABLE + " " + EVENT_INVITATION_TABLE +
                    " on " + EVENT_TABLE + "." + EVENT_ID + " = " + EVENT_INVITATION_TABLE + "." + EVENT_INVITATION_EVENT +
                    " WHERE (" + EVENT_NAME + " LIKE ?" +
                    " OR " + EVENT_DESCRIPTION + " LIKE ?" +
                    ") AND " + EVENT_INVITATION_USER + " = ?";


            List<EventDatabase> events = jdbcTemplate.query(SQL,
                    new BeanPropertyRowMapper<>(EventDatabase.class),
                    "%" + searchword + "%", "%" + searchword + "%", userName);

            List<Event> eventsReturn = new ArrayList<>(events.size());
            for(EventDatabase eventDatabase: events) {
                Event event = eventDatabase.getEvent();
                event.setLocation(locationDao.getLocation(eventDatabase.getLocationId()));

                eventsReturn.add(event);
            }

            return eventsReturn;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void putUserInviteToEventAsAdmin(String userToInviteName, int eventId) throws DatabaseException {
        putUserInvited(userToInviteName, eventId, true);
    }

    @Override
    public void addTeamToEvent(int eventId, int teamId) throws DatabaseException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName(EVENT_TEAM_TABLE);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(EVENT_TEAM_TEAM, teamId);
        parameters.put(EVENT_TEAM_EVENT, eventId);

        try {
            simpleJdbcInsert.execute(new MapSqlParameterSource(parameters));
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean userHasAdminPrivileges(String userName, int eventId) throws DatabaseException {
        try {
            String SQL = "SELECT count(*) FROM " + EVENT_INVITATION_TABLE + " WHERE " +
                    EVENT_INVITATION_USER + " = ? " +
                    " AND " + EVENT_INVITATION_EVENT + " = ?" +
                    " AND " + EVENT_INVITATION_ADMIN + " = " + 1;

            int count = jdbcTemplate.queryForObject(SQL,
                    Integer.class,
                    userName, eventId);

            return count != 0;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean userHasPrivileges(String userName, int eventId) throws DatabaseException {
        try {
            String SQL = "SELECT count(*) FROM " + EVENT_INVITATION_TABLE + " WHERE " +
                    EVENT_INVITATION_USER + " = ? " +
                    " AND " + EVENT_INVITATION_EVENT + " = ?";

            int count = jdbcTemplate.queryForObject(SQL,
                    new Object[]{userName, eventId},
                    Integer.class);

            return count != 0;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }
      
    public void replyInvitation(String userName, int eventId, InvitationAnswer answer) throws DatabaseException {
        String SQL = "UPDATE " + EVENT_INVITATION_TABLE + " SET " + EVENT_INVITATION_REPLY + " = ? WHERE " + EVENT_INVITATION_EVENT + " = ? AND "
                + EVENT_INVITATION_USER + " = ?";

        try {
            jdbcTemplate.update(SQL, answer.getValue(), eventId, userName);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    private Event putUserInvited(String userName, int eventId, boolean admin) throws DatabaseException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName(EVENT_INVITATION_TABLE);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(EVENT_INVITATION_ADMIN, admin);
        parameters.put(EVENT_INVITATION_EVENT, eventId);
        if(admin)
            parameters.put(EVENT_INVITATION_REPLY, InvitationAnswer.ACCEPT.getValue());
        else
            parameters.put(EVENT_INVITATION_REPLY, InvitationAnswer.MAYBE.getValue());
        parameters.put(EVENT_INVITATION_USER, userName);

        try {
            Number key = simpleJdbcInsert.execute(new MapSqlParameterSource(parameters));
            return getEvent(key.intValue());
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }
}
