package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.Date;
import group.greenbyte.lunchplanner.excpetions.DatabaseException;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDao {

    /**
     * Insert an event into the database
     *
     * @param eventName name of the event
     * @param description description of the event
     * @param locationId id of the location
     * @param timeStart time when the event starts
     * @param timeEnd time when the events ends
     * @param createName id of the user who creates the events
     * @return the inserted Event
     *
     * @throws DatabaseException when an unexpected error happens
     */
    Event insertEvent(String eventName,
                    String description,
                    int locationId,
                    Date timeStart,
                    Date timeEnd,
                    String createName) throws DatabaseException;

}
