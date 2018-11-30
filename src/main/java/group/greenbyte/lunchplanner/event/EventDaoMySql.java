package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.*;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.user.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.*;

@Repository
public class EventDaoMySql implements EventDao {


    private static final String EVENT_BRINGSERVICE_TABLE = "bring_service";
    private static final String EVENT_BRINGSERVICE_ID = "service_id";
    private static final String EVENT_BRINGSERVICE_FOOD = "food";
    private static final String EVENT_BRINGSERVICE_EVENT = "event_id";
    private static final String EVENT_BRINGSERVICE_CREATOR = "user_name";
    private static final String EVENT_BRINGSERVICE_ACCEPTER = "accepter";
    private static final String EVENT_BRINGSERVICE_DESCRIPTION = "description";
    private static final String EVENT_BRINGSERVICE_PRICE="price";

    private static final String EVENT_INVITATION_TABLE = "event_invitation";
    private static final String EVENT_INVITATION_ADMIN = "is_admin";
    private static final String EVENT_INVITATION_REPLY = "answer";
    private static final String EVENT_INVITATION_USER = "user_name";
    private static final String EVENT_INVITATION_EVENT = "event_id";

    private static final String EVENT_COMMENT_TABLE = "comment";
    private static final String EVENT_COMMENT_USER = "user_name";
    private static final String EVENT_COMMENT_DATE = "date";
    private static final String EVENT_COMMENT_EVENT = "event_id";
    private static final String EVENT_COMMENT_ID = "comment_id";
    private static final String EVENT_COMMENT_TEXT = "comment_text";

    private static final String EVENT_TABLE = "event";
    private static final String EVENT_ID = "event_id";
    private static final String EVENT_NAME = "event_name";
    private static final String EVENT_DESCRIPTION = "event_description";
    private static final String EVENT_START_DATE = "start_date";
    private static final String EVENT_IS_PUBLIC = "is_public";
    private static final String EVENT_LOCATION = "location";
    private static final String EVENT_SHARETOKEN = "share_token";
    private static final String EVENT_LOCATION_ID = "location_id";
    private static final String EVENT_LAT = "lat";
    private static final String EVENT_LNG = "lng";

    private static final String EVENT_TEAM_TABLE = "event_team_visible";
    private static final String EVENT_TEAM_TEAM = "team_id";
    private static final String EVENT_TEAM_EVENT = "event_id";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EventDaoMySql(JdbcTemplate jdbcTemplateObject) {
        this.jdbcTemplate = jdbcTemplateObject;
    }

    @Override
    public Event insertEvent(String userName, String eventName, String description, String location, Date timeStart, boolean isPublic, String locationId, String lat, String lng) throws DatabaseException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName(EVENT_TABLE).usingGeneratedKeyColumns(EVENT_ID);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(EVENT_NAME, eventName);
        parameters.put(EVENT_DESCRIPTION, description);
        parameters.put(EVENT_START_DATE, timeStart);
        parameters.put(EVENT_IS_PUBLIC, isPublic);
        parameters.put(EVENT_LOCATION, location);
        parameters.put(EVENT_LOCATION_ID, locationId);
        parameters.put(EVENT_LAT, lat);
        parameters.put(EVENT_LNG, lng);

