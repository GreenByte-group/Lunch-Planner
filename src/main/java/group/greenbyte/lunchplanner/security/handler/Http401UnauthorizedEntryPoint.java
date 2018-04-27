
package group.greenbyte.lunchplanner.security.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Returns a 401 error code (Unauthorized) to the client.
 */
@Component
public class Http401UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(Http401UnauthorizedEntryPoint.class);
    private final HeaderHandler headerHandler;

    @Autowired
    public Http401UnauthorizedEntryPoint(HeaderHandler headerHandler) {
        this.headerHandler = headerHandler;
    }

    /**
     * Always returns a 401 error code to the client.
     */
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2) throws IOException,ServletException {
        LOGGER.debug("Pre-authenticated entry point called. Rejecting access:" + request.getRequestURL());
        headerHandler.process(request, response);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
    }
}