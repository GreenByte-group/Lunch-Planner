package group.greenbyte.lunchplanner.event.database;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventDatabaseConnector extends CrudRepository<Event, Integer> {

    Event getByEventId(int eventId);

    List<Event> getAllByIsPublic(boolean isPublic);

    //TODO check if that is right
    List<Event> getAllByTeamsVisibleContaining(int teamId);

    //TODO check if that is right
    List<Event> getAllByUsersInvitedContains(String userName);

}
