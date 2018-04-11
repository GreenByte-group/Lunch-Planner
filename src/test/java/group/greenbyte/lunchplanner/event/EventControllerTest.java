package group.greenbyte.lunchplanner.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
public class EventControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private EventController eventController;

    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(eventController)
                .build();
    }

    @Test
    public void test1CreateEventNoDescription() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson(createString(50), "", 1, timeStart, timeStart + 1000);

        String json = mapper.writeValueAsString(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
        //TODO add result matching to be a number
    }

    @Test
    public void test2CreateEventNormalDescription() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson(createString(50), "Super Event", 1, timeStart, timeStart + 1000);

        String json = mapper.writeValueAsString(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
        //TODO add result matching to be a number
    }

    @Test
    public void test3CreateEventLongDescription() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson(createString(50), createString(1000), 1, timeStart, timeStart + 1000);

        String json = mapper.writeValueAsString(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
        //TODO add result matching to be a number
    }

    @Test
    public void test4CreateEventNoName() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson("", "", 1, timeStart, timeStart + 1000);

        String json = mapper.writeValueAsString(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isNotExtended())
                .andExpect(MockMvcResultMatchers.content().string("name is missing"));
    }

    @Test
    public void test5CreateEventNameTooLong() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson(createString(51), "", 1, timeStart, timeStart + 1000);

        String json = mapper.writeValueAsString(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("name is too long"));
    }

    @Test
    public void test6CreateEventDescriptionTooLong() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson("", "", 1, timeStart, timeStart + 1000);

        String json = mapper.writeValueAsString(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("description is too long"));
    }

    @Test
    public void test7CreateEventTimeStartTooLow() throws Exception {
        long timeStart = System.currentTimeMillis() - 100000;
        long timeEnd = System.currentTimeMillis() + 100000;

        EventJson event = new EventJson("", "", 1, timeStart, timeEnd);

        String json = mapper.writeValueAsString(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("start time must be in the future"));
    }

    @Test
    public void test8CreateEventTimeStartAfterTimeEnd() throws Exception {
        long timeStart = System.currentTimeMillis() + 100000;
        long timeEnd = System.currentTimeMillis() + 1000;

        EventJson event = new EventJson("", "", 1, timeStart, timeEnd);

        String json = mapper.writeValueAsString(event);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/event").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("start time must be before end time"));
    }

    private String createString(int length) {
        String temp = "";
        for(int i = 0; i < length; i++) {
            temp += "a";
        }

        return temp;
    }

}