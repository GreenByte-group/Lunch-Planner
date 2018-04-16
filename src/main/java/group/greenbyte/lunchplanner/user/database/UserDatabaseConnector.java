package group.greenbyte.lunchplanner.user.database;

import org.springframework.data.repository.CrudRepository;

public interface UserDatabaseConnector extends CrudRepository<User, Integer> {

    User findByUserName(String userName);

}

