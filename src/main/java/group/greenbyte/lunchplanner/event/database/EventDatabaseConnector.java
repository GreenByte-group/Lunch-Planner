package group.greenbyte.lunchplanner.event.database;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventDatabaseConnector extends CrudRepository<Event, Integer> {

    Event getByEventId(int eventId);

}
