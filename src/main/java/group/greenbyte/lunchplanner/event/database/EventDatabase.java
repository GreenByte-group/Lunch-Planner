package group.greenbyte.lunchplanner.event.database;

import java.util.Date;

public class EventDatabase {

    private int eventId;

    private Date startDate;

    private Date endDate;

    private boolean isPublic;

    private String eventName;

    private String eventDescription;

    private int locationId;

    /**
     *
     * @return event object without location and other relation objects
     */
    public Event getEvent() {
        Event event = new Event();
        event.setEventName(eventName);
        event.setEventDescription(eventDescription);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setPublic(isPublic);
        event.setEventId(eventId);

        return event;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getLocationId() {
        return locationId;
    }
}
