package group.greenbyte.lunchplanner.event.database;

import org.springframework.data.repository.CrudRepository;


public interface EventDatabaseConnector extends CrudRepository<Event, Integer> {

    Event getByEventId(int eventId);

}
