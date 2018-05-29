package group.greenbyte.lunchplanner.event;

import com.google.api.Http;
import group.greenbyte.lunchplanner.event.database.BringService;
import group.greenbyte.lunchplanner.event.database.Comment;
import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.team.TeamDao;
import group.greenbyte.lunchplanner.team.TeamLogic;
import group.greenbyte.lunchplanner.team.database.TeamMemberDataForReturn;
import group.greenbyte.lunchplanner.security.SessionManager;
import group.greenbyte.lunchplanner.user.UserLogic;
import group.greenbyte.lunchplanner.user.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.*;

@Service
public class EventLogic {


    private EventDao eventDao;
    private UserLogic userLogic;
    private TeamDao teamDao;
    private TeamLogic teamLogic;

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
    private boolean hasUserPrivileges(int eventId, String userName) throws DatabaseException {
        return eventDao.userHasPrivileges(userName, eventId);
    }

    /**
     * Will update all subscribtions for an event when it changes
     * @param event event that has changed
     */
    private void eventChanged(Event event) {
        //TODO eventChanged
    }


    int createEvent(String userName, String eventName, String eventDescription,
                    String location, Date timeStart) throws HttpRequestException {
        return createEvent(userName, eventName, eventDescription, location, timeStart, false);
    }

    /**
     * Create an event. At least the eventName and a location or timeStart is needed
     *
     * @param userName userName that is logged in
     * @param eventName name of the new event, not null
     * @param eventDescription description of the new event
     * @param location id of the used location
     * @param timeStart time when the event starts
     * @return the id of the new event
     * @throws HttpRequestException when location and timeStart not valid or eventName has no value
     * or an Database error happens
     */
    int createEvent(String userName, String eventName, String eventDescription,
                    String location, Date timeStart, boolean visible) throws HttpRequestException{

        if(userName == null || userName.length()==0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is empty");

        if(userName.length()> Event.MAX_USERNAME_LENGHT)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is too long, maximum length: " + Event.MAX_USERNAME_LENGHT);

        if(eventName.length()==0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Event name is empty");

        if(eventName.length()>Event.MAX_EVENTNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Event name is too long, maximum length: " + Event.MAX_EVENTNAME_LENGTH);

        if(eventDescription == null || eventDescription.length()>Event.MAX_DESCRITION_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Description is too long, maximum length" + Event.MAX_DESCRITION_LENGTH);

        if(timeStart.before(new Date()))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Start time must be in the future");

        if(location == null || location.trim().equals(""))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Please set a location");

        if(location.length() > Event.MAX_LOCATION_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Location is too long, maximum length: " + Event.MAX_LOCATION_LENGTH);

        try {
            return eventDao.insertEvent(userName, eventName, eventDescription, location, timeStart, visible)
                    .getEventId();
        }catch(DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
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
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "User name can not be empty");

        if(name.length() > Event.MAX_EVENTNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Too long user name");

        try {
            Event event = eventDao.getEvent(eventId);
            if(event == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with eventId: " + eventId + " does not exist");

            if(!hasAdminPrivileges(eventId, username))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You don't have write access to this event");

            Event updatedEvent = eventDao.updateEventName(eventId,name);

            eventChanged(updatedEvent);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
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
            if(description.length() > Event.MAX_DESCRITION_LENGTH)
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Too long description");

            Event event = eventDao.getEvent(eventId);
            if(event == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with eventId does not exist: " + eventId);

            if(!hasAdminPrivileges(eventId, username))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You don't have write access to this event");

            Event updatedEvent = eventDao.updateEventDescription(eventId, description);

            eventChanged(updatedEvent);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    /**
     *
     * @param username      userName that is logged in
     * @param eventId       id of the updated event
     * @exception HttpRequestException  when location and timeStart not valid or eventName has no value
     *                                  or an Database error happens
     */
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

            Event updatedEvent = eventDao.updateEventTimeStart(eventId, timeStart);

            eventChanged(updatedEvent);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
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
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is too long, maximum length: " + Event.MAX_USERNAME_LENGHT);
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
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with event-id: " + eventId + "was not found");
            else {
                if(!hasUserPrivileges(eventId, userName)) //TODO write test for next line
                    throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You dont have rights to access this event");

                return event;
            }
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
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
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is not valid, maximum length: " + Event.MAX_USERNAME_LENGHT + ", minimum length 1");
        if(!isValidName(userToInvite))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username of invited user is not valid, maximum length" + Event.MAX_USERNAME_LENGHT + ", minimum length 1");

        try{
            if(!hasAdminPrivileges(eventId, username)) //TODO write test for next line
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You don't have write access to this event");

            eventDao.putUserInviteToEvent(userToInvite, eventId);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

        userLogic.sendInvitation(username, userToInvite);
    }

    /**
     * Invite a team to an event
     *
     * @param userName id of the user who creates the event
     * @param eventId id of event
     * @param teamId id of team
     * @throws HttpRequestException
     */
    public void inviteTeam(String userName, int eventId, int teamId) throws HttpRequestException{

        if(!isValidName(userName))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is not valid, maximum length: " + Event.MAX_USERNAME_LENGHT + ", minimum length 1");

        try{
            if(!hasAdminPrivileges(eventId, userName)) //TODO write test for next line
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You don't have write access to this event");

            List<TeamMemberDataForReturn> members = teamDao.getInvitations(teamId);

            for(TeamMemberDataForReturn member : members) {
                //userName == member.getUserName() -> primary key exception
                //users can not invite themselves to an event
                if(!userName.equals(member.getUserName())){
                    eventDao.putUserInviteToEvent(member.getUserName(), eventId);
                    userLogic.sendInvitation(userName, member.getUserName());
                }
            }
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
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

    public Event getEventByToken(String token) throws HttpRequestException {
        try {
            Event event = eventDao.getEventByShareToken(token);

            if(event == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event for token: " + token + " not found");
            
            return event;
        } catch(DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    private String generateShareToken() {
        return UUID.randomUUID().toString();
    }

    public String getShareToken(int eventId) throws HttpRequestException {
        try {
            Event event = eventDao.getEvent(eventId);
            if(event == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with id " + eventId + " not found");

            String shareToken = event.getShareToken();
            if(shareToken != null)
                return shareToken;

            shareToken = generateShareToken();

            eventDao.addShareToken(eventId, shareToken);

            return shareToken;
        } catch(DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }


    public void putService(int event_id, String food, String description) throws HttpRequestException{
        try{
            eventDao.putService(SessionManager.getUserName(),event_id,food,description);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public List<BringService> getService(int eventId) throws HttpRequestException{
        try {
//            if(eventId == null)
//                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "eventId is null "+eventId);

            List<BringService> serviceList = eventDao.getService(eventId);
            return serviceList;
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public void updateBringservice(int eventId, String accepter, int serviceId) throws HttpRequestException{
        try{
            //TODO check ob schon jemand anderes eingetragen ist
            eventDao.updateBringservice(eventId,accepter,serviceId);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @Autowired
    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Autowired
    public void setUserLogic(UserLogic userLogic) {
        this.userLogic = userLogic;
    }

    @Autowired
    public void setTeamDao(TeamDao teamDao) {
        this.teamDao = teamDao;
    }
    
    @Autowired
    public void setTeamLogic(TeamLogic teamLogic) {
        this.teamLogic = teamLogic;
    }
}
