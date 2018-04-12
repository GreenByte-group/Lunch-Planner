package group.greenbyte.lunchplanner.event;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static group.greenbyte.lunchplanner.Utils.createString;
import static group.greenbyte.lunchplanner.Utils.getJsonFromObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = AppConfig.class)
@WebAppConfiguration
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
                .andExpect(MockMvcResultMatchers.status().isNotExtended());
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
}