package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
//import org.springframework.session.

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    private EventLogic eventLogic;

    /**
     * Returns one event by his id
     *
     * @param eventId id of the event
     * @param response is used to send a response code
     * @return the event
     */
    @RequestMapping(value = "/{eventId}",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public Event getEvent(@PathVariable("eventId") int eventId, HttpServletResponse response) {
        try {
            Event event = eventLogic.getEvent("dummy", eventId);
            if(event != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                return event;
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            //TODO how to send error message
        }

        return null;
    }

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
     * Updates the event with the specific id
     *
     * @param newEventName new name of event to update in Database
     * @param eventId id of the updated event
     * @param response response channel
     */
    @RequestMapping(value = "/{eventId}/name", method = RequestMethod.PUT,
            consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String updateEventName(@RequestBody String newEventName, @PathVariable(value = "eventId") int eventId, HttpServletResponse response) {

        try {
            eventLogic.updateEventName("dummy",eventId,newEventName);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

            return "";
        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }
    }

    /**
     *
     * @param location new location of event to updte in Database
     * @param eventId id the updated event
     * @param response response channel
     */
    @RequestMapping(value = "{eventId}/location", method = RequestMethod.PUT,
            consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String updateEventLocation(@RequestBody String location, @PathVariable(value = "eventId") int eventId, HttpServletResponse response) {
        try {
            eventLogic.updateEventLocation("dummy",eventId,Integer.valueOf(location));
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

            return "";
        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }catch(NumberFormatException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return "not a number";
        }
    }

    /**
     *
     * @param newEventDescription
     * @param eventId id of the updated event
     * @param response response channel
     */
    @RequestMapping(value = "{eventId}/description", method = RequestMethod.PUT,
            consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String updateEventDescription(@RequestBody String newEventDescription, @PathVariable(value = "eventId") int eventId, HttpServletResponse response) {
        try {
            eventLogic.updateEventDescription("dummy",eventId,newEventDescription);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

            return "";
        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }
    }

    /**
     *
     * @param newTimeStart new start time to update in the event
     * @param eventId id of the updated event
     * @param response response channel
     */
    @RequestMapping(value = "{eventId}/timestart", method = RequestMethod.PUT,
            consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String updateEventTimeStart(@RequestBody String newTimeStart, @PathVariable(value = "eventId") int eventId, HttpServletResponse response) {
        try {
            eventLogic.updateEventTimeStart("dummy",eventId, new Date(Long.valueOf(newTimeStart)));
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

            return "";
        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }catch(NumberFormatException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return "not a number";
        }
    }

    /**
     *
     * @param newTimeEnd new Date to update in Event
     * @param eventId id of the updated event
     * @param response response channel
     */
    @RequestMapping(value = "{eventId}/timeend", method = RequestMethod.PUT,
            consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String updateEventTimEnd(@RequestBody String newTimeEnd, @PathVariable(value = "eventId") int eventId, HttpServletResponse response) {
        try {
            eventLogic.updateEventTimeEnd("dummy",eventId, new Date(Long.valueOf(newTimeEnd)));
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

            return "";
        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }catch(NumberFormatException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return "not a number";
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

            for(Event event : allSearchingEvents) {
                event.getLocation().setEvents(null);
            }

            return allSearchingEvents;
        } catch (HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            return null;
        }
    }


    /**
     * only here for throwing an exception is no searchword is giving
     */
    @RequestMapping(value = "/search/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void searchEventNoSearchWord() {

    }

    @RequestMapping(value = "/search/{searchWord}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Event> searchEvents(@PathVariable("searchWord") String searchword, HttpServletResponse response){

     try{
         List<Event> searchingEvent = eventLogic.searchEventsForUser("dummy", searchword);
         response.setStatus(HttpServletResponse.SC_OK);
         return searchingEvent;
     } catch (HttpRequestException e) {
         response.setStatus(e.getStatusCode());
         return null;
     }
    }

    @RequestMapping(value = "/{userToInvite}/invite/event/{eventId}", method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE )
    @ResponseBody
    public String inviteFriend(@PathVariable("userToInvite") String userToInvite, @PathVariable ("eventId") int eventId, HttpServletResponse response){
        try {
            eventLogic.inviteFriend("dummy", userToInvite, eventId);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }

        return "";
    }


    @Autowired
    public void setEventLogic(EventLogic eventLogic) {
        this.eventLogic = eventLogic;
    }

}
