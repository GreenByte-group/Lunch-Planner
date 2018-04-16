package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import sun.net.httpserver.HttpsServerImpl;

import javax.xml.crypto.Data;
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

        if(timeStart.before(new Date()))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Start time must be in the future");

        if(timeEnd.before(timeStart) || timeEnd.equals(timeStart))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "End time must be after start time");


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

    /**
     *
     * @return List<Event> List with generic typ of Event which includes all Events matching with the searchword
     */
    public List<Event> getAllEvents() throws HttpRequestException{

        try{
           return eventDao.getAll("dummy", "");
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        }

    }

    /**
     *
     * @param username  userName that is logged in
     * @param searchword word to compare an find matches with a name of any events
     * @return List<Event> List with generic typ of Event which includes all Events matching with the searchword
     *
     */
    public List<Event> getAllEvents(String username, String searchword) throws HttpRequestException{
        if(username.length() > Event.MAX_USERNAME_LENGHT)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is to long, maximun length" + Event.MAX_USERNAME_LENGHT);
        if(username.length() == 0 )
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is empty");
        if(searchword == null)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Searchword is null");
        if(searchword.length() > Event.MAX_SEARCHWORD_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Searchword is to long, maximun length" + Event.MAX_SEARCHWORD_LENGTH);

        try{
            return eventDao.getAll(username, searchword);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        }


    }

    /**
     * Invite user to an event
     *
     * @param username id of the user who creates the events
     * @param userToInvited id of the user who is invited
     * @param eventId id of event
     * @return the Event of the invitation
     *
     * @throws HttpRequestException when an unexpected error happens
     *
     */
    public Event inviteFriend(String username, String userToInvited, int eventId) throws HttpRequestException{
        if(!isValidName(username))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is not valid, maximun length" + Event.MAX_USERNAME_LENGHT + ", minimum length 1");
        if(!isValidName(userToInvited))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username of invited user is not valid, maximun length" + Event.MAX_USERNAME_LENGHT + ", minimum length 1");

        try{
            return eventDao.putUserInviteToEvent(username, eventId);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        }
    }

    private boolean isValidName(String name){
        if(name.length() <= Event.MAX_USERNAME_LENGHT && name.length() > 0)
            return true;
        else
            return false;
    }


    @Autowired
    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

}
