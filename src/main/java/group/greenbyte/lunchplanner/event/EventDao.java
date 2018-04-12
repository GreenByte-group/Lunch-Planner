package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.Date;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
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
     * @param creatorsName id of the user who creates the events
     * @return the inserted Event
     *
     * @throws DatabaseException when an unexpected error happens
     */
    Event insertEvent(String creatorsName,
                      String eventName,
                    String description,
                    int locationId,
                    Date timeStart,
                    Date timeEnd) throws DatabaseException;

}
