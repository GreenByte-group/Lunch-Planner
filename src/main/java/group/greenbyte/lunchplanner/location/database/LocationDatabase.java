package group.greenbyte.lunchplanner.location.database;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class LocationDatabase {

    private int locationId;

    private String locationName;

    private String locationDescription;

    private double x_coordinate;

    private double y_coordinate;

    private boolean isPublic;

    /**
     * @return Location object without the data for all relations
     */
    public Location getLocation() {
        Location location = new Location();
        location.setLocationName(locationName);
        location.setLocationDescription(locationDescription);
        location.setLocationId(locationId);
        location.setPublic(isPublic);
        location.setCoordinate(new Coordinate(x_coordinate, y_coordinate));

        return location;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public void setX_coordinate(double x_coordinate) {
        this.x_coordinate = x_coordinate;
    }

    public void setY_coordinate(double y_coordinate) {
        this.y_coordinate = y_coordinate;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public double getX_coordinate() {
        return x_coordinate;
    }

    public double getY_coordinate() {
        return y_coordinate;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
