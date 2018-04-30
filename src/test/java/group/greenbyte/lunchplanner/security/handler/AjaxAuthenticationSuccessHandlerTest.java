package group.greenbyte.lunchplanner.security.handler;


import group.greenbyte.lunchplanner.user.UserLogic;
import group.greenbyte.lunchplanner.user.database.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

@RunWith(value = MockitoJUnitRunner.class)
public class AjaxAuthenticationSuccessHandlerTest {

    @InjectMocks
    AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;
    @Mock
    HeaderHandler headerHandler;
    @Mock
    UserLogic userLogic;

    @Test
    public void testOnAuthenticationSuccess() throws IOException, ServletException {
        //TODO
        /*HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn("name");
        when(request.getRemoteAddr()).thenReturn("localhost");

        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        String token = "token";
        User u = new User();
        u.setUserName("userma");
        u.setPassword("pass");
        u.setToken(token);

        when(userLogic.createUserToken(authentication.getName())).thenReturn(u);

        ajaxAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(headerHandler).process(request, response);
        verify(userLogic).createUserToken(authentication.getName()) ;
        verify(printWriter).print("{ \"token\" : \"" + u.getToken() + "\"}");
        */
    }

}
