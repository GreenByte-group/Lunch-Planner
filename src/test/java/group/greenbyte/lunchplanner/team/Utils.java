package group.greenbyte.lunchplanner.team;


public class Utils {

    public static int createTeamWithoutParent(TeamLogic teamLogic,
                                              String userName,
                                              String teamName,
                                              String description) throws Exception {
        return teamLogic.createTeamWithoutParent(userName, teamName, description, false);
    }

    public static void setTeamPublic(TeamDao teamDao, int teamId) throws Exception {
        teamDao.updateTeamIsPublic(teamId, true);
    }

}
