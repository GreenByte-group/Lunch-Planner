package group.greenbyte.lunchplanner.location;

import group.greenbyte.lunchplanner.location.database.Coordinate;
import group.greenbyte.lunchplanner.location.database.LocationDatabaseConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LocationDaoMySql implements LocationDao {

    private LocationDatabaseConnector locationDatabaseConnector;

    @Override
    public int insertLocation(String locationName, Coordinate coordinate, String description, String adminName) {
        return 0;
    }

    @Autowired
    public void setLocationDatabaseConnector(LocationDatabaseConnector locationDatabaseConnector) {
        this.locationDatabaseConnector = locationDatabaseConnector;
    }
}
