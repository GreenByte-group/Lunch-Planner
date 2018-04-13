package group.greenbyte.lunchplanner.event;

import java.io.Serializable;

/**
 * This class stores all data that can be send / received over REST API.
 * This class is used to convert JSON into java objects and java objects into json
 */
public class EventJson implements Serializable {

    public EventJson() { }

    public EventJson(String name, String description, int locationId, long timeStart, long timeEnd) {
        this.name = name;
        this.description = description;
        this.locationId = locationId;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    private String name;
    private String description;
    private int locationId;
    private long timeStart;
    private long timeEnd;

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

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }
}
