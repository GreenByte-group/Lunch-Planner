package group.greenbyte.lunchplanner.user;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static group.greenbyte.lunchplanner.Utils.createString;

public class Utils {

    public static String createUserIfNotExists(MockMvc mockMvc) throws Exception {
        String userName = createString(10);
        return createUserIfNotExists(mockMvc, userName);
    }

    public static String createUserIfNotExists(MockMvc mockMvc, String userName) throws Exception {
        String password = "1234";
        String email = "mail@mail.de";

        String user = "{username:" + userName + ", password:" + password + ", email:" + email +"}";

        mockMvc.perform(
                MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON_VALUE).content(user));

        return userName;
    }

    public static String createUserIfNotExists(UserLogic userLogic, String userName) throws Exception {
        try {
            userLogic.createUser(userName, "1234", "mail@mail.de");
        } catch (Exception ignored) {}
        return userName;
    }
}
