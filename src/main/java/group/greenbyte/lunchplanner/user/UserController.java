package group.greenbyte.lunchplanner.user;

import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserLogic userLogic;

    /**
     * Create user with username, password and mail
     *
     * @param user is a json object with all attributes from UserJson
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void createUser(@RequestBody UserJson user, HttpServletResponse response) {

        try {
            userLogic.createUser(user.getUserName(), user.getPassword(), user.getMail());
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (HttpRequestException e) {
            response.setStatus(e.getStatusCode());
        }
    }

    @Autowired
    public void setUserLogic(UserLogic userLogic) {
        this.userLogic = userLogic;
    }

}
