package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;

import java.util.Date;
import java.util.List;

public interface EventDao {

    /**
     * Insert an event into the database
     *
     * @param eventName name of the event
     * @param description description of the event
     * @param locationId id of the location
     * @param timeStart time when the event starts
     * @param timeEnd time when the events ends
     * @param userName id of the user who creates the events
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
     * TODO write tests
     * Searchs for events
     *
     * @param username who searches
     * @param searchword for what the user is searching
     * @return a list of events matching the search
     * @throws DatabaseException when an error happens
     */
    List<Event> search(String username,
                       String searchword)throws DatabaseException;

}
