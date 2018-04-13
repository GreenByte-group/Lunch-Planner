package group.greenbyte.lunchplanner.location;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.location.database.Coordinate;
import group.greenbyte.lunchplanner.location.database.Location;
import group.greenbyte.lunchplanner.location.database.LocationDatabaseConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LocationDaoMySql implements LocationDao {

    private LocationDatabaseConnector locationDatabaseConnector;

    @Override
    public int insertLocation(String locationName, Coordinate coordinate, String description,
                              String adminName) throws DatabaseException {
        Location location = new Location();
        location.setLocationName(locationName);
        location.setLocationDescription(description);
        location.setCoordinate(coordinate);

        //TODO admin name

        try {
            return locationDatabaseConnector.save(location).getLocationId();
        } catch(Exception e) {
            throw new DatabaseException();
        }
    }

    @Autowired
    public void setLocationDatabaseConnector(LocationDatabaseConnector locationDatabaseConnector) {
        this.locationDatabaseConnector = locationDatabaseConnector;
    }
}
