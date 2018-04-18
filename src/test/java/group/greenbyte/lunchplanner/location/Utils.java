package group.greenbyte.lunchplanner.location;

import group.greenbyte.lunchplanner.exceptions.HttpRequestException;

public class Utils {

    public static int createLocation(LocationLogic locationLogic, String userName, String name, String description) throws Exception {
        return locationLogic.createLocation(userName, name, 1.0, 1.0, description);
    }

}
