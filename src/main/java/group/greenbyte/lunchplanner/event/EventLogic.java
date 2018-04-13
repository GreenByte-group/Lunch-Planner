package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Date;

@Service
public class EventLogic {

    private EventDao eventDao;

    /**
     * Create an event. At least the eventName and a location or timeStart is needed
     *
     * @param userName userName that is logged in
     * @param eventName name of the new event, not null
     * @param eventDescription description of the new event
     * @param locationId id of the used location
     * @param timeStart time when the event starts
     * @param timeEnd time when the event ends
     * @return the id of the new event
     * @throws HttpRequestException when location and timeStart not valid or eventName has no value
     * or an Database error happens
     */
    int createEvent(String userName, String eventName, String eventDescription,
                    int locationId, Date timeStart, Date timeEnd) throws HttpRequestException{

        if(userName.length()==0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is empty");
        if(userName.length()> Event.MAX_USERNAME_LENGHT)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Usernam is to long, maximum length:" + Event.MAX_USERNAME_LENGHT);
        if(eventDescription.length()>Event.MAX_DESCRITION_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Description is to long, maximun length" + Event.MAX_DESCRITION_LENGTH);

        try {
            return eventDao.insertEvent(userName, eventName, eventDescription, locationId, timeStart, timeEnd)
                    .getEventTd();
        }catch(DatabaseException e) {
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }


    }


    /**
     *
     * @param username      userName that is logged in
     * @param eventId       id of the updated event
     * @param name          name of the updated event
     * @param description   description of the updated event
     * @param timeStart     time on which the event starts
     * @param timEnd        time on which the event ends
     * @exception HttpRequestException  when location and timeStart not valid or eventName has no value
     *                                  or an Database error happens
     */
    void updatEvent(String username, int eventId, String name, String description,
                int locationId, Date timeStart, Date timEnd)  throws HttpRequestException {

    }





    /**
     * Will update all subscribtions for an event when it changes
     * @param event event that has changed
     */
    private void eventChanged(Event event) {

    }

    @Autowired
    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

}
