package group.greenbyte.lunchplanner.security;

import group.greenbyte.lunchplanner.user.UserLogic;
import group.greenbyte.lunchplanner.user.database.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    static final String AUTHORIZATION = "Authorization";
    static final String UTF_8 = "UTF-8";
    static final int BEGIN_INDEX = 7;
    private final Log logger = LogFactory.getLog(this.getClass());

    final UserLogic userLogic;

    final SecurityAppContext securityAppContext;

    @Autowired
    public JwtAuthenticationTokenFilter(SecurityAppContext securityAppContext, UserLogic userLogic) {
        this.securityAppContext = securityAppContext;
        this.userLogic = userLogic;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authToken = request.getHeader(AUTHORIZATION);
        if(authToken != null) {
            try {
                SecurityContext context = securityAppContext.getContext();
                if(context.getAuthentication() == null) {
                    logger.info("Checking authentication for token " + authToken);
                    User u = userLogic.validateUser(authToken);
                    if(u != null) {
                        logger.info("User " + u.getUserName() + " found.");
                        Authentication authentication = create(u);
                        context.setAuthentication(authentication);
                    }
                }
            } catch (StringIndexOutOfBoundsException e) {
                logger.error(e.getMessage());
            }

        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken create(User u) {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(u.getUserName());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(u.getUserName(), u.getPassword(), Arrays.asList(simpleGrantedAuthority));
        return authentication;
    }

}