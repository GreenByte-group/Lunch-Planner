package group.greenbyte.lunchplanner.location;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.location.database.Coordinate;

public interface LocationDao {

    /**
     * Insert a location into the database
     *
     * @param locationName name of the location
     * @param coordinate coordinates of the location
     * @param description description of the location
     * @param adminName name of the creater of the location
     * @return the locationId created by the database
     */
    int insertLocation(String locationName, Coordinate coordinate, String description,
                       String adminName) throws DatabaseException;

}
