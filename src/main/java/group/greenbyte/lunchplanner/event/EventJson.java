package group.greenbyte.lunchplanner.event;

import java.io.Serializable;
import java.util.Date;

/**
 * This class stores all data that can be send / received over REST API.
 * This class is used to convert JSON into java objects and java objects into json
 */
public class EventJson implements Serializable {

<<<<<<< HEAD
    private static final long serialVersionUID = 465186153151351685L;

    public EventJson() { }

    public EventJson(String name, String description, int locationId, Date timeStart, Date timeEnd) {
        this.name = name;
        this.description = description;
        this.locationId = locationId;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
=======
    private static final long serialVersionUID = 465186153151351686L;

    public EventJson() { }

    public EventJson(String name, String description, String location, Date timeStart) {
        this.name = name;
        this.description = description;
        this.timeStart = timeStart;
        this.location = location;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    }

    private String name;
    private String description;
<<<<<<< HEAD
    private int locationId;
    private Date timeStart;
    private Date timeEnd;
=======
    private Date timeStart;
    private String location;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

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

<<<<<<< HEAD
    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

=======
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

<<<<<<< HEAD
    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
=======
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    }
}
