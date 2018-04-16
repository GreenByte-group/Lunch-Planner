package group.greenbyte.lunchplanner.event;

import group.greenbyte.lunchplanner.AppConfig;
import group.greenbyte.lunchplanner.event.database.Event;
import group.greenbyte.lunchplanner.exceptions.HttpRequestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.Utils.getJsonFromObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
public class EventLogicTest {

    @Autowired
    private EventLogic eventLogic;

    // ------------------------- CREATE EVENT ------------------------------

    @Test
    public void test1createEventNoDescription() throws Exception{
        String userName = createString(50);
        String eventName = createString(50);
        String description = "";
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        int result = eventLogic.createEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date (timeEnd));

    }

    @Test
    public void test2createEventLongDescription() throws Exception {
        String userName = createString(50);
        String eventName = createString(50);
        String description = createString(1000);
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        int result = eventLogic.createEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date (timeEnd));
    }

    @Test(expected = HttpRequestException.class)
    public void test3createEventEmptyUserName() throws Exception {
        String userName = "";
        String eventName = createString(50);
        String description = "";
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        int result = eventLogic.createEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date (timeEnd));
    }

    @Test(expected = HttpRequestException.class)
    public void test4createEventTooLongUserName() throws Exception {
        String userName = createString(51);
        String eventName = createString(50);
        String description = "";
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        int result = eventLogic.createEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date (timeEnd));
    }

    @Test(expected = HttpRequestException.class)
    public void test5createEventEmptyEventName() throws Exception {
        String userName = createString(50);
        String eventName = "";
        String description = "";
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        int result = eventLogic.createEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date (timeEnd));
    }

    @Test(expected = HttpRequestException.class)
    public void test6createEventTooLongEventName() throws Exception {
        String userName = createString(50);
        String eventName = createString(51);
        String description = "";
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        int result = eventLogic.createEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date (timeEnd));
    }

    @Test(expected = HttpRequestException.class)
    public void test7createEventTooLongDescription() throws Exception {
        String userName = createString(50);
        String eventName = createString(50);
        String description = createString(1001);
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart + 10000;

        int result = eventLogic.createEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date (timeEnd));
    }

    @Test(expected = HttpRequestException.class)
    public void test8createEventTimeStartTooEarly() throws Exception {
        String userName = createString(50);
        String eventName = createString(50);
        String description = "";
        int locationId = 1;
        long timeStart = System.currentTimeMillis() - 10000;
        long timeEnd = timeStart + 10000;

        int result = eventLogic.createEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date (timeEnd));
    }

    @Test(expected = HttpRequestException.class)
    public void test4createEventTimeStartAfterTimeEnd() throws Exception {
        String userName = createString(50);
        String eventName = createString(50);
        String description = "";
        int locationId = 1;
        long timeStart = System.currentTimeMillis() + 10000;
        long timeEnd = timeStart - 10000;

        int result = eventLogic.createEvent(userName, eventName, description, locationId,
                new Date(timeStart), new Date (timeEnd));
    }


    // ------------------------- GET ALL EVENTS ------------------------------

    @Test(expected = HttpRequestException.class)
    public void test1getAllEventsEmptyUsername() throws Exception {
        String userName  = createString(0);
        String searchword = createString(0);

        List<Event> result = eventLogic.getAllEvents(userName, searchword);
    }

    @Test (expected = HttpRequestException.class)
    public void test2getAllEventsUsernameIsToLong()throws Exception{
        String userName = createString(51);
        String searchword = createString(0);

        List<Event> result = eventLogic.getAllEvents(userName, searchword);
    }

    @Test(expected = HttpRequestException.class)
    public void test3getAllEventsSearchwordIsToLong() throws Exception {
        String userName = createString(50);
        String searchword = createString(51);

        List<Event> result = eventLogic.getAllEvents(userName, searchword);
    }

    @Test(expected = HttpRequestException.class)
    public void test4getAllEventsSearchwordIsNull() throws Exception {
        String userName = createString(50);
        String searchword = null;

        List<Event> result = eventLogic.getAllEvents(userName, searchword);
    }

    @Test
    public void test5getAllEventsOk() throws Exception {
        String userName  = createString(50);
        String searchword = createString(0);

        List<Event> result = eventLogic.getAllEvents(userName, searchword);
    }

    // ------------------------- INVITE FRIEND ------------------------------

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        //mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    @Test
    public void test1InviteMaxUsernameLength() throws Exception {
        String userName = createString(50);
        String toInviteUsername = createString(50);
        int eventId = 1;

        eventLogic.inviteFriend(userName, eventId, toInviteUsername);
    }

    @Test (expected = HttpRequestException.class)
    public void test2InviteInvalidUsername() throws Exception {
        String userName = createString(51);
        String toInviteUsername = createString(50);
        int eventId = 1;

        eventLogic.inviteFriend(userName, eventId, toInviteUsername);
    }

    @Test (expected = HttpRequestException.class)
    public void test3InviteEmptyUsername() throws Exception {
        String userName = createString(0);
        String toInviteUsername = createString(50);
        int eventId = 1;

        eventLogic.inviteFriend(userName, eventId, toInviteUsername);
    }

    @Test (expected = HttpRequestException.class)
    public void test4InviteInvalidToInviteUsername() throws Exception {
        String userName = createString(50);
        String toInviteUsername = createString(51);
        int eventId = 1;

        eventLogic.inviteFriend(userName, eventId, toInviteUsername);
    }

    @Test (expected = HttpRequestException.class)
    public void test5InviteEmptyToInviteUsername() throws Exception {
        String userName = createString(50);
        String toInviteUsername = createString(0);
        int eventId = 1;

        eventLogic.inviteFriend(userName, eventId, toInviteUsername);
    }

    class TestInvitePersonJson implements Serializable {
        int eventId = 1;
        String username;
        String toInviteUsername;

        TestInvitePersonJson(int lengthUser, int lengthToInvited){
            username = createString(lengthUser);
            toInviteUsername = createString(lengthToInvited);
        }
    }

    // ------------------------- SEND INVITATION ------------------------------

    @Test
    public void test1SendInvitation() throws Exception {

        String inventedPersonJson = getJsonFromObject(new TestInvitePersonJson(50, 50));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(inventedPersonJson))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test (expected = HttpRequestException.class)
    public void test2SendInvitationEmptyUsername() throws Exception {

        String inventedPersonJson = getJsonFromObject(new TestInvitePersonJson(0, 50));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(inventedPersonJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test (expected = HttpRequestException.class)
    public void test3SendInvitationInvalidUsername() throws Exception {

        String inventedPersonJson = getJsonFromObject(new TestInvitePersonJson(51, 50));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(inventedPersonJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test (expected = HttpRequestException.class)
    public void test4SendInvitationEmptyToInvitedUsername() throws Exception {

        String inventedPersonJson = getJsonFromObject(new TestInvitePersonJson(50, 0));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(inventedPersonJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test (expected = HttpRequestException.class)
    public void test5SendInvitationInvalidToInvitedUsername() throws Exception {

        String inventedPersonJson = getJsonFromObject(new TestInvitePersonJson(50, 51));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(inventedPersonJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }



}