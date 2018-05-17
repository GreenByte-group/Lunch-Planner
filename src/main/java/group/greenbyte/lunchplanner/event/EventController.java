package group.greenbyte.lunchplanner.event;

<<<<<<< HEAD
import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.user.TestInvitePersonJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
=======
import group.greenbyte.lunchplanner.event.database.Comment;
import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.security.SessionManager;
import group.greenbyte.lunchplanner.user.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
//import org.springframework.session.

>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RestController
<<<<<<< HEAD
=======
@CrossOrigin
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
@RequestMapping("/event")
public class EventController {

    private EventLogic eventLogic;

    /**
<<<<<<< HEAD
=======
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
            Event event = eventLogic.getEvent(SessionManager.getUserName(), eventId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(event);
        } catch (HttpRequestException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getErrorMessage());
        }
    }

    /**
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
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
<<<<<<< HEAD
            //TODO change userName
            int eventId = eventLogic.createEvent("dummy", event.getName(), event.getDescription(),
                    event.getLocationId(), event.getTimeStart(), event.getTimeEnd());
=======
            int eventId = eventLogic.createEvent(SessionManager.getUserName(), event.getName(), event.getDescription(),
                    event.getLocation(), event.getTimeStart());
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

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
<<<<<<< HEAD
            eventLogic.updateEventName("dummy",eventId,newEventName);
=======
            eventLogic.updateEventName(SessionManager.getUserName(),eventId,newEventName);
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
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
<<<<<<< HEAD
            eventLogic.updateEventLoction("dummy",eventId,Integer.valueOf(location));
=======
            eventLogic.updateEventLocation(SessionManager.getUserName(),eventId,location);
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

            return "";
        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
<<<<<<< HEAD
        }catch(NumberFormatException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return "not a number";
=======
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
        }
    }

    /**
     *
<<<<<<< HEAD
     * @param newEventDescription
=======
     * @param newEventDescription the updated event description
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
     * @param eventId id of the updated event
     * @param response response channel
     */
    @RequestMapping(value = "{eventId}/description", method = RequestMethod.PUT,
            consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String updateEventDescription(@RequestBody String newEventDescription, @PathVariable(value = "eventId") int eventId, HttpServletResponse response) {
        try {
<<<<<<< HEAD
            eventLogic.updateEventDescription("dummy",eventId,newEventDescription);
=======
            eventLogic.updateEventDescription(SessionManager.getUserName(),eventId,newEventDescription);
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
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
<<<<<<< HEAD
            eventLogic.updateEventTimeStart("dummy",eventId, new Date(Long.valueOf(newTimeStart)));
=======
            eventLogic.updateEventTimeStart(SessionManager.getUserName(),eventId, new Date(Long.valueOf(newTimeStart)));
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

            return "";
        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
<<<<<<< HEAD
        }catch(NumberFormatException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return "not a number";
=======
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
        }
    }

    /**
<<<<<<< HEAD
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
            return allSearchingEvents;
        } catch (HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            return null;
        }
    }


=======
     * Get all events that are visible for the user who created this request
     *
     * @return a list of all events
     */
    @RequestMapping(value = "", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity getAllEvents() {

        try {
            List<Event> allSearchingEvents = eventLogic.getAllEvents(SessionManager.getUserName());

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
             List<Event> searchingEvent = eventLogic.searchEventsForUser(SessionManager.getUserName(), searchword);

             return ResponseEntity
                     .status(HttpStatus.OK)
                     .body(searchingEvent);
         } catch (HttpRequestException e) {
             return ResponseEntity
                     .status(e.getStatusCode())
                     .body(e.getErrorMessage());
         }
    }

>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
    @RequestMapping(value = "/{userToInvite}/invite/event/{eventId}", method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE )
    @ResponseBody
    public String inviteFriend(@PathVariable("userToInvite") String userToInvite, @PathVariable ("eventId") int eventId, HttpServletResponse response){
        try {
<<<<<<< HEAD
            eventLogic.inviteFriend("dummy", userToInvite, eventId);
=======
            eventLogic.inviteFriend(SessionManager.getUserName(), userToInvite, eventId);
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }

        return "";
    }

<<<<<<< HEAD
=======
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
            eventLogic.reply(SessionManager.getUserName(), eventId, InvitationAnswer.fromString(answer));
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch(HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }
        return "";

    }

    /**
     *
     * @param eventId
     * @param comment
     * @param response
     * @return
     */
    @RequestMapping(value = "/{eventId}/comment", method = RequestMethod.PUT,
            consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String createComment(@PathVariable("eventId") int eventId, @RequestBody String comment, HttpServletResponse response){
        try{
            eventLogic.newComment(SessionManager.getUserName(), comment, eventId);
            response.setStatus(HttpServletResponse.SC_CREATED);

            return "";
        }catch(HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }

    }

    /**
     * Get all comments of an event
     *
     * @return a list of all comments
     */
    @RequestMapping(value = "/{eventId}/getComments", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity getAllComments(@PathVariable("eventId") int eventId) {

        try {
            List<Comment> allComments = eventLogic.getAllComments(SessionManager.getUserName(), eventId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(allComments);
        } catch (HttpRequestException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getErrorMessage());
        }
    }
>>>>>>> faa515c581e217f842d716b6e6b224743202cf56

    @Autowired
    public void setEventLogic(EventLogic eventLogic) {
        this.eventLogic = eventLogic;
    }

<<<<<<< HEAD
=======

>>>>>>> faa515c581e217f842d716b6e6b224743202cf56
}
