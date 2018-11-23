package group.greenbyte.lunchplanner.event;

import java.io.Serializable;
import java.util.Date;

/**
 * This class stores all data that can be send / received over REST API.
 * This class is used to convert JSON into java objects and java objects into json
 */
public class EventJson implements Serializable {

    private static final long serialVersionUID = 465186153151351686L;

    public EventJson() { }

    public EventJson(String name, String description, String location, Date timeStart, boolean visible) {
        this.name = name;
        this.description = description;
        this.timeStart = timeStart;
        this.location = location;
        this.visible = visible;
    }

    public EventJson(String name, String description, String location, Date timeStart, boolean visible, String locationId, String lat, String lng) {
        this.name = name;
        this.description = description;
        this.timeStart = timeStart;
        this.location = location;
        this.visible = visible;
        this.locationId = locationId;
        this.lat = lat;
        this.lng = lng;
    }

    private String name;
    private String description;
    private Date timeStart;
    private String location;
    private boolean visible;
    private String locationId;
    private String lat;
    private String lng;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
