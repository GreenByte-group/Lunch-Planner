package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
     * @return the event
     */
    @RequestMapping(value = "/{eventId}",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getEvent(@PathVariable("eventId") int eventId) {
        try {
            Event event = eventLogic.getEvent("dummy", eventId);
            if(event != null) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(event);
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Event with event-id: " + eventId + "was not found");
            }

        } catch (HttpRequestException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getErrorMessage());
        }
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
     * @param newEventDescription the updated event description
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
     * Get all events that are visible for the user who created this request
     *
     * @return a list of all events
     */
    @RequestMapping(value = "", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity getAllEvents() {

        try {
            List<Event> allSearchingEvents = eventLogic.getAllEvents("dummy");

            for(Event event : allSearchingEvents) {
                event.getLocation().setEvents(null);
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(allSearchingEvents);
        } catch (HttpRequestException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getErrorMessage());
        }
    }


    /**
     * only here for throwing an exception is no searchword is giving
     */
    @RequestMapping(value = "/search/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String searchEventNoSearchWord() {
        return "No searchword";
    }

    /**
     * Search events that are visible for the user who created this request
     *
     * @param searchword what to search
     * @return all events or an error message
     */
    @RequestMapping(value = "/search/{searchWord}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity searchEvents(@PathVariable("searchWord") String searchword){
         try{
             List<Event> searchingEvent = eventLogic.searchEventsForUser("dummy", searchword);

             return ResponseEntity
                     .status(HttpStatus.OK)
                     .body(searchingEvent);
         } catch (HttpRequestException e) {
             return ResponseEntity
                     .status(e.getStatusCode())
                     .body(e.getErrorMessage());
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

    /**
     *
     * @param eventId id of the event
     * @param answer answer of the user
     */
    @RequestMapping(value = "/{eventId}/reply", method = RequestMethod.PUT,
           consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String reply(@PathVariable("eventId") int eventId, @RequestBody String answer, HttpServletResponse response){
        try {
            eventLogic.reply("dummy", eventId, InvitationAnswer.fromString(answer));
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch(HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        } catch(IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return e.getMessage();
        }
        return "";

    }


    @Autowired
    public void setEventLogic(EventLogic eventLogic) {
        this.eventLogic = eventLogic;
    }


}
