package group.greenbyte.lunchplanner.location.database;

import org.springframework.data.repository.CrudRepository;

public interface LocationDatabaseConnector extends CrudRepository<Location, Integer> {

}
