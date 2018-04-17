package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.user.TestInvitePersonJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

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
    @RequestMapping(value = "", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String createEvent(@RequestBody EventJson event, HttpServletResponse response) {

        try {
            //TODO change userName
            int eventId = eventLogic.createEvent("dummy", event.getName(), event.getDescription(),
                    event.getLocationId(), new Date(event.getTimeStart()), new Date(event.getTimeEnd()));

            response.setStatus(HttpServletResponse.SC_CREATED);
            return String.valueOf(eventId);
        } catch (HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            e.printStackTrace();
            return e.getErrorMessage();
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Event> getAllEvents(HttpServletResponse response) {

        try {
            List<Event> allSearchingEvents = eventLogic.getAllEvents("dummy");
            response.setStatus(HttpServletResponse.SC_OK);
            return allSearchingEvents;
        } catch (HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            return null;
        }
    }


    @RequestMapping(value = "/{userToInvite}/invite/event/{eventId}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE )
    @ResponseBody
    public void inviteFriend(@PathVariable TestInvitePersonJson json, @PathVariable("userToInvite") String userToInvite, @RequestParam ("eventId") int eventId, HttpServletResponse response){
        try {
            eventLogic.inviteFriend(json.getUsername(), json.getToInviteUsername(), json.getEventId());
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (HttpRequestException e) {
            response.setStatus(e.getStatusCode());
        }
    }


    @Autowired
    public void setEventLogic(EventLogic eventLogic) {
        this.eventLogic = eventLogic;
    }

}
