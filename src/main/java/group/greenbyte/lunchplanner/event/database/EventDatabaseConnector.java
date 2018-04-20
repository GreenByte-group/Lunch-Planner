package group.greenbyte.lunchplanner.event.database;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventDatabaseConnector extends CrudRepository<Event, Integer> {

    Event getByEventId(int eventId);

    @Query("select e from Event e INNER JOIN e.usersInvited ui where ui.userInvited.userName = ?1")
    List<Event> searchInvitetPerson(String userName, String search);

//    //TODO check if that is right
//    List<Event> findByEventNameContainingOrEventDescriptionContainingAndIsPublic(String eventName, String eventDescription, boolean isPublic);
//
//    List<Event> findByTeamsVisibleContains(int teamId);
//
//    //TODO check if that is right
//    List<Event> getAllByTeamsVisibleContaining(int teamId);
//
//    //TODO check if that is right
//    List<Event> getAllByUsersInvitedContains(String userName);

}
