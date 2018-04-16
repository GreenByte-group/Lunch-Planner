package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
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
                    event.getLocationId(), event.getTimeStart(), event.getTimeEnd());

            response.setStatus(HttpServletResponse.SC_CREATED);
            return String.valueOf(eventId);
        } catch (HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            e.printStackTrace();
            return e.getErrorMessage();
        }
    }

    /**
     *
     * @param newEventName new name of event to update in Database
     * @param eventId id of the updated event
     * @param response response channel
     */
    @RequestMapping(value = "{eventId}/name", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void updateEventName(@RequestBody String newEventName, @PathVariable(value = "eventId") int eventId, HttpServletResponse response) {


        try {
            eventLogic.updateEventName("dummy",eventId,newEventName);
            response.setStatus(HttpServletResponse.SC_CREATED);

        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
        }
    }

    /**
     *
     * @param location new location of event to updte in Database
     * @param eventId id the updated event
     * @param response response channel
     */
    @RequestMapping(value = "{eventId}/location", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void updateEventLocation(@RequestBody int location, @PathVariable(value = "eventId") int eventId, HttpServletResponse response) {


        try {
            eventLogic.updateEventLoction("dummy",eventId,location);
            response.setStatus(HttpServletResponse.SC_CREATED);

        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
        }
    }

    /**
     *
     * @param newEventDescription
     * @param eventId id of the updated event
     * @param response response channel
     */
    @RequestMapping(value = "{eventId}/description", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void updateEventDescription(@RequestBody String newEventDescription, @PathVariable(value = "eventId") int eventId, HttpServletResponse response) {


        try {
            eventLogic.updateEventDescription("dummy",eventId,newEventDescription);
            response.setStatus(HttpServletResponse.SC_CREATED);

        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
        }
    }

    /**
     *
     * @param newTimeStart new start time to update in the event
     * @param eventId id of the updated event
     * @param response response channel
     */
    @RequestMapping(value = "{eventId}/timeStart", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void updateEventTimeStart(@RequestBody Date newTimeStart, @PathVariable(value = "eventId") int eventId, HttpServletResponse response) {


        try {
            eventLogic.updateEventTimeEnd("dummy",eventId,newTimeStart);
            response.setStatus(HttpServletResponse.SC_CREATED);

        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
        }
    }

    /**
     *
     * @param newTimeEnd new Date to update in Event
     * @param eventId id of the updated event
     * @param response response channel
     */
    @RequestMapping(value = "{eventId}/timeEnd", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void updateEventTimEnd(@RequestBody Date newTimeEnd, @PathVariable(value = "eventId") int eventId, HttpServletResponse response) {


        try {
            eventLogic.updateEventTimeEnd("dummy",eventId,newTimeEnd);
            response.setStatus(HttpServletResponse.SC_CREATED);

        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
        }
    }

    /**
     *
     * @param response response channel
     * @return a list of all events
     */
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





    @Autowired
    public void setEventLogic(EventLogic eventLogic) {
        this.eventLogic = eventLogic;
    }

}
