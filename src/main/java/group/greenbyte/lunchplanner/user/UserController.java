package group.greenbyte.lunchplanner.user;

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
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Autowired
    public void setUserLogic(UserLogic userLogic) {
        this.userLogic = userLogic;
    }

}
