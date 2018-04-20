package group.greenbyte.lunchplanner.user.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserDatabaseConnector extends JpaRepository<User, String> {

    @Query("SELECT u from User u where u.userName = ?1")
    User findUserByUserName(String userName);

}

