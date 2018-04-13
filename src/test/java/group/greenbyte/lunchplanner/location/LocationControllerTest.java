package group.greenbyte.lunchplanner.location;

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
public class LocationControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        //mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    // ------------------ CREATE LOCATION ------------------------

    @Test
    public void test1CreateLocationWithNoDescription() throws Exception {
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;

        LocationJson location = new LocationJson(createString(1), xCoordinate, yCoordinate, "");

        String json = getJsonFromObject(location);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/location").contentType(MediaType.APPLICATION_JSON).content(json))
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
    public void test2CreateLocationWithNormalDescriptionAndMaxLocationName() throws Exception {
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;

        LocationJson location = new LocationJson(createString(50), xCoordinate, yCoordinate, "Super Location");

        String json = getJsonFromObject(location);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/location").contentType(MediaType.APPLICATION_JSON).content(json))
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
    public void test3CreateLocationWithMaxDescriptionAndMaxLocationName() throws Exception {
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;

        LocationJson location = new LocationJson(createString(50), xCoordinate, yCoordinate, createString(1000));

        String json = getJsonFromObject(location);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/location").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();

        String response = result .getResponse().getContentAsString();

        try {
            Integer.valueOf(response);
        } catch(NumberFormatException e){
            Asser.fail("Result is not a number");
        }

    }

    @Test
    public void test4CreateLocationWithNoLocationName() throws Exception {
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;

        LocationJson location = new LocationJson("", xCoordinate, yCoordinate, createString(1000));

        String json = getJsonFromObject(location);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/location").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isNotExtended());
    }

    @Test
    public void test5CreateLocationNameTooLong() throws Exception {
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;

        LocationJson location = new LocationJson(createString(51), xCoordinate, yCoordinate, createString(1000));



        String json = getJsonFromObject(location);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/location").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void test6CreateLocationDescriptionTooLong() throws Exception {
        double xCoordinate = 1.0;
        double yCoordinate = 1.0;

        LocationJson location = new LocationJson(createString(50), xCoordinate, yCoordinate, createString(1001));

        String json = getJsonFromObject(location);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/location").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }