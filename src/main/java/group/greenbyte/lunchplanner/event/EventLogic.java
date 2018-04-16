package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.user.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

        if(eventName.length()==0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Event name is empty");

        if(eventName.length()>Event.MAX_EVENTNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Event name is too long, maximum length: " + Event.MAX_EVENTNAME_LENGTH);

        if(eventDescription.length()>Event.MAX_DESCRITION_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Description is to long, maximum length" + Event.MAX_DESCRITION_LENGTH);
/*
        if(timeStart.before(new Date()))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Start time must be in the future");

        if(timeEnd.before(timeStart) || timeEnd.equals(timeStart))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "End time must be after start time");
*/

        try {
            return eventDao.insertEvent(userName, eventName, eventDescription, locationId, timeStart, timeEnd)
                    .getEventId();
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
     * @param timeEnd        time on which the event ends
     * @exception HttpRequestException  when location and timeStart not valid or eventName has no value
     *                                  or an Database error happens
     */
//    void updateEvent(String username, int eventId, String name, String description,
//                int locationId, Date timeStart, Date timeEnd)  throws HttpRequestException {
//        try {
//            Event updatedEvent = eventDao.updateEvent(username, eventId, name,description,locationId,timeStart,timeEnd);
//        }catch(DatabaseException e){
//            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
//        }
//    }

    /**
     *
     * @param username      userName that is logged in
     * @param eventId       id of the updated event
     * @param name          name of the updated event
     * @exception HttpRequestException  when location and timeStart not valid or eventName has no value
     *                                  or an Database error happens
     */
    void updateEventName(String username, int eventId, String name)  throws HttpRequestException {
        try {
            Event updatedEvent = eventDao.updateEventName(eventId,name);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    /**
     *
     * @param username      userName that is logged in
     * @param eventId       id of the updated event
     * @exception HttpRequestException  when location and timeStart not valid or eventName has no value
     *                                  or an Database error happens
     */
    void updateEventDescription(String username, int eventId, String description)  throws HttpRequestException {
        try {
            Event updatedEvent = eventDao.updateEventDescription(eventId, description);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    /**
     *
     * @param username      userName that is logged in
     * @param eventId       id of the updated event
     * @exception HttpRequestException  when location and timeStart not valid or eventName has no value
     *                                  or an Database error happens
     */
    void updateEventLoction(String username, int eventId, int locationId)  throws HttpRequestException {
        try {
            Event updatedEvent = eventDao.updateEventLocation(eventId,locationId);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    /**
     *
     * @param username      userName that is logged in
     * @param eventId       id of the updated event
     * @param timeStart     time on which the event starts
     * @exception HttpRequestException  when location and timeStart not valid or eventName has no value
     *                                  or an Database error happens
     */
    void updateEventTimeStart(String username, int eventId, Date timeStart) throws HttpRequestException {
        try {
            Event updatedEvent = eventDao.updateEventTimeStart(eventId, timeStart);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    /**
     *
     * @param username      userName that is logged in
     * @param eventId       id of the updated event
     * @param timEnd        time on which the event ends
     * @exception HttpRequestException  when location and timeStart not valid or eventName has no value
     *                                  or an Database error happens
     */
    void updateEventTimeEnd(String username, int eventId, Date timEnd)  throws HttpRequestException {
        try {
            Event updatedEvent = eventDao.updateEventTimeEnd(eventId, timEnd);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    /**
     * Will update all subscribtions for an event when it changes
     * @param event event that has changed
     */
    private void eventChanged(Event event) {

    }

    /**
     *
     * @param username  userName that is logged in
     * @return List<Event> List with generic typ of Event which includes all Events matching with the searchword
     *
     */
    public List<Event> getAllEvents(String username) throws HttpRequestException{
        if(username.length() > User.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is to long, maximun length" + Event.MAX_USERNAME_LENGHT);
        if(username.length() == 0 )
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is empty");

        try{
            return eventDao.search(username, "");
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    /**
     *
     * @param eventId  id of the event
     * @return Event which matched with the given id
     */
    public Event getEvent(int eventId)throws HttpRequestException{

        try{
            if(eventDao.getEvent(eventId).equals(null))
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "event is null");
            return eventDao.getEvent(eventId);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }


    }




    @Autowired
    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

}
