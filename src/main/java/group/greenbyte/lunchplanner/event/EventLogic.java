package group.greenbyte.lunchplanner.event;

<<<<<<< HEAD
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

import java.util.Date;
import java.util.List;
=======
import group.greenbyte.lunchplanner.event.database.Comment;
import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.user.UserLogic;
import group.greenbyte.lunchplanner.user.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

@Service
public class EventLogic {

<<<<<<< HEAD
    private EventDao eventDao;
    private LocationDao locationDao;
=======

    private EventDao eventDao;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    private UserLogic userLogic;

    /**
     * Checks if a user has privileges to change the event object
     *
<<<<<<< HEAD
     * @param event to check
     * @param userName who wants to change
     * @return true if the user has permission, false if not
     */
    private boolean hasAdminPrivileges(Event event, String userName) {
        //TODO hasPrivileges

        return true;
=======
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
    private boolean hasUserPrivileges(int eventId, String userName) throws DatabaseException {
        return eventDao.userHasPrivileges(userName, eventId);
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
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
<<<<<<< HEAD
     * @param locationId id of the used location
     * @param timeStart time when the event starts
     * @param timeEnd time when the event ends
=======
     * @param location id of the used location
     * @param timeStart time when the event starts
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
     * @return the id of the new event
     * @throws HttpRequestException when location and timeStart not valid or eventName has no value
     * or an Database error happens
     */
    int createEvent(String userName, String eventName, String eventDescription,
<<<<<<< HEAD
                    int locationId, Date timeStart, Date timeEnd) throws HttpRequestException{
=======
                    String location, Date timeStart) throws HttpRequestException{
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

        if(userName == null || userName.length()==0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is empty");

        if(userName.length()> Event.MAX_USERNAME_LENGHT)
<<<<<<< HEAD
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Usernam is to long, maximum length:" + Event.MAX_USERNAME_LENGHT);
=======
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is too long, maximum length: " + Event.MAX_USERNAME_LENGHT);
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

        if(eventName.length()==0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Event name is empty");

        if(eventName.length()>Event.MAX_EVENTNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Event name is too long, maximum length: " + Event.MAX_EVENTNAME_LENGTH);

        if(eventDescription == null || eventDescription.length()>Event.MAX_DESCRITION_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Description is too long, maximum length" + Event.MAX_DESCRITION_LENGTH);

        if(timeStart.before(new Date()))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Start time must be in the future");

<<<<<<< HEAD
        if(timeEnd.before(timeStart) || timeEnd.equals(timeStart))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "End time must be after start time");

        //ToDo check if user exists

        try {
            return eventDao.insertEvent(userName, eventName, eventDescription, locationId, timeStart, timeEnd)
                    .getEventId();
        }catch(DatabaseException e) {
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
=======
        if(location == null || location.trim().equals(""))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Please set a location");

        if(location.length() > Event.MAX_LOCATION_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Location is too long, maximum length: " + Event.MAX_LOCATION_LENGTH);

        try {
            return eventDao.insertEvent(userName, eventName, eventDescription, location, timeStart)
                    .getEventId();
        }catch(DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
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
<<<<<<< HEAD
        //ToDo check if user exists

        if(name == null || name.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username can not be empty");

        try {
            Event event = eventDao.getEvent(eventId);
            //TODO write test for permission
            if(event == null || !hasAdminPrivileges(event, username))
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Event with eventId does not exist: " + eventId);
=======
        if(name == null || name.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "User name can not be empty");

        if(name.length() > Event.MAX_EVENTNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Too long user name");

        try {
            Event event = eventDao.getEvent(eventId);
            if(event == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with eventId: " + eventId + " does not exist");

            if(!hasAdminPrivileges(eventId, username))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You don't have write access to this event");
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

            Event updatedEvent = eventDao.updateEventName(eventId,name);

            eventChanged(updatedEvent);
        }catch(DatabaseException e){
<<<<<<< HEAD
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
=======
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
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
<<<<<<< HEAD
        //ToDo check if user exists

        try {
            Event event = eventDao.getEvent(eventId);
            //TODO write test for permission
            if(event == null || !hasAdminPrivileges(event, username))
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Event with eventId does not exist: " + eventId);
=======
        try {
            if(description.length() > Event.MAX_DESCRITION_LENGTH)
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Too long description");

            Event event = eventDao.getEvent(eventId);
            if(event == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with eventId does not exist: " + eventId);

            if(!hasAdminPrivileges(eventId, username))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You don't have write access to this event");
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

            Event updatedEvent = eventDao.updateEventDescription(eventId, description);

            eventChanged(updatedEvent);
        }catch(DatabaseException e){
<<<<<<< HEAD
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
=======
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
        }
    }

    /**
     *
     * @param username      userName that is logged in
     * @param eventId       id of the updated event
     * @exception HttpRequestException  when location and timeStart not valid or eventName has no value
     *                                  or an Database error happens
     */
<<<<<<< HEAD
    void updateEventLoction(String username, int eventId, int locationId)  throws HttpRequestException {
        //ToDo check if user exists

        try {
            Location location = locationDao.getLocation(locationId);
            if(location == null)
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Location with locationId does not exist: " + locationId);

            //ToDo write test for permission
            Event event = eventDao.getEvent(eventId);
            if(event == null || !hasAdminPrivileges(event, username))
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Event with eventId does not exist: " + eventId);

            Event updatedEvent = eventDao.updateEventLocation(eventId, locationId);

            eventChanged(updatedEvent);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
=======
    void updateEventLocation(String username, int eventId, String location)  throws HttpRequestException {
        try {
            Event event = eventDao.getEvent(eventId);
            if(event == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with eventId does not exist: " + eventId);

            if(!hasAdminPrivileges(eventId, username))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You don't have write access to this event");

            if(location == null || location.trim().equals(""))
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Please set a location");

            if(location.length() > Event.MAX_LOCATION_LENGTH)
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Location is too long, maximum length: " + Event.MAX_LOCATION_LENGTH);


            Event updatedEvent = eventDao.updateEventLocation(eventId, location);

            eventChanged(updatedEvent);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
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
<<<<<<< HEAD
        //ToDo check if user exists

=======
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
        if(timeStart.before(new Date()))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Time start is before today");

        try {
<<<<<<< HEAD
            //ToDo: Write test for permission
            Event event = eventDao.getEvent(eventId);
            if(event == null || !hasAdminPrivileges(event, username))
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Event with eventId does not exist: " + eventId);

            if(!timeStart.before(event.getEndDate()))
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Time Start is after time end");
=======
            Event event = eventDao.getEvent(eventId);
            if(event == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with eventId does not exist: " + eventId);

            if(!hasAdminPrivileges(eventId, username))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You dont have write access to this event");
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

            Event updatedEvent = eventDao.updateEventTimeStart(eventId, timeStart);

            eventChanged(updatedEvent);
        }catch(DatabaseException e){
<<<<<<< HEAD
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
        //ToDo: check if user exists

        try {
            //ToDo: write test for permission
            Event event = eventDao.getEvent(eventId);
            if(event == null || !hasAdminPrivileges(event, username))
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Event with eventId does not exist: " + eventId);

            if(timeEnd.before(event.getStartDate()))
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Time end is before time start");

            Event updatedEvent = eventDao.updateEventTimeEnd(eventId, timeEnd);

            eventChanged(updatedEvent);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
=======
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
        }
    }

    /**
     *
     * @param username  userName that is logged in
     * @return List<Event> List with generic typ of Event which includes all Events matching with the searchword
     *
     */
    public List<Event> getAllEvents(String username) throws HttpRequestException{
<<<<<<< HEAD
        //ToDo check if user exists

        if(username.length() > User.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is to long, maximun length" + Event.MAX_USERNAME_LENGHT);
        if(username.length() == 0 )
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is empty");

        try{
            return eventDao.search(username, "");
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
=======
        if(username.length() > User.MAX_USERNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is too long, maximum length: " + Event.MAX_USERNAME_LENGHT);
        if(username.length() == 0 )
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is empty");

        return this.searchEventsForUser(username, "");
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    }

    /**
     *
     * @param userName the user who wants to access the event
     * @param eventId  id of the event
     * @return Event which matched with the given id or null
     */
    public Event getEvent(String userName, int eventId)throws HttpRequestException{
<<<<<<< HEAD
        //ToDo check if user exists and has permission

        try{
            return eventDao.getEvent(eventId);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    //only for test purpose
    /**
     * only for test purpose!
     *
     * @param eventId  id of the event
     * @return Event which matched with the given id or null
     */
    public Event getEvent(int eventId)throws HttpRequestException{

        try{
            return eventDao.getEvent(eventId);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
=======
        try{
            Event event = eventDao.getEvent(eventId);

            if(event == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with event-id: " + eventId + "was not found");
            else {
                if(!hasUserPrivileges(eventId, userName)) //TODO write test for next line
                    throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You dont have rights to access this event");

                return event;
            }
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
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
<<<<<<< HEAD
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is not valid, maximun length" + Event.MAX_USERNAME_LENGHT + ", minimum length 1");
        if(!isValidName(userToInvite))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username of invited user is not valid, maximun length" + Event.MAX_USERNAME_LENGHT + ", minimum length 1");

        try{
            eventDao.putUserInviteToEvent(userToInvite, eventId);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), e.getMessage());
=======
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is not valid, maximum length: " + Event.MAX_USERNAME_LENGHT + ", minimum length 1");
        if(!isValidName(userToInvite))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username of invited user is not valid, maximum length" + Event.MAX_USERNAME_LENGHT + ", minimum length 1");

        try{
            if(!hasAdminPrivileges(eventId, username)) //TODO write test for next line
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You don't have write access to this event");

            eventDao.putUserInviteToEvent(userToInvite, eventId);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
        }

        userLogic.sendInvitation(username, userToInvite);
    }

<<<<<<< HEAD
=======
    /**
     * Invitation reply
     *
     * @param userName user that replies
     * @param eventId id of the event
     * @param answer answer of the user
     */
    public void reply(String userName, int eventId, InvitationAnswer answer) throws HttpRequestException {
        if(!isValidName(userName))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is not valid, maximum length: " + Event.MAX_USERNAME_LENGHT + ", minimum length 1");
        if(answer == null)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Invalid answer");

        try {
            if(eventDao.getEvent(eventId) == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with event-id: " + eventId + ", was not found");

            //TODO privilege check

            eventDao.replyInvitation(userName, eventId, answer);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    private boolean isValidName(String name){
        if(name.length() <= Event.MAX_USERNAME_LENGHT && name.length() > 0){
            System.out.println("isValid");
            return true;
        }

        else
            return false;
    }

<<<<<<< HEAD

    @Autowired
    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Autowired
    public void setLocationDao(LocationDao locationDao) {
        this.locationDao = locationDao;
=======
    public List<Event> searchEventsForUser(String userName, String searchword) throws HttpRequestException{

        if(searchword == null || searchword.length() > Event.MAX_SEARCHWORD_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Searchword is too long or null ");
        if(userName == null || userName.length()== 0 || userName.length() > Event.MAX_USERNAME_LENGHT)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is too long, empty or null ");

        try{

            Set<Event> searchResults = new HashSet<>(eventDao.findPublicEvents(searchword));

            List<Event> temp = eventDao.findEventsUserInvited(userName, searchword);
            for(Event event : temp) {
                if(!searchResults.contains(event))
                    searchResults.add(event);
            }

            //Get teams and get all events for this teams

            return new ArrayList<>(searchResults);

        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    /**
     *
     *
     * @param userName
     * @param comment
     * @param eventId
     * @throws HttpRequestException
     */
    public void newComment(String userName, String comment, int eventId) throws HttpRequestException {

        if(!isValidName(userName))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is not valid, maximun length" + Event.MAX_USERNAME_LENGHT + ", minimum length 1");

        if(comment == null || comment.length() == 0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Comment can not be empty");

        if(comment.length() > Event.MAX_COMMENT_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Comment too long");

        try{
            Event event = eventDao.getEvent(eventId);

            if(event == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with event-id: " + eventId + "was not found");



            if(!hasUserPrivileges(eventId, userName))
                if(!hasAdminPrivileges(eventId, userName))
                    throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You dont have rights to access this event");


            eventDao.putCommentForEvent(userName,eventId, comment);


        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

    }

    /**
     *
     * @param userName the user who wants to access the event
     * @param eventId  id of the event
     * @return Event which matched with the given id or null
     */
    public List<Comment> getAllComments(String userName, int eventId)throws HttpRequestException {
        if(!isValidName(userName))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is not valid, maximun length" + Event.MAX_USERNAME_LENGHT + ", minimum length 1");

        try{
           Event event = eventDao.getEvent(eventId);
           if(event == null)
               throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with event-id: " + eventId + " was not found");


           if(!hasUserPrivileges(eventId, userName))
               throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You dont have rights to access this event");


            List<Comment> comments = eventDao.getAllComments(eventId);

            return comments;

        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Autowired
    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    }

    @Autowired
    public void setUserLogic(UserLogic userLogic) {
        this.userLogic = userLogic;
    }
}
