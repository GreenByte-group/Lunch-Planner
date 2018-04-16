package group.greenbyte.lunchplanner.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import group.greenbyte.lunchplanner.AppConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import java.util.Date;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.Utils.getJsonFromObject;
import static group.greenbyte.lunchplanner.event.Utils.createEvent;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("application-test.properties")
public class EventControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        //mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    // ------------------ CREATE EVENT ------------------------

    @Test
    public void test1CreateEventNoDescription() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson(createString(50), "", 1, timeStart, timeStart + 1000);

        String json = getJsonFromObject(event);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();

        String response = result.getResponse().getContentAsString();

        try {
            Integer.valueOf(response);
        } catch(NumberFormatException e) {
            Assert.fail("Result is not a number");
        }
    }

    @Test
    public void test2CreateEventNormalDescription() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson(createString(50), "Super Event", 1, timeStart, timeStart + 1000);

        String json = getJsonFromObject(event);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
                .andReturn();

        String response = result.getResponse().getContentAsString();

        try {
            Integer.valueOf(response);
        } catch(NumberFormatException e) {
            Assert.fail("Result is not a number");
        }
    }

    @Test
    public void test3CreateEventLongDescription() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson(createString(50), createString(1000), 1, timeStart, timeStart + 1000);

        String json = getJsonFromObject(event);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
                .andReturn();

        String response = result.getResponse().getContentAsString();

        try {
            Integer.valueOf(response);
        } catch(NumberFormatException e) {
            Assert.fail("Result is not a number");
        }
    }

    @Test
    public void test4CreateEventNoName() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson("", "", 1, timeStart, timeStart + 1000);

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test5CreateEventNameTooLong() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson(createString(51), "", 1, timeStart, timeStart + 1000);

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test6CreateEventDescriptionTooLong() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson("", "", 1, timeStart, timeStart + 1000);

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test7CreateEventTimeStartTooLow() throws Exception {
        long timeStart = System.currentTimeMillis() - 100000;
        long timeEnd = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson("", "", 1, timeStart, timeEnd);

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test8CreateEventTimeStartAfterTimeEnd() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;
        long timeEnd = System.currentTimeMillis() + 1000;

        EventJson event = new EventJson("", "", 1, timeStart, timeEnd);

        String json = getJsonFromObject(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    // ------------------ GET ALL ------------------------

    @Test
    public void test1SearchEventsForUserSearchwordIsNull() throws Exception {
        String searchword = null;
        String json = getJsonFromObject(searchword);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test2SearchEventsForUserSearchwordToBig() throws Exception {
        String searchword = createString(51);
        String json = getJsonFromObject(searchword);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // ------------------ UPDATE EVENT NAME ------------------------

    @Test
    public void test1updateEventName() throws Exception{
        int eventId = createEvent(mockMvc);

        String eventName = createString(50);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/name").contentType(MediaType.TEXT_PLAIN_VALUE).content(eventName))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        //TODO check if event is updated
    }

    @Test
    public void test2updateEventNameTooLong() throws Exception{
        int eventId = createEvent(mockMvc);

        String eventName = createString(51);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/name").contentType(MediaType.TEXT_PLAIN_VALUE).content(eventName))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test3updateEventNameEmpty() throws Exception{
        int eventId = createEvent(mockMvc);

        String eventName = "";

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/name").contentType(MediaType.TEXT_PLAIN_VALUE).content(eventName))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // ------------------- UPDATE EVENT DESCRIPTION -----------------

    @Test
    public void test1updateEventDescription() throws Exception{
        int eventId = createEvent(mockMvc);

        String eventDescription = createString(50);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/description").contentType(MediaType.TEXT_PLAIN_VALUE).content(eventDescription))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        //TODO check if event is updated
    }

    @Test
    public void test2updateEventDescriptionTooLong() throws Exception{
        int eventId = createEvent(mockMvc);

        String eventDescription = createString(51);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/description").contentType(MediaType.TEXT_PLAIN_VALUE).content(eventDescription))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test3updateEventDescriptionEmpty() throws Exception{
        int eventId = createEvent(mockMvc);

        String eventDescription = "";

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/description").contentType(MediaType.TEXT_PLAIN_VALUE).content(eventDescription))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // ------------------ UPDATE EVENT LOCATION ------------------

    @Test
    public void test1updateEventLocation() throws Exception{
        int eventId = createEvent(mockMvc);

        int location = 1;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/location").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(location)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        //TODO check if event is updated
    }

    // ------------------ UPDATE EVENT TIMESTART -------------------------
    @Test
    public void test1updateEventStartTime() throws Exception{
        int eventId = createEvent(mockMvc);

        long startTime = System.currentTimeMillis() + 10000;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/timestart").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(startTime)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        //TODO check if event is updated
    }

    @Test
    public void test2updateEventStartTimeTooEarly() throws Exception{
        int eventId = createEvent(mockMvc);

        long startTime = System.currentTimeMillis() - 10000;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/timestart").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(startTime)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test3updateEventStartTimeTooLate() throws Exception{
        int eventId = createEvent(mockMvc);

        long startTime = System.currentTimeMillis() + 1000000000L;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/timestart").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(startTime)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // ------------------ UPDATE EVENT TIMEEND -------------------------
    @Test
    public void test1updateEventEndTime() throws Exception{
        int eventId = createEvent(mockMvc);

        long endTime = System.currentTimeMillis() + 1000000;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/timeend").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(endTime)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        //TODO check if event is updated
    }

    @Test
    public void test2updateEventEndTimeTooEarly() throws Exception{
        int eventId = createEvent(mockMvc);

        long endTime = System.currentTimeMillis() - 10000;

        mockMvc.perform(
                MockMvcRequestBuilders.put("/event/" + eventId + "/timeend").contentType(MediaType.TEXT_PLAIN_VALUE).content(String.valueOf(endTime)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}