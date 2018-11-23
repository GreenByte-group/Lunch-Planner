package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.BringService;
import group.greenbyte.lunchplanner.event.database.Comment;
import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.event.database.EventInvitationDataForReturn;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.user.database.User;

import java.util.Date;
import java.util.List;


public interface EventDao {

    /**
     * Insert an event into the database
     *
     * @param userName id of the user who creates the events
     * @param eventName name of the event
     * @param description description of the event
     * @param timeStart time when the event starts
     * @return the inserted Event
     *
     * @throws DatabaseException when an unexpected error happens
     */
    Event insertEvent(String userName,
                      String eventName,
                      String description,
                      String location,
                      Date timeStart,
                      boolean isPublic,
                      String locationId,
                      String lat,
                      String lng) throws DatabaseException;

    Event insertEvent(String userName,
                      String eventName,
                      String description,
                      String location,
                      Date timeStart,
                      boolean isPublic) throws DatabaseException;

    void deleteInvitationsForEvent(int eventId) throws DatabaseException;

    void deleteBringServiceForEvent(int eventId) throws DatabaseException;

    List<User> getReply(int eventId) throws DatabaseException;

    void deleteCommentsForEvent(int eventId) throws DatabaseException;

    /**
     * deleted user will delete from event
     * @param username user to delete
     * @throws DatabaseException
     */
    void  deleteUserInvitation(String username) throws DatabaseException;

    /**
     * delete user from bringservice as an creator and accepter
     * @param username
     * @throws DatabaseException
     */
    void deleteUserBringservice(String username) throws DatabaseException;



    /**
     * Delete one event from the database
     *
     * @param eventId id of the event
     * @throws DatabaseException
     */
    void deleteEvent(int eventId) throws DatabaseException;

    /**
     * Gets the event with location but without usersInvited and teamsVisible
     *
     * @param eventId id of the event
     * @return the searched event
     * @throws DatabaseException when an unexpected error happens
     */
    Event getEvent(int eventId) throws DatabaseException;

    List<Event> getAllEvents() throws DatabaseException;

    /**
     *
     * @param eventId id of the event
     * @param eventName name of the event
     * @return the updated event
     * @throws DatabaseException
     */
    Event updateEventName(int eventId,
                          String eventName) throws DatabaseException;


    /**
     *
     * @param eventId id of the event
     * @param description description of the event
     * @return the updated event
     * @throws DatabaseException when an unexpected error happens
     */
    Event updateEventDescription(int eventId,
                                 String description) throws DatabaseException;

    /**
     *
     * @param eventId id of the event
     * @param location Name, Adress or google place api id key
     * @return the updated event
     * @throws DatabaseException when an unexpected error happens
     */
    Event updateEventLocation(int eventId,
                              String location) throws DatabaseException;

    Event updateEventLocationCoordinates(int eventId, String lat, String lng,
                              String placeId) throws DatabaseException;

    /**
     *
     * @param eventId id of the event
     * @param timeStart time when the event starts
     * @return the updated event
     * @throws DatabaseException when an unexpected error happens
     */
    Event updateEventTimeStart(int eventId,
                               Date timeStart) throws DatabaseException;

    /**
     * For now only for test purpose
     * @param eventId
     * @param isPublic
     * @throws DatabaseException
     */
    void updateEventIsPublic(int eventId, boolean isPublic) throws DatabaseException;

    /**
     * Insert an new invited user into an event
     *
     * @param userToInviteName id of the user who is invited
     * @param eventId id of the event
     * @return the Event of the invitation
     *
     * @throws DatabaseException when an unexpected error happens
     */
    Event putUserInviteToEvent (String userToInviteName, int eventId) throws DatabaseException;

    /**
     * Find all events that are public for all
     *
     * @param searchword for what the user is searching
     * @return a list of events matching the search
     * @throws DatabaseException when an error happens
     */
    List<Event> findPublicEvents(String searchword) throws DatabaseException;

    /**
     * Find all event that are public for a team
     *
     * @param teamId team
     * @param searchword for what the user is searching
     * @return a list of events matching the search
     * @throws DatabaseException when an error happens
     */
    List<Event> findEventsForTeam(int teamId, String searchword) throws DatabaseException;

    /**
     * Find all event where an user is invited to
     *
     * @param userName the user
     * @param searchword for what the user is searching
     * @return a list of events matching the search
     * @throws DatabaseException when an error happens
     */
    List<Event> findEventsUserInvited(String userName, String searchword) throws DatabaseException;

    /**
     *
     * @param creater name of the service creater (i know its creator, but IDGAF)
     * @param eventId id of the event
     * @param food name of the food to bring with
     * @param description description of special wishes
     * @throws DatabaseException when an error happens
     */
    void putService(String creater, int eventId, String food, String description) throws DatabaseException;



    /**
     *
     * @param eventId id of the event
     * @return servicelist of event
     * @throws DatabaseException when an error happens with statuscode and message
     */
    List<BringService> getService(int eventId) throws DatabaseException;

    /**
     *
     * @param serviceId
     * @return
     * @throws DatabaseException
     */
    BringService getOneService(int serviceId) throws DatabaseException;

    /**
     *
     * @param eventId
     * @param accepter
     * @param serviceId
     */
    void updateBringservice(int eventId, String accepter, int serviceId, int price) throws DatabaseException;

    void putUserInviteToEventAsAdmin (String userToInviteName, int eventId) throws DatabaseException;

    /**
     * adds a team to the event
     *
     * @param eventId
     * @param teamId
     * @throws DatabaseException
     */
    void addTeamToEvent(int eventId, int teamId) throws DatabaseException;

    boolean isEventPublic(int eventId) throws DatabaseException;

    /**
     * checks if a user has admin privileges for the given event
     *
     * @param userName
     * @param eventId
     * @return true when the user has admin privileges, false if not
     * @throws DatabaseException
     */
    boolean userHasAdminPrivileges(String userName, int eventId) throws DatabaseException;

    /**
     * checks if a user has privileges for the given event
     *
     * @param userName
     * @param eventId
     * @return true when the user has privileges, false if not
     * @throws DatabaseException
     */
    boolean userHasPrivileges(String userName, int eventId) throws DatabaseException;

    /**
     *
     * @param eventId
     * @return
     * @throws DatabaseException
     */
    List<Comment> getAllComments(int eventId) throws DatabaseException;

    void putCommentForEvent(String userName, int eventId, String comment) throws DatabaseException;

    void deleteUserComments(String username) throws DatabaseException;


    List<EventInvitationDataForReturn> getInvitations(int eventId) throws DatabaseException;

    void replyInvitation(String userName, int eventId, InvitationAnswer answer) throws DatabaseException;

    /**
     * Add a token to the event to access it
     * //TODO write tests
     *
     * @param eventId
     * @param shareToken
     */
    void addShareToken(int eventId, String shareToken) throws DatabaseException;

    /**
     * Get team by share token
     * TODO write tests
     *
     * @param token
     * @return event
     */
    Event getEventByShareToken(String token) throws DatabaseException;
}
