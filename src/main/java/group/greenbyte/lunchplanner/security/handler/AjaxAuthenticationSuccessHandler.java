
package group.greenbyte.lunchplanner.security.handler;

import group.greenbyte.lunchplanner.user.UserLogic;
import group.greenbyte.lunchplanner.user.database.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@Component
public class AjaxAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    final HeaderHandler headerHandler;
    final UserLogic userLogic;

    @Autowired
    public AjaxAuthenticationSuccessHandler(HeaderHandler headerHandler, UserLogic userLogic) {
        this.headerHandler = headerHandler;
        this.userLogic = userLogic;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.debug("Authentication Successful");
        User u = userLogic.createUserToken(((User) authentication.getPrincipal()).getUserName());
        response.getWriter().print("{ \"token\" : \"" + u.getToken() + "\"}");
        response.setStatus(HttpServletResponse.SC_OK);
        headerHandler.process(request, response);
    }

}
