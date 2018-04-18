package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.event.database.EventDatabaseConnector;
import group.greenbyte.lunchplanner.event.database.EventInvitation;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.location.LocationDao;
import group.greenbyte.lunchplanner.user.UserDao;
import group.greenbyte.lunchplanner.user.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public List<Event> search(String username, String searchword) throws DatabaseException {

        //TODO (searchEvent)
        Iterable<Event> source = eventDatabaseConnector.findAll();
        List<Event> target = new ArrayList<>();
        source.forEach(target::add);
        return target;

    }

    @Override
    public List<Event> findPublicEvents(String searchword) throws DatabaseException{

        if(searchword == null || searchword.length() > Event.MAX_SEARCHWORD_LENGTH)
            throw new DatabaseException();
        if(searchword.length() == 0) {
            Iterable<Event> source = eventDatabaseConnector.findAll();
            List<Event> target = new ArrayList<>();
            source.forEach(target::add);
            return target;
        }

        List <Event> toReturn = new ArrayList<>();
        Iterable<Event> source = eventDatabaseConnector.findAll();

        for(Event t : source){
            String name = t.getEventName();
            if(name.contains(searchword))
                toReturn.add(t);
        }

        for(Event t : source){
            String location = t.getLocation().getLocationName();
            if(location.contains(searchword))
                toReturn.add(t);
        }

        for(Event t : source){
            String description = t.getEventDescription();
            if(description.contains(searchword))
                toReturn.add(t);
        }

           for(Event t : source){
            String id = t.getEventId().toString();
            if(id == searchword)
                toReturn.add(t);
        }

         return toReturn;
    }

}
