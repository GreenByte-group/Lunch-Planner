package group.greenbyte.lunchplanner.team;

import group.greenbyte.lunchplanner.location.LocationLogic;

public class Utils {

    public static int createTeamWithoutParent(TeamLogic teamLogic,
                                              String userName,
                                              String teamName,
                                              String description) throws Exception {
        return teamLogic.createTeamWithoutParent(userName, teamName, description);
    }

}
