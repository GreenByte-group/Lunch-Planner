package group.greenbyte.lunchplanner.security;

import group.greenbyte.lunchplanner.user.database.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SessionManager {

    public static String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof User)
            return ((User) authentication.getPrincipal()).getUserName();
        else if(authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User)
            return ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();

        return null;
    }

}
