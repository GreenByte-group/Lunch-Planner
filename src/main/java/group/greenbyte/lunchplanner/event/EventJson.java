package group.greenbyte.lunchplanner.event;

import java.io.Serializable;
import java.util.Date;

/**
 * This class stores all data that can be send / received over REST API.
 * This class is used to convert JSON into java objects and java objects into json
 */
public class EventJson implements Serializable {

    private static final long serialVersionUID = 465186153151351685L;

    public EventJson() { }

    public EventJson(String name, String description, int locationId, Date timeStart, Date timeEnd) {
        this.name = name;
        this.description = description;
        this.locationId = locationId;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public EventJson(String name, String description, int locationId, Date timeStart) {
        this.name = name;
        this.description = description;
        this.locationId = locationId;
        this.timeStart = timeStart;
    }

    private String name;
    private String description;
    private int locationId;
    private Date timeStart;
    private Date timeEnd;

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

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }
}