        try {
            Number key = simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

            putUserInviteToEventAsAdmin(userName, key.intValue());

            return getEvent(key.intValue());
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Event insertEvent(String userName, String eventName, String description, String location, Date timeStart, boolean isPublic) throws DatabaseException {
        return insertEvent(userName, eventName, description, location, timeStart, isPublic, null, null, null);
    }

    @Override
    public void deleteInvitationsForEvent(int eventId) throws DatabaseException {
        try {
            String SQL_DELETE = "DELETE FROM " + EVENT_INVITATION_TABLE + " WHERE " + EVENT_INVITATION_EVENT + " = ?";

            jdbcTemplate.update(SQL_DELETE, eventId);
        } catch(Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteUserInvitation(String username) throws DatabaseException {
        System.out.println("DAO SQL deleteUserinvatation: "+username);
        try {
            String SQL_DELETE = "DELETE FROM " + EVENT_INVITATION_TABLE + " WHERE " + EVENT_INVITATION_USER + " = ?";

            jdbcTemplate.update(SQL_DELETE, username);
        } catch(Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteBringServiceForEvent(int eventId) throws DatabaseException {
        try {
            String SQL_DELETE = "DELETE FROM " + EVENT_BRINGSERVICE_TABLE + " WHERE " + EVENT_BRINGSERVICE_EVENT + " = ?";

            jdbcTemplate.update(SQL_DELETE, eventId);
        } catch(Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteUserBringservice(String username) throws DatabaseException {
        System.out.println("DAO SQL deleteBringService: "+username);
        try {
            String SQL_DELETE = "DELETE FROM " + EVENT_BRINGSERVICE_TABLE + " WHERE " + EVENT_BRINGSERVICE_CREATOR + " = ?";

            jdbcTemplate.update(SQL_DELETE, username);
        } catch(Exception e) {
            throw new DatabaseException(e);
        }

        try {
            String SQL_DELETE2 = "DELETE FROM " + EVENT_BRINGSERVICE_TABLE + " WHERE " + EVENT_BRINGSERVICE_ACCEPTER + " = ?";

            jdbcTemplate.update(SQL_DELETE2, username);
        } catch(Exception e) {
            throw new DatabaseException(e);
        }

    }


    @Override
    public void deleteCommentsForEvent(int eventId) throws DatabaseException {
        try {
            String SQL_DELETE = "DELETE FROM " + EVENT_COMMENT_TABLE + " WHERE " + EVENT_COMMENT_EVENT + " = ?";

            jdbcTemplate.update(SQL_DELETE, eventId);
        } catch(Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteUserComments(String username) throws DatabaseException {
        System.out.println("DAO SQL deleteUserComments: "+username);
        try {
            String SQL_DELETE = "DELETE FROM " + EVENT_COMMENT_TABLE + " WHERE " + EVENT_COMMENT_USER + " = ?";

            jdbcTemplate.update(SQL_DELETE, username);
        } catch(Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void deleteEvent(int eventId) throws DatabaseException {
        System.out.println("DELETENOW DAO");

        deleteInvitationsForEvent(eventId);
        deleteBringServiceForEvent(eventId);
        deleteCommentsForEvent(eventId);

        try {
            String SQL = "DELETE FROM " + EVENT_TABLE + " WHERE " + EVENT_ID + " = ?";

            jdbcTemplate.update(SQL, eventId);
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
                event.setInvitations(new HashSet<>(getInvitations(eventId)));

                return event;
            }
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public int getNumberParticipant(int eventId) throws DatabaseException{
        try{
            String SQL = "SELECT count(*) FROM " + EVENT_INVITATION_TABLE + " WHERE " + EVENT_INVITATION_EVENT + " = ?";

            Integer count = jdbcTemplate.queryForObject(SQL,
                    Integer.class,
                    eventId);
            System.out.println("ANZAHL IST: " + count);
            return count;
        }catch(Exception e){
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<Event> getAllEvents() throws DatabaseException {
        try {
            String SQL = "SELECT * FROM " + EVENT_TABLE;

            List<EventDatabase> events = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(EventDatabase.class));

            List<Event> eventsToReturn = new ArrayList<>();

            for(EventDatabase event : events) {
                eventsToReturn.add(event.getEvent());
            }

            return eventsToReturn;
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
    public Event updateEventLocation(int eventId, String location) throws DatabaseException {
        String SQL = "UPDATE " + EVENT_TABLE + " SET " + EVENT_LOCATION + " = ? WHERE " + EVENT_ID + " = ?";

        try {
            jdbcTemplate.update(SQL, location, eventId);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }

        return getEvent(eventId);
    }

    @Override
    public Event updateEventLocationCoordinates(int eventId, String lat, String lng, String placeId) throws DatabaseException {

        String SQL1 = "UPDATE " + EVENT_TABLE + " SET " + EVENT_LAT + " = ? WHERE " + EVENT_ID + " = ?";
        String SQL2 = "UPDATE " + EVENT_TABLE + " SET " + EVENT_LNG + " = ? WHERE " + EVENT_ID + " = ?";
        String SQL3 = "UPDATE " + EVENT_TABLE + " SET " + EVENT_LOCATION_ID + " = ? WHERE " + EVENT_ID + " = ?";

        try {
            jdbcTemplate.update(SQL1, lat, eventId);
            jdbcTemplate.update(SQL2, lng, eventId);
            jdbcTemplate.update(SQL3, placeId, eventId);
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
    public void updateEventIsPublic(int eventId, boolean isPublic) throws DatabaseException {
        String SQL = "UPDATE " + EVENT_TABLE + " SET " + EVENT_IS_PUBLIC + " = ? WHERE " + EVENT_ID + " = ?";

        try {

            jdbcTemplate.update(SQL, isPublic, eventId);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
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

                event.setInvitations(new HashSet<>(getInvitations(event.getEventId())));

                eventsReturn.add(event);
            }
            sortByDate(eventsReturn);

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
                Event event = getEvent(id);

                events.add(event);
            }
            sortByDate(events);

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

                event.setInvitations(new HashSet<>(getInvitations(event.getEventId())));

                eventsReturn.add(event);
            }
            sortByDate(eventsReturn);

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
    public boolean isEventPublic(int eventId) throws DatabaseException {
        try {
            String SQL = "SELECT " + EVENT_IS_PUBLIC + " FROM " + EVENT_TABLE + " WHERE " +
                    EVENT_ID + " = ?";

            Boolean isPublic = jdbcTemplate.queryForObject(SQL, Boolean.class, eventId);

            if(isPublic == null)
                return false;

            return isPublic;
        } catch(Exception e) {
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

            Integer count = jdbcTemplate.queryForObject(SQL,
                    Integer.class,
                    userName, eventId);

            return count != 0;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public boolean userHasPrivileges(String userName, int eventId) throws DatabaseException {
        System.out.println("ADMIN DAO");

        if(isEventPublic(eventId))
            return true;

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

    @Override
    public List<Comment> getAllComments(int eventId) throws DatabaseException {
        try {
            String SQL = "SELECT * FROM " + EVENT_COMMENT_TABLE + " WHERE " +
                    EVENT_COMMENT_EVENT + " = ?";

            List<CommentDatabase> comments = jdbcTemplate.query(SQL,
                    new BeanPropertyRowMapper<>(CommentDatabase.class),
                    eventId);

            List<Comment> commentsReturn = new ArrayList<>(comments.size());
            for(CommentDatabase commentDatabase : comments){
                Comment comment = commentDatabase.getComment();

                commentsReturn.add(comment);
            }

        return commentsReturn;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void putService(String creater, int eventId, String food, String description)throws DatabaseException{
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName(EVENT_BRINGSERVICE_TABLE).usingGeneratedKeyColumns(EVENT_BRINGSERVICE_ID);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(EVENT_BRINGSERVICE_FOOD, food);
        parameters.put(EVENT_BRINGSERVICE_EVENT, eventId);
        parameters.put(EVENT_BRINGSERVICE_CREATOR, creater);
        parameters.put(EVENT_BRINGSERVICE_ACCEPTER, null);
        parameters.put(EVENT_BRINGSERVICE_DESCRIPTION, description);
        parameters.put(EVENT_BRINGSERVICE_PRICE, 0);

        try{
            simpleJdbcInsert.execute(new MapSqlParameterSource(parameters));
        }catch(Exception e){
            throw new DatabaseException(e);
        }
    }

    @Override
    public void updateBringservice(int eventId,String accepter, int serviceId, String price) throws DatabaseException{
            try {
                String SQL1 = " UPDATE " + EVENT_BRINGSERVICE_TABLE +
                        " SET " + EVENT_BRINGSERVICE_ACCEPTER + " = ? WHERE " + EVENT_BRINGSERVICE_ID + " = ? " +
                        "AND " + EVENT_BRINGSERVICE_EVENT + " = ? ";
                jdbcTemplate.update(SQL1, accepter, serviceId, eventId);

                String SQL2 = " UPDATE " + EVENT_BRINGSERVICE_TABLE +
                        " SET " + EVENT_BRINGSERVICE_PRICE + " = ? WHERE " + EVENT_BRINGSERVICE_ID + " = ? " +
                        "AND " + EVENT_BRINGSERVICE_EVENT + " = ? ";
                jdbcTemplate.update(SQL2, price, serviceId, eventId);

            }catch(Exception e){
                throw new DatabaseException(e);
            }

    }

    @Override
    public List<BringService> getService(int eventId) throws DatabaseException{
        try{
            String SQL = "SELECT * FROM " + EVENT_BRINGSERVICE_TABLE + " WHERE " +
                    EVENT_BRINGSERVICE_EVENT + " = ? ";

            List<BringServiceDatabase> serviceList = jdbcTemplate.query(SQL,
                    new BeanPropertyRowMapper<>(BringServiceDatabase.class),
                    eventId);

            List<BringService> serviceReturn = new ArrayList<>(serviceList.size());
            for(BringServiceDatabase bringServiceDatabase : serviceList){
                BringService bringService = bringServiceDatabase.getBringService();
                serviceReturn.add(bringService);
            }

            return serviceReturn;
        }catch(Exception e){
            throw new DatabaseException(e);
        }
    }

    @Override
    public BringService getOneService(int serviceId) throws DatabaseException {
        try{
            String SQL = "SELECT * FROM " + EVENT_BRINGSERVICE_TABLE + " WHERE " +
                    EVENT_BRINGSERVICE_ID + " = ? ";

            List<BringServiceDatabase> serviceList = jdbcTemplate.query(SQL,
                    new BeanPropertyRowMapper<>(BringServiceDatabase.class),
                    serviceId);

            return serviceList.get(0).getBringService();
        }catch(Exception e){
            throw new DatabaseException(e);
        }
    }

    @Override
    public void setNewAdmin(int eventId, String newAdmin) throws DatabaseException{
        try{
            System.out.println("AAAAAAAAAAAAMMMMMMMMMMMMMMMMKKKKKKKKKKKKKKK: "+eventId+newAdmin);
            String SQL = "UPDATE " + EVENT_INVITATION_TABLE + " SET " + EVENT_INVITATION_ADMIN+ " = ? WHERE " + EVENT_ID + " = ?" +
                    " AND " + EVENT_INVITATION_USER + " = ? ";

            jdbcTemplate.update(SQL, 1, eventId, newAdmin);
        }catch(Exception e){
            throw new DatabaseException(e);
        }
    }

    @Override
    public void putCommentForEvent(String userName, int eventId, String comment) throws DatabaseException {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName(EVENT_COMMENT_TABLE).usingGeneratedKeyColumns(EVENT_COMMENT_ID);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(EVENT_COMMENT_USER, userName);
        parameters.put(EVENT_COMMENT_EVENT, eventId);
        parameters.put(EVENT_COMMENT_DATE, new Date());
        parameters.put(EVENT_COMMENT_TEXT, comment);

        try{
            simpleJdbcInsert.execute(new MapSqlParameterSource(parameters));
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<EventInvitationDataForReturn> getInvitations(int eventId) throws DatabaseException {
        try {
            String SQL = "SELECT * FROM " + EVENT_INVITATION_TABLE + " WHERE " +
                    EVENT_INVITATION_EVENT + " = ?";

            return jdbcTemplate.query(SQL,
                    new BeanPropertyRowMapper<>(EventInvitationDataForReturn.class),
                    eventId);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    private boolean isInvited(int eventId, String username) throws DatabaseException {
        try {
            String SQL = "SELECT count(*) FROM " + EVENT_INVITATION_TABLE + " WHERE " +
                    EVENT_INVITATION_USER + " = ? " +
                    " AND " + EVENT_INVITATION_EVENT + " = ?";

            Integer count = jdbcTemplate.queryForObject(SQL,
                    Integer.class,
                    username, eventId);

            return count != 0;
        } catch(Exception e) {
            throw new DatabaseException(e);
        }
    }

    public List<User> getReply(int eventId) throws DatabaseException{
        return null;
    }

    public void replyInvitation(String userName, int eventId, InvitationAnswer answer) throws DatabaseException {
        if(answer != InvitationAnswer.REJECT) {
            if(!isInvited(eventId, userName)) {
                putUserInvited(userName, eventId, false);
            }
            String SQL = "UPDATE " + EVENT_INVITATION_TABLE + " SET " + EVENT_INVITATION_REPLY + " = ? WHERE " + EVENT_INVITATION_EVENT + " = ? AND "
                    + EVENT_INVITATION_USER + " = ?";

            try {
                jdbcTemplate.update(SQL, answer.getValue(), eventId, userName);
            } catch (Exception e) {
                throw new DatabaseException(e);
            }
        } else {

            String SQL ="";
            if(userHasAdminPrivileges(userName, eventId)){
                giveEventNewAdmin(userName,eventId);
                SQL = "DELETE FROM " + EVENT_INVITATION_TABLE + " WHERE " + EVENT_INVITATION_EVENT + " = ? AND "
                        + EVENT_INVITATION_USER + " = ?";
            }else{
                SQL = "DELETE FROM " + EVENT_INVITATION_TABLE + " WHERE " + EVENT_INVITATION_EVENT + " = ? AND "
                        + EVENT_INVITATION_USER + " = ?";
            }
            try {
                jdbcTemplate.update(SQL, eventId, userName);
            } catch (Exception e) {
                throw new DatabaseException(e);
            }
        }
    }

    @Override
    public void addShareToken(int eventId, String shareToken) throws DatabaseException {
        String SQL = "UPDATE " + EVENT_TABLE + " SET " + EVENT_SHARETOKEN + " = ? WHERE " + EVENT_ID + " = ?";

        try {
            jdbcTemplate.update(SQL, shareToken, eventId);
        } catch(Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Event getEventByShareToken(String token) throws DatabaseException {
        try {
            String SQL = "SELECT * FROM " + EVENT_TABLE + " WHERE " +
                    EVENT_SHARETOKEN + " = ?";

            List<EventDatabase> events = jdbcTemplate.query(SQL,
                    new BeanPropertyRowMapper<>(EventDatabase.class),
                    token);

            if (events.size() == 0)
                return null;
            else {
                Event event = events.get(0).getEvent();
                event.setInvitations(new HashSet<>(getInvitations(event.getEventId())));

                return event;
            }
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    private void giveEventNewAdmin(String userName, int eventId) throws DatabaseException{
        Event check = getEvent(eventId);
        Set<EventInvitationDataForReturn> list = check.getInvitations();
        System.out.println("NO NO NO "+list.toArray().length);
       try{
           if(list.toArray().length > 1){
               for(EventInvitationDataForReturn tile : list){
                   if(tile.getUserName() != userName){
                       System.out.println("HIER ODER: " + tile.getUserName() + "Ad: "+tile.isAdmin());
                       setNewAdmin(eventId,tile.getUserName());
//                       putUserInviteToEventAsAdmin(tile.getUserName(), eventId);
                   }
               }
           }
       }catch (Exception e){
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

    private void sortByDate(List<Event> events){
        //eventsReturn.sort((e1, e2) -> e1.getStartDate().compareTo(e2.getStartDate()));
        events.sort(Comparator.comparing(Event::getStartDate));
    }


}
