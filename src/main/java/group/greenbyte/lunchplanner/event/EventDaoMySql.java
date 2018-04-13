package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.event.database.EventDatabaseConnector;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

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

}
