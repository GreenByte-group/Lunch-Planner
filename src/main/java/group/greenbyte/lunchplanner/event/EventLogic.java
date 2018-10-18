package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.Config;
import group.greenbyte.lunchplanner.event.database.BringService;
import group.greenbyte.lunchplanner.event.database.Comment;
import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.event.database.EventInvitationDataForReturn;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.security.SessionManager;
import group.greenbyte.lunchplanner.team.TeamDao;
import group.greenbyte.lunchplanner.team.TeamLogic;
import group.greenbyte.lunchplanner.team.database.TeamMemberDataForReturn;
import group.greenbyte.lunchplanner.user.UserDao;
import group.greenbyte.lunchplanner.user.UserLogic;
import group.greenbyte.lunchplanner.user.database.User;
import group.greenbyte.lunchplanner.user.database.notifications.NotificationOptions;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.*;
import java.util.Calendar;

import static org.hibernate.criterion.Projections.property;

@Service
public class EventLogic {

    private final Scheduler scheduler;

    private EventDao eventDao;
    private UserLogic userLogic;
    private TeamDao teamDao;
    private TeamLogic teamLogic;
    private UserDao userDao;

//    @Autowired
//    private JavaMailSender sender;

    @Autowired
    public EmailService emailservice;

    @Autowired
    public EventLogic(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    private void checkEventsHasToBeDeleted() {
        try {
            Date dateDelete = new Date(new Date().getTime() - Config.DELETE_EVENT_AFTER_SECONDS * 1000);
            List<Event> events = eventDao.getAllEvents();
            for(Event event : events) {

            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String username){
        System.out.println("Eventlogic "+ username);
        try{
            eventDao.deleteUserInvitation(username);
            eventDao.deleteUserComments(username);
            eventDao.deleteUserBringservice(username);
        }catch(DatabaseException e){
            e.printStackTrace();
        }
    }

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

    int createEvent(String userName, String eventName, String eventDescription,
                    String location, Date timeStart, boolean visible, String locationId) throws HttpRequestException{

        System.out.println("Alle wichtigen Daten:" +userName+" ,"+eventName+userName+" ,"+eventDescription+userName+" ,"+location+userName+" ,"+timeStart+userName+" ,"+visible+userName+" ,"+locationId);
        if(userName == null || userName.length()==0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is empty");

        if(userName.length()> Event.MAX_USERNAME_LENGHT)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is too long, maximum length: " + Event.MAX_USERNAME_LENGHT);

        if(eventName.length()==0)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Event name is empty");

        if(eventName.length()>Event.MAX_EVENTNAME_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Event name is too long, maximum length: " + Event.MAX_EVENTNAME_LENGTH);

        if(eventDescription == null || eventDescription.length()>Event.MAX_DESCRIPTION_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Description is too long, maximum length" + Event.MAX_DESCRIPTION_LENGTH);

        if(timeStart.before(new Date()))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Start time must be in the future");

        if(location == null || location.trim().equals(""))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Please set a location");

        if(location.length() > Event.MAX_LOCATION_LENGTH)
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Location is too long, maximum length: " + Event.MAX_LOCATION_LENGTH);

        try {
            //check if there is an event with the same name and start date
            List<Event> events = eventDao.getAllEvents();

            for (Event event : events) {
                if (event.getEventName().equals(eventName) && timeStart.compareTo(event.getStartDate()) == 0) {
                    List<EventInvitationDataForReturn> eventInvitations = eventDao.getInvitations(event.getEventId());
                    for (EventInvitationDataForReturn eventInvitation : eventInvitations) {
                        //TODO test if same location too?
                        if (eventInvitation.getUserName().equals(userName) && eventInvitation.isAdmin()) {
                            throw new HttpRequestException(HttpStatus.NOT_ACCEPTABLE.value(), "You already created an event with the same name and start date");
                        }
                    }
                }
            }

            Integer eventId = eventDao.insertEvent(userName, eventName, eventDescription, location, timeStart, visible, locationId)
                    .getEventId();

            scheduleDeleteEvent(eventId);
            System.out.println("All variables from new event: "+userName+", "+eventName+", "+eventDescription+", "+location+", "+visible+", "+locationId);

            //only send notifications if the created event is visible for everyone
            if(visible) {

                //set notification information
                User eventCreator = userLogic.getUser(userName);
                String picturePath = eventCreator.getProfilePictureUrl();
                System.out.println("locationId: "+locationId);
                List<User> users = userLogic.getSubscriber(location);
                System.out.println("USERS: "+users);
                String title = "Event created in " + location;
                String description = String.format("%s created an event in your subscribed location", userName);
                String linkToClick = "/event/" + eventId;

                /*
                the user that created an event in a specific location could be a subscriber too and
                shouldn't get a notification
                */
                users.remove(eventCreator);
                System.out.println("user anzahl: "+users.size());
                System.out.println("user list: "+users);

                for (User subscriber : users) {
                    //save notification
                    userLogic.saveNotification(subscriber.getUserName(), title, description, userName, linkToClick, picturePath);
                    String nameOfUser = subscriber.getUserName();
                    //send a notification & email to subscriber
                    String body = "Hey "+nameOfUser+",\ncheck mal den Lunchplanner. Du wolltest benachrichtigt werden wenn jemand zur Location:  "+eventName+
                            " geht.\nVielleicht hast du ja lust "+userName+" zu begleiten?\n\nViel spaß & Hasta la pasta";
                    System.out.println("NAME: "+userLogic.getUser(subscriber.getUserName()).geteMail());

                   try{
                       emailservice.send(subscriber.geteMail(),"Email", body);
                   }catch(Exception e){
                       e.printStackTrace();
                   }



                    NotificationOptions notificationOptions = userLogic.getNotificationOptions(subscriber.getUserName());
                    if (notificationOptions == null || (notificationOptions.notificationsAllowed() && !notificationOptions.isSubscriptionsBlocked())) {
                        try {
//                            userLogic.sendNotification(subscriber.getFcmToken(), subscriber.getUserName(), title, description, linkToClick, picturePath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            return eventId;
        }catch(DatabaseException e) {
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
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

        return createEvent(userName, eventName, eventDescription, location, timeStart, visible, null);
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
            if(description.length() > Event.MAX_DESCRIPTION_LENGTH)
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

            scheduleDeleteEvent(updatedEvent);

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
    public void inviteFriend(String username, String userToInvite, int eventId) throws HttpRequestException,Exception {

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


        //send invited peoples a email
        String body = "Hey "+userToInvite+",\ncheck mal den Lunchplanner. Du hast eine Einladung von "+SessionManager.getUserName()+
                " erhalten.\nVielleicht hast du ja lust auf "+this.eventDao.getEvent(eventId).getEventName()+" ?\n\nViel spaß & Hasta la pasta";

        System.out.println("NAME2: "+this.userDao.getUser(userToInvite).geteMail());
       emailservice.send(this.userDao.getUser(userToInvite).geteMail(),"Email", body);


        User user = userLogic.getUser(userToInvite);
        //set notification information
//        String title = "Event invitation";
//        String description = String.format("%s invited you to an event", username);
//        String linkToClick = "/event/" + eventId;
//
//        //save notification
//        userLogic.saveNotification(userToInvite,title,description,username,linkToClick, "");
//
//        //send a notification to userToInvite
//        NotificationOptions notificationOptions = userLogic.getNotificationOptions(userToInvite);
//        if(notificationOptions == null || (notificationOptions.notificationsAllowed() && !notificationOptions.isEventsBlocked())) {
//            try {
//                userLogic.sendNotification(user.getFcmToken(), userToInvite, title, description,linkToClick, "");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * Invite a team to an event
     *
     * @param userName id of the user who creates the event
     * @param eventId id of event
     * @param teamId id of team
     * @throws HttpRequestException
     */
    public void inviteTeam(String userName, int eventId, int teamId) throws HttpRequestException {

        if(!isValidName(userName))
            throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Username is not valid, maximum length: " + Event.MAX_USERNAME_LENGHT + ", minimum length 1");

        try{
            if(!hasAdminPrivileges(eventId, userName)) //TODO write test for next line
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You don't have write access to this event");

            // get members to invite
            List<TeamMemberDataForReturn> members = teamDao.getInvitations(teamId);

            // set notification information
            String title = "Event invitation";
            String description = String.format("%s invited you to an event", userName);
            String linkToClick = "/event/" + eventId;

            User user;

            for(TeamMemberDataForReturn member : members) {
                if(!userName.equals(member.getUserName())){
                    eventDao.putUserInviteToEvent(member.getUserName(), eventId);

                    //TODO handle exception
                    //TODO check if user wants notifications
                    //TODO picture path
                    user = userLogic.getUser(member.getUserName());
                   // userLogic.sendNotification(user.getFcmToken(),member.getUserName(),title, description,linkToClick, "");
                    this.inviteFriend(userName,member.getUserName(),eventId);
                }
            }
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
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


            // if the user doesn't have rights to access an event, they can only comment if the event is public.
            if(!hasUserPrivileges(eventId, userName)) {
                Event e = getEvent(userName, eventId);
                if(!e.isPublic()) {
                    throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You dont have rights to access this event");
                }
            }

            eventDao.putCommentForEvent(userName,eventId, comment);

            List<EventInvitationDataForReturn> invitations = eventDao.getInvitations(eventId);
            for(EventInvitationDataForReturn invitation : invitations) {
                if(invitation.getAnswer() == InvitationAnswer.ACCEPT.getValue() && !invitation.getUserName().equals(userName)) {
                    User user = userLogic.getUser(invitation.getUserName());

                    String title = "New comment in " + event.getEventName();
                    String description = userName + ": " + comment;
                    String linkToClick = "/event/" + eventId + "/comments";
                    String picturePath = "";

                    //save notification
                    userLogic.saveNotification(userName, title, description,
                            user.getUserName(), linkToClick, picturePath);

                    //send a notification to userToInvite
                    NotificationOptions notificationOptions = userLogic.getNotificationOptions(user.getUserName());
                    if (notificationOptions == null || (notificationOptions.notificationsAllowed() && !notificationOptions.isEventsBlocked())) {
                        try {
                            userLogic.sendNotification(user.getFcmToken(), user.getUserName(), title, description, linkToClick, picturePath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Notification options disallow");
                    }
                }
            }
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


    public void putService(String userName, int eventId, String food, String description) throws HttpRequestException{
        try{
            if(food.length() > BringService.MAX_NAME_LENGTH || food.length() == 0)
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Name is too long or empty");
            if(description.length() > BringService.MAX_DESCRIPTION_LENGTH)
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), "Description is too long");

            if(!hasUserPrivileges(eventId, userName))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "You don't have acces to this event");

            eventDao.putService(SessionManager.getUserName(),eventId,food,description);

            List<EventInvitationDataForReturn> invitations = eventDao.getInvitations(eventId);
            Event event = eventDao.getEvent(eventId);
            for(EventInvitationDataForReturn invitation : invitations) {
                if(invitation.getAnswer() == InvitationAnswer.ACCEPT.getValue() && !invitation.getUserName().equals(userName)) {
                    User user = userLogic.getUser(invitation.getUserName());

                    String title = "New task in " + event.getEventName();
                    String descriptionNotification = userName + ": " + food;
                    String linkToClick = "/event/" + eventId;
                    String picturePath = "";

                    //save notification
                    userLogic.saveNotification(userName, title, descriptionNotification,
                            user.getUserName(), linkToClick, picturePath);

                    //send a notification to userToInvite
                    NotificationOptions notificationOptions = userLogic.getNotificationOptions(user.getUserName());
                    System.out.println("dachte es amk: "+notificationOptions.notificationsAllowed()+", "+notificationOptions.isEventsBlocked());
//                    if (notificationOptions == null || (notificationOptions.notificationsAllowed() && !notificationOptions.isEventsBlocked())) {
                        try {
                            userLogic.sendNotification(user.getFcmToken(), user.getUserName(), title, descriptionNotification, linkToClick, picturePath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                    }
                }
            }
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public List<BringService> getService(String userName, int eventId) throws HttpRequestException{
        try {
            if(eventDao.getEvent(eventId) == null)
                throw new HttpRequestException(HttpStatus.NOT_FOUND.value(), "Event with id  "+eventId + " not found.");

            if(!hasUserPrivileges(eventId, userName))
                throw new HttpRequestException(HttpStatus.FORBIDDEN.value(), "Your don't have permission to see this event");

            return eventDao.getService(eventId);
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    public void updateBringservice(int eventId, String accepter, int serviceId) throws HttpRequestException{
        try{
            BringService bringService = eventDao.getOneService(serviceId);

            if(bringService.getAccepter() != null)
                throw new HttpRequestException(HttpStatus.BAD_REQUEST.value(), bringService.getAccepter() + " already accepted this task");

            eventDao.updateBringservice(eventId,accepter,serviceId);

            //Send notification
            User user = userLogic.getUser(bringService.getCreatorName());

            String title = "Task accepted";
            String descriptionNotification = user.getUserName() + " accepted your task \"" + bringService.getFood() + "\"";
            String linkToClick = "/event/" + eventId;
            String picturePath = "";

            //save notification
            userLogic.saveNotification(accepter, title, descriptionNotification,
                    user.getUserName(), linkToClick, picturePath);

            NotificationOptions notificationOptions = userLogic.getNotificationOptions(user.getUserName());
//            if (notificationOptions == null || (notificationOptions.notificationsAllowed() && !notificationOptions.isEventsBlocked())) {
                try {
                    userLogic.sendNotification(user.getFcmToken(), user.getUserName(), title, descriptionNotification, linkToClick, picturePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//            }
        }catch(DatabaseException e){
            throw new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    private void scheduleDeleteEvent(int eventId) throws DatabaseException {
        scheduleDeleteEvent(eventDao.getEvent(eventId));
    }

    private void scheduleDeleteEvent(Event event) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(event.getStartDate());
        cal.add(Calendar.SECOND, Config.DELETE_EVENT_AFTER_SECONDS);

        Date timeDelete = cal.getTime();

        int eventId = event.getEventId();
        JobDetail job = JobBuilder.newJob(DeleteEventJob.class)
                .usingJobData("eventId", eventId)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .startAt(timeDelete)
                .build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if an event is over and deletes it
     *
     * @param eventId
     */
    public void deleteEvent(int eventId) {
        try {
            Event event = eventDao.getEvent(eventId);
            if(event == null)
                return;

            Date now = new Date();
            if(now.getTime() - event.getStartDate().getTime() >= Config.DELETE_EVENT_AFTER_SECONDS * 1000) {
                eventDao.deleteEvent(eventId);
            }
        } catch(DatabaseException e) {
            e.printStackTrace();
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

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}

@Component
class DeleteEventJob implements Job {

    private EventLogic eventLogic;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        int eventId = dataMap.getInt("eventId");

        eventLogic.deleteEvent(eventId);
    }

    @Autowired
    public void setEventLogic(EventLogic eventLogic) {
        this.eventLogic = eventLogic;
    }
}
