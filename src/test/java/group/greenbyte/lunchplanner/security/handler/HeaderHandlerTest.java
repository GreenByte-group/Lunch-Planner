package group.greenbyte.lunchplanner.security.handler;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static group.greenbyte.lunchplanner.security.handler.HeaderHandler.*;
import static org.mockito.Mockito.*;


public class HeaderHandlerTest {

    HeaderHandler headerHandler;

    @Before
    public void before() {
        headerHandler = new HeaderHandler();
    }

    @Test
    public void testProcess() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getMethod()).thenReturn(OPTIONS);
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        headerHandler.process(request, response);

        verify(response).setHeader(ALLOW_ORIGIN, STAR);
        verify(response).setHeader(ALLOW_CREDENTIALS, TRUE);
        verify(response).setHeader(ALLOW_HEADERS,  request.getHeader(REQUEST_HEADERS));
        verify(printWriter).print(OK);
        verify(printWriter).flush();
    }

    @Test
    public void testProcessMethodGet() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getMethod()).thenReturn("GET");
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        headerHandler.process(request, response);

        verify(response).setHeader(ALLOW_ORIGIN, STAR);
        verify(response).setHeader(ALLOW_CREDENTIALS, TRUE);
        verify(response).setHeader(ALLOW_HEADERS,  request.getHeader(REQUEST_HEADERS));
        verify(printWriter, never()).print(OK);
        verify(printWriter, never()).flush();
    }

}
