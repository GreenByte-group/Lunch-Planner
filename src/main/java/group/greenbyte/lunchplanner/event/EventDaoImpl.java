package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.Date;
import group.greenbyte.lunchplanner.exceptions.DatabaseException;
import org.springframework.stereotype.Repository;

@Repository
public class EventDaoImpl implements EventDao {
    @Override
    public Event insertEvent(String creatorsName, String eventName, String description, int locationId, Date timeStart, Date timeEnd) throws DatabaseException {
        return null;
    }
}
