package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
public class EventController {

    private EventLogic eventLogic;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public int createEvent(@RequestBody EventJson event) {
        return 0;
    }

    @Autowired
    public void setEventLogic(EventLogic eventLogic) {
        this.eventLogic = eventLogic;
    }

}
