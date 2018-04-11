package group.greenbyte.lunchplanner.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserLogic userLogic;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createEvent(@RequestBody UserJson user) {

    }

    @Autowired
    public void setUserLogic(UserLogic userLogic) {
        this.userLogic = userLogic;
    }

}
