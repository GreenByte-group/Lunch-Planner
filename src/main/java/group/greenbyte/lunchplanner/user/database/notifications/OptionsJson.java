package group.greenbyte.lunchplanner.user.database.notifications;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class OptionsJson implements Serializable {

    private Boolean blockAll;
    private Date block_until;
    private Boolean blockedForWork;
    private Date start_working;
    private Date stop_working;
    private Boolean eventsBlocked;
    private Boolean teamsBlocked;
    private Boolean subscriptionsBlocked;

    public OptionsJson() {};

    public OptionsJson(Boolean blockAll, Date block_until, Boolean blockedForWork,
                       Date start_working, Date stop_working, Boolean eventsBlocked, Boolean teamsBlocked,
                       Boolean subscriptionsBlocked) {

        this.blockAll = blockAll;
        this.block_until = block_until;
        this.blockedForWork = blockedForWork;
        this.start_working = start_working;
        this.stop_working = stop_working;
        this.eventsBlocked = eventsBlocked;
        this.teamsBlocked = teamsBlocked;
        this.subscriptionsBlocked = subscriptionsBlocked;

    }


    //------- GETTERS AND SETTERS -------

    public Boolean getBlockAll() {
        return blockAll;
    }

    public void setBlockAll(Boolean blockAll) {
        this.blockAll = blockAll;
    }

    public Date getBlock_until() {
        return block_until;
    }

    public void setBlock_until(Date block_until) {
        this.block_until = block_until;
    }

    public Boolean getBlockedForWork() {
        return blockedForWork;
    }

    public void setBlockedForWork(Boolean blockedForWork) {
        this.blockedForWork = blockedForWork;
    }

    public Date getStart_working() {
        return start_working;
    }

    public void setStart_working(Date start_working) {
        this.start_working = start_working;
    }

    public Date getStop_working() {
        return stop_working;
    }

    public void setStop_working(Date stop_working) {
        this.stop_working = stop_working;
    }

    public Boolean getEventsBlocked() {
        return eventsBlocked;
    }

    public void setEventsBlocked(Boolean eventsBlocked) {
        this.eventsBlocked = eventsBlocked;
    }

    public Boolean getTeamsBlocked() {
        return teamsBlocked;
    }

    public void setTeamsBlocked(Boolean teamsBlocked) {
        this.teamsBlocked = teamsBlocked;
    }

    public Boolean getSubscriptionsBlocked() {
        return subscriptionsBlocked;
    }

    public void setSubscriptionsBlocked(Boolean subscriptionsBlocked) {
        this.subscriptionsBlocked = subscriptionsBlocked;
    }

    public static Integer getMinutesFromDate(Date date) throws NumberFormatException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int minutes = 0;
        minutes += cal.get(Calendar.HOUR_OF_DAY) * 60;
        minutes += cal.get(Calendar.MINUTE);

        return minutes;
    }
}
