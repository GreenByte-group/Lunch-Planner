package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.event.database.EventDatabaseConnector;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class EventDaoMySql implements EventDao {

    private final EventDatabaseConnector eventDatabaseConnector;

    @Autowired
    public EventDaoMySql(EventDatabaseConnector eventDatabaseConnector) {
        this.eventDatabaseConnector = eventDatabaseConnector;
    }

    @Override
    public Event insertEvent(String userName, String eventName, String description, int locationId, Date timeStart, Date timeEnd) throws DatabaseException {
        Event event = new Event();
        event.setEventName(eventName);
        event.setEventDescription(description);
        event.setLocationId(locationId);
        event.setStartDate(timeStart);
        event.setEndDate(timeEnd);

        try {
            return eventDatabaseConnector.save(event);
        } catch(Exception e) {
            throw new DatabaseException();
        }
    }

    @Override
    public List<Event> getAll(String username, String searchword) throws DatabaseException{

        //toDo (searchEvent)
        Iterable<Event> source = eventDatabaseConnector.findAll();
        List<Event> target = new ArrayList<>();
        source.forEach(target::add);
        return target;

    }

    @Override
    public Event getEvent(int eventId) throws DatabaseException{
        Optional<Event> eventList = eventDatabaseConnector.findById(eventId);
        Object event = eventList.get();
        return (Event) event;
    }

    @Override
    public Event updateEvent(String userName, int eventId, String eventName,String description,
                             int locationId, Date timeStart, Date timeEnd)throws DatabaseException{

        Event event = new Event();
        event.setEventName(eventName);
        event.setEventDescription(description);
        event.setLocationId(locationId);
        event.setStartDate(timeStart);
        event.setEndDate(timeEnd);


        try{

            eventDatabaseConnector.save(event);
            return insertEvent(userName, eventName, description, locationId, timeStart, timeEnd);
        }catch(DatabaseException e){
            throw new DatabaseException();
        }


    }

    @Override
    public Event updateEventName(int eventId, String eventName) throws DatabaseException {
        return null;
    }


    @Override
    public Event updateEventDescription(int eventId, String eventName, String description) throws DatabaseException {
        return null;
    }

    @Override
    public Event updateEventLocation(int eventId, String eventName, int locationId) throws DatabaseException {
        return null;
    }

    @Override
    public Event updateEventTimeStart(int eventId, Date timeStart) throws DatabaseException {
        return null;
    }

    @Override
    public Event updateEventTimeEnd(int eventId, Date timeEnd) throws DatabaseException {
        return null;
    }

}
