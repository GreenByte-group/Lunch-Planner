package group.greenbyte.lunchplanner.location;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.location.database.Coordinate;
import group.greenbyte.lunchplanner.location.database.Location;

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

    /**
     * Get the location ojbect from the locationId
     *
     * @param locationId id of the location
     * @return the object of the location or null if not exists
     * @throws DatabaseException when a database error happens
     */
    Location getLocation(int locationId) throws DatabaseException;

    /**
     * Add an admin to the location
     *
     * @param locationId
     * @param userName
     * @throws DatabaseException
     */
    void addAdminToLocation(int locationId, String userName) throws DatabaseException;

    /**
     * Checks if a user has admin privileges
     *
     * @param locationId
     * @param userName
     * @return true when the user has privileges and false if not
     * @throws DatabaseException
     */
    boolean hasAdminPrivileges(int locationId, String userName) throws DatabaseException;
}
