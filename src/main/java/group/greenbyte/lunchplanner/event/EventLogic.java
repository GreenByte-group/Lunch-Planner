package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.Date;
import group.greenbyte.lunchplanner.excpetions.HttpRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventLogic {

    private EventDao eventDao;

    /**
     * Create an event. At least the eventName and a location or timeStart is needed
     *
     * @param eventName name of the new event, not null
     * @param eventDescription description of the new event
     * @param locationId id of the used location
     * @param timeStart time when the event starts
     * @param timeEnd time when the event ends
     * @return the id of the new event
     * @throws HttpRequestException when location and timeStart not valid or eventName has no value
     */
    int createEvent(String eventName, String eventDescription,
                    int locationId, Date timeStart, Date timeEnd) throws HttpRequestException {
        return 0;
        //TODO: call eventChanged
    }

    private void eventChanged(Event event) {

    }

    @Autowired
    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

}
