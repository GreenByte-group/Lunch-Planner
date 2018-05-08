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

    public EventJson(String name, String description, String location, Date timeStart) {
        this.name = name;
        this.description = description;
        this.timeStart = timeStart;
        this.location = location;
    }

    private String name;
    private String description;
    private Date timeStart;
    private String location;

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
}
