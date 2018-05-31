package group.greenbyte.lunchplanner.event;

import com.google.firebase.messaging.FirebaseMessagingException;
import group.greenbyte.lunchplanner.event.database.BringService;
import group.greenbyte.lunchplanner.event.database.Comment;
import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.security.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

//import org.springframework.session.

@RestController
@CrossOrigin
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
            int eventId = eventLogic.createEvent(SessionManager.getUserName(), event.getName(), event.getDescription(),
                    event.getLocation(), event.getTimeStart(), event.isVisible());

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
            eventLogic.updateEventName(SessionManager.getUserName(),eventId,newEventName);
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
            eventLogic.updateEventLocation(SessionManager.getUserName(),eventId,location);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

            return "";
        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
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
            eventLogic.updateEventDescription(SessionManager.getUserName(),eventId,newEventDescription);
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
            eventLogic.updateEventTimeStart(SessionManager.getUserName(),eventId, new Date(Long.valueOf(newTimeStart)));
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

            return "";
        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
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

    /**
     * Invite a friend to an event
     *
     * @param userToInvite id of the user who is invited
     * @param eventId id of event
     * @param response response channel
     */
    @RequestMapping(value = "/{userToInvite}/invite/event/{eventId}", method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE )
    public String inviteFriend(@PathVariable("userToInvite") String userToInvite, @PathVariable ("eventId") int eventId, HttpServletResponse response) throws FirebaseMessagingException {
        try {
            eventLogic.inviteFriend(SessionManager.getUserName(), userToInvite, eventId);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }

        return "";
    }

    /**
     * Invite a team to an event
     *
     * @param eventId id of event
     * @param teamId id of the team that is invited
     * @param response response channel
     */
    @RequestMapping(value = "/{eventId}/inviteTeam/{teamId}", method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE )
    @ResponseBody
    public String inviteTeam(@PathVariable("eventId") int eventId, @PathVariable ("teamId") int teamId, HttpServletResponse response) throws FirebaseMessagingException {
        try {
            eventLogic.inviteTeam(SessionManager.getUserName(), eventId, teamId);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }

        return "";
    }

    /**
     * ToDo
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
     * create a comment for an event
     *
     * @param eventId event which bring service is dependent on
     * @param comment String of message
     * @param response response channel
     * @return empty String
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
     * Create bring service for an event
     *
     * @param eventId event which bring service is dependent on
     * @param bringService JSON includes food, description
     * @param response response channel
     * @return empty String
     */
    @RequestMapping(value = "/{eventId}/service", method = RequestMethod.PUT,
                    consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String putService(@PathVariable("eventId") int eventId, @RequestBody BringServiceJson bringService, HttpServletResponse response){
        try{
            eventLogic.putService(SessionManager.getUserName(), eventId, bringService.getFood(), bringService.getDescription());
            response.setStatus(HttpServletResponse.SC_CREATED);
            return "";
        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }
    }

    /**
     * Get servicelist from event
     *
     * @param eventId of event with servicelist
     * @return servicelist of event with id
     */
    @RequestMapping(value = "/{eventId}/service", method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity getService(@PathVariable("eventId") int eventId) {
        try{
            //TODO permission
            List<BringService> serviceList = eventLogic.getService(SessionManager.getUserName(), eventId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(serviceList);
        }catch(HttpRequestException e){
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getErrorMessage());
        }
    }

    /**
     * Accept to bring a item of the bring service
     *
     * @param eventId of event with servicelist
     * @param serviceId of serviceList in bringServiceDatabase
     * @param response response channel
     * @return empty String
     */
    @RequestMapping(value = "/{eventId}/service/{serviceId}", method = RequestMethod.POST)
    @ResponseBody
    public String acceptBringservice(@PathVariable("eventId") int eventId,
                                     @PathVariable("serviceId") int serviceId, HttpServletResponse response){
        try{
            eventLogic.updateBringservice(eventId,SessionManager.getUserName(),serviceId);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return "";
        }catch(HttpRequestException e){
            response.setStatus(e.getStatusCode());
            return e.getMessage();
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

    @RequestMapping(value = "/{eventId}/token", method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity getTokenForEvent(@PathVariable("eventId") int eventId) {
        try {
            String token = eventLogic.getShareToken(eventId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(token);
        } catch(HttpRequestException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getErrorMessage());
        }
    }

    // -------------------- UNAUTHORIZED ------------------------
    @RequestMapping(value = "/token/{shareToken}", method = RequestMethod.GET)
    public ResponseEntity getEventByToken(@PathVariable("shareToken") String shareToken) {
        try {
            Event event = eventLogic.getEventByToken(shareToken);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(event);
        } catch(HttpRequestException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getErrorMessage());
        }
    }

    @Autowired
    public void setEventLogic(EventLogic eventLogic) {
        this.eventLogic = eventLogic;
    }


}
