package group.greenbyte.lunchplanner.event;

/**
 * This class stores all data that can be send / received over REST API.
 * This class is used to convert JSON into java objects and java objects into json
 */
public class EventJson {

    public String name;
    public String description;
    public int locationId;
    public int timeStart;
    public int timeEnd;

}
