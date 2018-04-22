package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.location.LocationDao;
import group.greenbyte.lunchplanner.location.LocationLogic;
import group.greenbyte.lunchplanner.location.database.Location;
import group.greenbyte.lunchplanner.user.UserLogic;
import group.greenbyte.lunchplanner.user.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EventLogic {


    private EventDao eventDao;
    private LocationDao locationDao;
    private UserLogic userLogic;

    /**
     * Checks if a user has privileges to change the event object
     *
     * @param eventId id of the event to check
     * @param userName who wants to change
     * @return true if the user has permission, false if not
     */
    private boolean hasAdminPrivileges(int eventId, String userName) throws DatabaseException {
        return eventDao.userHasAdminPrivileges(userName, eventId);
    }

    /**
     * Checks if a user has privileges to view the event object
     *
     * @param eventId id of the event to check
     * @param userName who wants to change
     * @return true if the user has permission, false if not
     */
    private boolean hasPrivileges(int eventId, String userName) throws DatabaseException {
        return eventDao.userHasPrivileges(userName, eventId);
    }

    /**
     * Will update all subscribtions for an event when it changes
     * @param event event that has changed
     */
    private void eventChanged(Event event) {
        //TODO eventChanged
    }

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

        if(userName == null || userName.length()==0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is empty");

        if(userName.length()> Event.MAX_USERNAME_LENGHT)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Usernam is to long, maximum length:" + Event.MAX_USERNAME_LENGHT);

        if(eventName.length()==0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Event name is empty");

        if(eventName.length()>Event.MAX_EVENTNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Event name is too long, maximum length: " + Event.MAX_EVENTNAME_LENGTH);

        if(eventDescription == null || eventDescription.length()>Event.MAX_DESCRITION_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Description is too long, maximum length" + Event.MAX_DESCRITION_LENGTH);

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
     * @exception HttpRequestException  when location and timeStart not valid or eventName has no value
     *                                  or an Database error happens
     */
    void updateEventName(String username, int eventId, String name)  throws HttpRequestException {
        if(name == null || name.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username can not be empty");

        try {
            Event event = eventDao.getEvent(eventId);
            if(event == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with eventId does not exist: " + eventId);

            if(!hasAdminPrivileges(eventId, username))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You dont have write access to this event");

            Event updatedEvent = eventDao.updateEventName(eventId,name);

            eventChanged(updatedEvent);
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
            Event event = eventDao.getEvent(eventId);
            if(event == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with eventId does not exist: " + eventId);

            if(!hasAdminPrivileges(eventId, username))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You dont have write access to this event");

            Event updatedEvent = eventDao.updateEventDescription(eventId, description);

            eventChanged(updatedEvent);
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
    void updateEventLocation(String username, int eventId, int locationId)  throws HttpRequestException {
        try {
            Location location = locationDao.getLocation(locationId);
            if(location == null)
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Location with locationId does not exist: " + locationId);

            Event event = eventDao.getEvent(eventId);
            if(event == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with eventId does not exist: " + eventId);

            if(!hasAdminPrivileges(eventId, username))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You dont have write access to this event");

            Event updatedEvent = eventDao.updateEventLocation(eventId, locationId);

            eventChanged(updatedEvent);
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
        if(timeStart.before(new Date()))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Time start is before today");

        try {
            Event event = eventDao.getEvent(eventId);
            if(event == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with eventId does not exist: " + eventId);

            if(!hasAdminPrivileges(eventId, username))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You dont have write access to this event");

            if(!timeStart.before(event.getEndDate()))
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Time Start is after time end");

            Event updatedEvent = eventDao.updateEventTimeStart(eventId, timeStart);

            eventChanged(updatedEvent);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
    /**
     *
     * @param username      userName that is logged in
     * @param eventId       id of the updated event
     * @param timeEnd        time on which the event ends
     * @exception HttpRequestException  when location and timeStart not valid or eventName has no value
     *                                  or an Database error happens
     */
    void updateEventTimeEnd(String username, int eventId, Date timeEnd)  throws HttpRequestException {
        try {
            Event event = eventDao.getEvent(eventId);
            if(event == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with eventId does not exist: " + eventId);

            if(!hasAdminPrivileges(eventId, username))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You dont have write access to this event");

            if(timeEnd.before(event.getStartDate()))
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Time end is before time start");

            Event updatedEvent = eventDao.updateEventTimeEnd(eventId, timeEnd);

            eventChanged(updatedEvent);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
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

        return this.searchEventsForUser(username, "");
    }

    /**
     *
     * @param userName the user who wants to access the event
     * @param eventId  id of the event
     * @return Event which matched with the given id or null
     */
    public Event getEvent(String userName, int eventId)throws HttpRequestException{
        try{


            Event event = eventDao.getEvent(eventId);

            if(event == null)
                return null;
            else {
                if(!hasPrivileges(eventId, userName))
                    throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You dont have rights to access this event");

                return event;
            }
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    /**
     * Invite user to an event
     *
     * @param username id of the user who creates the events
     * @param userToInvite id of the user who is invited
     * @param eventId id of event
     * @return the Event of the invitation
     *
     * @throws HttpRequestException when an unexpected error happens
     *
     */
    public void inviteFriend(String username, String userToInvite, int eventId) throws HttpRequestException{

        if(!isValidName(username))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is not valid, maximun length" + Event.MAX_USERNAME_LENGHT + ", minimum length 1");
        if(!isValidName(userToInvite))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username of invited user is not valid, maximun length" + Event.MAX_USERNAME_LENGHT + ", minimum length 1");

        try{
            if(!hasAdminPrivileges(eventId, username))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You dont have write access to this event");

            eventDao.putUserInviteToEvent(userToInvite, eventId);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

        userLogic.sendInvitation(username, userToInvite);
    }

    /**
     * Invitation reply
     *
     * @param userName user that replies
     * @param eventId id of the event
     * @param answer answer of the user
     */
    public void reply(String userName, int eventId, InvitationAnswer answer) throws HttpRequestException {
        if(!isValidName(userName))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is not valid, maximun length" + Event.MAX_USERNAME_LENGHT + ", minimum length 1");
        if(answer == null)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Invalid answer");

        try {
            if(eventDao.getEvent(eventId) == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with event-id: " + eventId + ", was not found");

            //TODO privilege check

            eventDao.replyInvitation(userName, eventId, answer);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    private boolean isValidName(String name){
        if(name.length() <= Event.MAX_USERNAME_LENGHT && name.length() > 0){
            System.out.println("isValid");
            return true;
        }

        else
            return false;
    }

    public List<Event> searchEventsForUser(String userName, String searchword) throws HttpRequestException{

        if(searchword == null || searchword.length() > Event.MAX_SEARCHWORD_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Searchword is to long, empty or null ");
        if(userName == null || userName.length()== 0 || userName.length() > Event.MAX_USERNAME_LENGHT)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is to long, empty or null ");

        try{
            Set<Event> searchResults = new HashSet<>();
            List<Event> temp = eventDao.findPublicEvents(searchword);

            searchResults.addAll(eventDao.findPublicEvents(searchword));
            searchResults.addAll(eventDao.findEventsUserInvited(userName, searchword));

            //Get teams and get all events for this teams

            return new ArrayList<>(searchResults);

        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @Autowired
    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Autowired
    public void setLocationDao(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    @Autowired
    public void setUserLogic(UserLogic userLogic) {
        this.userLogic = userLogic;
    }
}
