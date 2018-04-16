package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.event.database.EventDatabaseConnector;
import group.greenbyte.lunchplanner.event.database.EventInvitation;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.location.LocationDao;
import group.greenbyte.lunchplanner.user.UserDao;
import group.greenbyte.lunchplanner.user.database.User;
import org.hibernate.dialect.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.crypto.Data;
import java.util.*;

@Repository
public class EventDaoMySql implements EventDao {

    private final EventDatabaseConnector eventDatabaseConnector;
    private final LocationDao locationDao;
    private final UserDao userDao;

    @Autowired
    public EventDaoMySql(EventDatabaseConnector eventDatabaseConnector,
                         LocationDao locationDao,
                         UserDao userDao) {
        this.eventDatabaseConnector = eventDatabaseConnector;
        this.locationDao = locationDao;
        this.userDao = userDao;
    }

    @Override
    public Event insertEvent(String userName, String eventName, String description, int locationId, Date timeStart, Date timeEnd) throws DatabaseException {
        if(userName.length() > User.MAX_USERNAME_LENGTH)
            throw new DatabaseException();

        try {
            Event event = new Event();
            event.setEventName(eventName);
            event.setEventDescription(description);
            event.setLocation(locationDao.getLocation(locationId));
            event.setStartDate(timeStart);
            event.setEndDate(timeEnd);

            User user = userDao.getUser(userName);

            EventInvitation eventInvitation = new EventInvitation();
            eventInvitation.setAdmin(true);
            eventInvitation.setConfirmed(true);
            eventInvitation.setUserInvited(user);
            eventInvitation.setEventInvited(event);

            event.addUsersInvited(eventInvitation);

            return eventDatabaseConnector.save(event);
        } catch(Exception e) {
            throw new DatabaseException();
        }
    }

    @Override
    public List<Event> search(String username, String searchword){

        //TODO (searchEvent)
        Iterable<Event> source = eventDatabaseConnector.findAll();
        List<Event> target = new ArrayList<>();
        source.forEach(target::add);
        return target;

    }

    @Override
    public Event getEvent(int eventId) throws DatabaseException{
        Optional<Event> optional = eventDatabaseConnector.findById(eventId);

        return optional.orElse(null);
    }

    @Override
    public Event updateEventName(int eventId, String eventName) throws DatabaseException {
        try{
            Event event = getEvent(eventId);
            event.setEventName(eventName);

            return eventDatabaseConnector.save(event);

        }catch(Exception e){
            throw new DatabaseException();
        }
    }

    @Override
    public Event updateEventDescription(int eventId, String description) throws DatabaseException {
        try{
            Event event = getEvent(eventId);

            event.setEventDescription(description);

            return eventDatabaseConnector.save(event);

        }catch(Exception e){
            throw new DatabaseException();
        }
    }

    @Override
    public Event updateEventLocation(int eventId, int locationId) throws DatabaseException {
        try{
            Event event = getEvent(eventId);
            //todo set locationId

            return eventDatabaseConnector.save(event);

        }catch(Exception e){
            throw new DatabaseException();
        }
    }

    @Override
    public Event updateEventTimeStart(int eventId, Date timeStart) throws DatabaseException {
        try{
            Event event = getEvent(eventId);
            event.setStartDate(timeStart);

            return eventDatabaseConnector.save(event);

        }catch(Exception e){
            throw new DatabaseException();
        }
    }

    @Override
    public Event updateEventTimeEnd(int eventId, Date timeEnd) throws DatabaseException {
        try{
            Event event = getEvent(eventId);
            event.setEndDate(timeEnd);

            return eventDatabaseConnector.save(event);

        }catch(Exception e){
            throw new DatabaseException();
        }
    }

}
