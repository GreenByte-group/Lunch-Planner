package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import org.hibernate.dialect.Database;

import java.util.Date;
import java.util.List;

public interface EventDao {

    /**
     * Insert an event into the database
     *
     * @param userName id of the user who creates the events
     * @param eventName name of the event
     * @param description description of the event
     * @param locationId id of the location
     * @param timeStart time when the event starts
     * @param timeEnd time when the events ends
     * @return the inserted Event
     *
     * @throws DatabaseException when an unexpected error happens
     */
    Event insertEvent(String userName,
                      String eventName,
                      String description,
                      int locationId,
                      Date timeStart,
                      Date timeEnd) throws DatabaseException;

    /**
     *
     * @param username name of the current session
     * @param searchword entity with this particular String
     * @return a list of events wich containes the searchword
     * @throws DatabaseException when an unexpected error happens
     */
    List<Event> getAll(String username,
                       String searchword)throws DatabaseException;

    /**
     *
     * @param eventId id of the event
     * @return the searched event
     * @throws DatabaseException when an unexpected error happens
     */
    Event getEvent(int eventId) throws DatabaseException;

    /**
     *
     * @param userName id of the user who creates the events
     * @param eventName name of the event
     * @param description description of the event
     * @param locationId id of the location
     * @param timeStart time when the event starts
     * @param timEnd time when the events ends
     * @return the updated event
     * @throws DatabaseException when an unexpected error happens
     */
    Event updateEvent(String userName,
                      int eventId,
                      String eventName,
                      String description,
                      int locationId,
                      Date timeStart,
                      Date timEnd
                      ) throws DatabaseException;

    /**
     *
     * @param eventId id of the event
     * @param eventName name of the event
     * @return the updated event
     * @throws DatabaseException
     */
    Event updateEventName(int eventId,
                          String eventName) throws DatabaseException;


    /**
     *
     * @param eventId id of the event
     * @param eventName name of the event
     * @param description description of the event
     * @return the updated event
     * @throws DatabaseException when an unexpected error happens
     */
    Event updateEventDescription(int eventId,
                                 String eventName,
                                 String description
                                 ) throws DatabaseException;

    /**
     *
     * @param eventId id of the event
     * @param eventName name of the event
     * @param locationId id of the location
     * @return the updated event
     * @throws DatabaseException when an unexpected error happens
     */
    Event updateEventLocation(int eventId,
                              String eventName,
                              int locationId) throws DatabaseException;

    /**
     *
     * @param eventId id of the event
     * @param timeStart time when the event starts
     * @return the updated event
     * @throws DatabaseException when an unexpected error happens
     */
    Event updateEventTimeStart(int eventId,
                               Date timeStart) throws DatabaseException;

    /**
     *
     * @param eventId id of the event
     * @param timeEnd time when the events ends
     * @return the updated event
     * @throws DatabaseException when an unexpected error happens
     */
    Event updateEventTimeEnd(int  eventId,
                             Date timeEnd) throws DatabaseException;

}
