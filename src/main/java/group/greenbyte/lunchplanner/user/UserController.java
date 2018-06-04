package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.security.SessionManager;
import group.greenbyte.lunchplanner.user.database.notifications.NotificationOptions;
import group.greenbyte.lunchplanner.user.database.notifications.Notifications;
import group.greenbyte.lunchplanner.user.database.User;
import group.greenbyte.lunchplanner.user.database.notifications.OptionsJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
//@CrossOrigin
@RequestMapping("/user")
public class UserController {


//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    private SpringSessionRememberMeServices a;

    private UserLogic userLogic;

    /**
     * Create user with username, password and mail
     *
     * @param user is a json object with all attributes from UserJson
     * @return error message or nothing
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String createUser(@RequestBody UserJson user,
                             HttpServletResponse response) {

        try {
            userLogic.createUser(user.getUserName(), user.getPassword(), user.getMail());
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }

        return "";
    }

    /**
     * Set the fcm (firebase cloud messaging) token for push notifications
     *
     * @return error message or nothing
     */
    @RequestMapping(value = "/fcm", method = RequestMethod.POST)
    public String setFcm(@RequestBody FcmToken fcmToken,
                             HttpServletResponse response) {
        try {
            userLogic.addFcmToken(SessionManager.getUserName(), fcmToken.getFcmToken());
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            return e.getErrorMessage();
        }

        return "";
    }

    /**
     *
     * @param toSearch searchword for Database to search for User/s
     * @return a List of User/s which is showing async in forntend. If Exception or
     * list is empty, null is returning back
     */
    @RequestMapping(value = "/search/{searchWord}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity searchUser(@PathVariable("searchWord") String toSearch) {

        try {
            List<User> toReturn =  userLogic.searchUserByName(toSearch);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(toReturn);
        } catch (HttpRequestException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getErrorMessage());
        }

    }

    //TODO write tests for this function
    /**
     *
     * @return a List of all users
     */
    @RequestMapping(value = "", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity getAllUser() {

        try {
            List<User> toReturn =  userLogic.searchUserByName("");
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(toReturn);
        } catch (HttpRequestException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getErrorMessage());
        }

    }

    /**
     * TODO write tests
     * @return one user
     */
    @RequestMapping(value = "{username}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUser(@PathVariable("username") String username) {
        try {
            User user = userLogic.getUser(username);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(user);
        } catch (HttpRequestException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getErrorMessage());
        }

    }

    /**
     *
     * @return a List of all notifications
     */

    @RequestMapping(value ="/notifications", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity getNotifications() {
        try {
            List<Notifications> toReturn =  userLogic.getNotifications(SessionManager.getUserName());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(toReturn);
        } catch (HttpRequestException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getErrorMessage());
        }
    }

    /**
     *
     * @return the users notification options
     */
    @RequestMapping(value = "/options/notifications",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getNotificationOptions() {
        try {
            NotificationOptions notificationOptions = userLogic.getNotificationOptions(SessionManager.getUserName());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(notificationOptions);
        } catch (HttpRequestException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getErrorMessage());
        }
    }

    @RequestMapping(value = "/options/notifications/update", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String updateNotificationOptions(@RequestBody OptionsJson options, HttpServletResponse response){
        try {
            userLogic.updateNotificationOptions(SessionManager.getUserName(), options.getBlockAll(),options.getBlockedUntil(),
                    options.getBlock_until(), options.getBlockedForWork(), options.getStart_working(), options.getStop_working(),
                    options.getEventsBlocked(),options.getTeamsBlocked(),options.getSubscriptionsBlocked());

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return "";
        } catch (HttpRequestException e) {
            response.setStatus(e.getStatusCode());
            e.printStackTrace();
            return e.getErrorMessage();
        }
    }





//    /**
//     *
//     * @param userToInvite
//     * @param eventId
//     */
//    @RequestMapping(value = "/invitations", method = RequestMethod.GET,
//                    produces = MediaType.APPLICATION_JSON_VALUE)
//    public String getInvitation(@PathVariable String userToInvite, @PathVariable int eventId, HttpServletResponse response) {
//        response.setStatus(HttpServletResponse.SC_OK);
//        return "";
//    }

    @Autowired
    public void setUserLogic(UserLogic userLogic) {
        this.userLogic = userLogic;
    }

}

class FcmToken {
    private String fcmToken;

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
