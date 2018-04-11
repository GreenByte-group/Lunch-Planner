package group.greenbyte.lunchplanner.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
public class EventController {

    private EventLogic eventLogic;

    /**
     * Create an event with all the data given in EventJson
     *
     * @param event the object that describes the JSON object in java format
     * @return the id ov the created event
     */
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String createEvent(@RequestBody EventJson event) {
        return "0";
    }

    @Autowired
    public void setEventLogic(EventLogic eventLogic) {
        this.eventLogic = eventLogic;
    }

}
