package group.greenbyte.lunchplanner.location;

import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.location.database.Coordinate;
import group.greenbyte.lunchplanner.location.database.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LocationLogic {

    private LocationDao locationDao;

    /**
     * Create an event. At least the eventName and a location or timeStart is needed
     *
     * @param userName userName that is logged in
     * @param locationName name of the new location, not null
     * @param description description of the new location
     * @param xCoordinate the x coordinate of the new location
     * @param yCoordinate the y coordinate of the new location
     * @return the id of the new location
     * @throws HttpRequestException when userName, locationName, description not valid
     * or an Database error happens
     */

    int createLocation(String userName, String locationName, double xCoordinate,
                       double yCoordinate, String description) throws HttpRequestException{
        if(userName.length() == 0 )
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is empty");

        if(userName.length() > Location.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username too long");

        if(locationName.length() == 0)
            throw new HttpRequestException(HttpStatus.NOT_EXTENDED.value(), "Locationname is empty");

        if(locationName.length() > Location.MAX_NAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Locationname too long");

        if(description.length() > Location.MAX_DESCRIPION_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Description too long");

        try {
            return locationDao.insertLocation(locationName, new Coordinate(xCoordinate, yCoordinate), description, userName);
        } catch(DatabaseException d){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), d.getMessage());
        }


    }

    @Autowired
    public void setLocationDao(LocationDao locationDao) {
        this.locationDao = locationDao;
    }
}
