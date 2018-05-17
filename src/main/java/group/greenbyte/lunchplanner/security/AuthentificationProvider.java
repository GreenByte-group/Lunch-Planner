package group.greenbyte.lunchplanner.security;

import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import group.greenbyte.lunchplanner.user.SecurityHelper;
import group.greenbyte.lunchplanner.user.UserLogic;
import group.greenbyte.lunchplanner.user.database.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
public class AuthentificationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthentificationProvider.class);

    private final UserLogic userLogic;
    @Autowired
    public AuthentificationProvider(UserLogic userLogic) {
        this.userLogic = userLogic;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String username = authentication.getName();
            String password = (String) authentication.getCredentials();

            User user = userLogic.getUser(username);

            if (user == null || !user.getUserName().equalsIgnoreCase(username)) {
                throw new BadCredentialsException("Username not found.");
            }

            if (!SecurityHelper.validatePassword(password, user.getPassword())) {
                throw new BadCredentialsException("Wrong password.");
            }

            Collection<? extends GrantedAuthority> authorities = Collections.singleton(new Authority(username));

            return new UsernamePasswordAuthenticationToken(user, password, authorities);
        } catch (HttpRequestException e) {
            e.printStackTrace();
            throw new BadCredentialsException("Database exception");
        }
    }

    public boolean supports(Class<?> arg0) {
        return true;
    }

    class Authority implements GrantedAuthority {

        private String username;

        Authority(String username) {
            this.username = username;
        }

        @Override
        public String getAuthority() {
            return username;
        }
    }
}
