package group.greenbyte.lunchplanner.event.database;

import java.util.Date;

public class EventDatabase {

    private int eventId;

    private Date startDate;

    private boolean isPublic;

    private String eventName;

    private String eventDescription;

    private String location;

    private String locationId;

    private String lat;

    private String lng;

    private String shareToken;

    /**
     *
     * @return event object without location and other relation objects
     */
    public Event getEvent() {
        Event event = new Event();
        event.setEventName(eventName);
        event.setEventDescription(eventDescription);
        event.setStartDate(startDate);
        event.setPublic(isPublic);
        event.setEventId(eventId);
        event.setLocation(location);
        event.setShareToken(shareToken);
        event.setLocationId(locationId);
        event.setLat(lat);
        event.setLng(lng);

        return event;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setIsPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public void setShareToken(String shareToken) {
        this.shareToken = shareToken;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
