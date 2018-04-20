package group.greenbyte.lunchplanner.location;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.location.database.Coordinate;
import group.greenbyte.lunchplanner.location.database.Location;
import group.greenbyte.lunchplanner.location.database.LocationAdmin;
import group.greenbyte.lunchplanner.location.database.LocationDatabaseConnector;
import group.greenbyte.lunchplanner.user.UserDao;
import group.greenbyte.lunchplanner.user.database.User;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class LocationDaoMySql implements LocationDao {

    private LocationDatabaseConnector locationDatabaseConnector;
    private UserDao userDao;

    @Transactional
    @Override
    public int insertLocation(String locationName, Coordinate coordinate, String description,
                              String adminName) throws DatabaseException {

        if(adminName.length() > User.MAX_USERNAME_LENGTH)
            throw new DatabaseException();

        try {
            Location location = new Location();
            location.setLocationName(locationName);
            location.setLocationDescription(description);
            location.setCoordinate(coordinate);

            User user = userDao.getUser(adminName);

            Hibernate.initialize(user.getLocationAdmin());

            LocationAdmin locationAdmin = new LocationAdmin();
            locationAdmin.setUserAdmin(user);
            locationAdmin.setLocation(location);

            user.addLocationAdmin(locationAdmin);
            //location.addLocationAdmin(locationAdmin);

            return locationDatabaseConnector.save(location).getLocationId();
        } catch(Exception e) {
            throw new DatabaseException();
        }
    }

    @Override
    public Location getLocation(int locationId) throws DatabaseException {
        try {
            Optional<Location> optional = locationDatabaseConnector.findById(locationId);

            return optional.orElse(null);
        } catch(Exception e) {
            throw new DatabaseException();
        }
    }

    @Autowired
    public void setLocationDatabaseConnector(LocationDatabaseConnector locationDatabaseConnector,
                                             UserDao userDao) {
        this.locationDatabaseConnector = locationDatabaseConnector;
        this.userDao = userDao;
    }
}
