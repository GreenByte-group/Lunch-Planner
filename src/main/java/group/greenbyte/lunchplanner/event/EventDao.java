package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import group.greenbyte.lunchplanner.team.database.Team;

import java.util.Date;
import java.util.List;
import java.util.Set;


public interface EventDao {

    /**
     * Insert an event into the database
     *
     * @param userName id of the user who creates the events
     * @param eventName name of the event
     * @param description description of the event
     * @param locationId id of the location
     * @param timeStart time when the event starts
     * @param timeEnd time when the events ends
     * @return the inserted Event
     *
     * @throws DatabaseException when an unexpected error happens
     */
    Event insertEvent(String userName,
                      String eventName,
                      String description,
                      int locationId,
                      Date timeStart,
                      Date timeEnd) throws DatabaseException;

    /**
     * Gets the event with location but without usersInvited and teamsVisible
     *
     * @param eventId id of the event
     * @return the searched event
     * @throws DatabaseException when an unexpected error happens
     */
    Event getEvent(int eventId) throws DatabaseException;

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
                                 String description
                                 ) throws DatabaseException;

    /**
     *
     * @param eventId id of the event
     * @param locationId id of the location
     * @return the updated event
     * @throws DatabaseException when an unexpected error happens
     */
    Event updateEventLocation(int eventId,
                              int locationId) throws DatabaseException;

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
     *
     * @param eventId id of the event
     * @param timeEnd time when the events ends
     * @return the updated event
     * @throws DatabaseException when an unexpected error happens
     */
    Event updateEventTimeEnd(int  eventId,
                             Date timeEnd) throws DatabaseException;

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

    void putUserInviteToEventAsAdmin (String userToInviteName, int eventId) throws DatabaseException;

    /**
     * adds a team to the event
     *
     * @param eventId
     * @param teamId
     * @throws DatabaseException
     */
    void addTeamToEvent(int eventId, int teamId) throws DatabaseException;

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

    void putCommentForEvent(String userName, int eventId, String comment) throws DatabaseException;
  
    void replyInvitation(String userName, int eventId, InvitationAnswer answer) throws DatabaseException;
}
