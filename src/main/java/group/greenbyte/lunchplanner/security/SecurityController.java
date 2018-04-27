package group.greenbyte.lunchplanner.security;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin
@Controller
public class SecurityController {

    //TODO test
/*
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration() {
        return "registration";
    }
    */


//    @RequestMapping(value = "/login" ,method = RequestMethod.POST,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public String login() {
//        return "";
//    }
}