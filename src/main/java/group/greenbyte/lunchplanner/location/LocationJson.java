package group.greenbyte.lunchplanner.location;

import java.io.Serializable;

/**
 * This class stores all data that can be send / received over REST API.
 * This class is used to convert JSON into java objects and java objects into json
 */
public class LocationJson implements Serializable {

    public LocationJson() { }

    public LocationJson(String locationName, double xCoordinate, double yCoordinate, String description) {
       this.locationName = locationName;
       this.xCoordinate = xCoordinate;
       this.yCoordinate = yCoordinate;
       this.description = description;
    }

    private String locationName;
    private double xCoordinate;
    private double yCoordinate;
    private String description;


    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

