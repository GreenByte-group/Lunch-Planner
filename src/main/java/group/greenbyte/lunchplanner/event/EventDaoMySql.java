package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.event.database.EventDatabaseConnector;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import org.hibernate.dialect.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
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

//    @Override
//    public Event updateEvent(String userName, int eventId, String eventName,String description,
//                             int locationId, Date timeStart, Date timeEnd)throws DatabaseException{
//
//        Event event = new Event();
//        event.setEventTd(eventId);
//        event.setEventName(eventName);
//        event.setEventDescription(description);
//        event.setLocationId(locationId);
//        event.setStartDate(timeStart);
//        event.setEndDate(timeEnd);
//
//        try{
//            eventDatabaseConnector.save(event);
//            return insertEvent(userName, eventName, description, locationId, timeStart, timeEnd);
//        }catch(DatabaseException e){
//            throw new DatabaseException();
//        }
//
//
//    }

    @Override
    public Event updateEventName(int eventId, String eventName) throws DatabaseException {
        Event event = new Event();
        event.setEventTd(eventId);
        event.setEventName(eventName);

        try{
            Event eventOld = getEvent(eventId);
            event.setEventDescription(eventOld.getEventDescription());
            event.setLocationId(eventOld.getLocationId());
            event.setStartDate(eventOld.getStartDate());
            event.setEndDate(eventOld.getEndDate());

            return eventDatabaseConnector.save(event);

        }catch(DatabaseException e){
            throw new DatabaseException();
        }

    }


    @Override
    public Event updateEventDescription(int eventId, String description) throws DatabaseException {
        Event event = new Event();
        event.setEventTd(eventId);
        event.setEventDescription(description);

        try{
            Event eventOld = getEvent(eventId);
            event.setLocationId(eventOld.getLocationId());
            event.setStartDate(eventOld.getStartDate());
            event.setEndDate(eventOld.getEndDate());

            return eventDatabaseConnector.save(event);

        }catch(DatabaseException e){
            throw new DatabaseException();
        }
    }

    @Override
    public Event updateEventLocation(int eventId, int locationId) throws DatabaseException {
        Event event = new Event();
        event.setEventTd(eventId);
        event.setLocationId(locationId);

        try{
            Event eventOld = getEvent(eventId);
            event.setEventDescription(eventOld.getEventDescription());
            event.setStartDate(eventOld.getStartDate());
            event.setEndDate(eventOld.getEndDate());

            return eventDatabaseConnector.save(event);

        }catch(DatabaseException e){
            throw new DatabaseException();
        }    }

    @Override
    public Event updateEventTimeStart(int eventId, Date timeStart) throws DatabaseException {
        Event event = new Event();
        event.setEventTd(eventId);
        event.setStartDate(timeStart);


        try{
            Event eventOld = getEvent(eventId);
            event.setLocationId(eventOld.getLocationId());
            event.setEventDescription(eventOld.getEventDescription());
            event.setEndDate(eventOld.getEndDate());
            event.setEventName(eventOld.getEventName());

            return eventDatabaseConnector.save(event);

        }catch(DatabaseException e){
            throw new DatabaseException();
        }
    }

    @Override
    public Event updateEventTimeEnd(int eventId, Date timeEnd) throws DatabaseException {
        Event event = new Event();
        event.setEventTd(eventId);
        event.setEndDate(timeEnd);


        try{
            Event eventOld = getEvent(eventId);
            event.setLocationId(eventOld.getLocationId());
            event.setEventDescription(eventOld.getEventDescription());
            event.setStartDate(eventOld.getStartDate());
            event.setEventName(eventOld.getEventName());

            return eventDatabaseConnector.save(event);

        }catch(DatabaseException e){
            throw new DatabaseException();
        }
    }

}
