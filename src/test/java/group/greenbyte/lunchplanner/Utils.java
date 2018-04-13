package group.greenbyte.lunchplanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }

    static public String createString(int length) {
        StringBuilder temp = new StringBuilder();
        for(int i = 0; i < length; i++) {
            temp.append("a");
        }

        return temp.toString();
    }

    static public String getJsonFromObject(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

}
