package group.greenbyte.lunchplanner.user.database.notifications;

import group.greenbyte.lunchplanner.user.database.User;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
public class NotificationOptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userName")
    private User user;

    //Option 1: block all notifications permanently
    private boolean blockAll;

    //Option 2: block all notifications for today
    private Date block_until;
    private boolean blockedUntil;

    //Option 3: block all notifications for specific time interval
    private boolean blockedForWork;
    private Date start_working;
    private Date stop_working;

    //Option 4: block all event notifications
    private boolean eventsBlocked;

    //Option 5: block all team notifications
    private boolean teamsBlocked;

    //Option 6: block notifications from subscriptions
    private boolean subscriptionsBlocked;

    @Transient
    private String username;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isBlockAll() {
        return blockAll;
    }

    public void setBlockAll(boolean blockAll) {
        this.blockAll = blockAll;
    }

    public Date getBlock_until() {
        return block_until;
    }

    public void setBlock_until(Date block_until) {
        this.block_until = block_until;
    }

    public boolean isBlockedUntil() {
        return blockedUntil;
    }

    public void setBlockedUntil(boolean blockedUntil) {
        this.blockedUntil = blockedUntil;
    }

    public boolean isBlockedForWork() {
        return blockedForWork;
    }

    public void setBlockedForWork(boolean blockedForWork) {
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

    public boolean isEventsBlocked() {
        return eventsBlocked;
    }

    public void setEventsBlocked(boolean eventsBlocked) {
        this.eventsBlocked = eventsBlocked;
    }

    public boolean isTeamsBlocked() {
        return teamsBlocked;
    }

    public void setTeamsBlocked(boolean teamsBlocked) {
        this.teamsBlocked = teamsBlocked;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isSubscriptionsBlocked() {
        return subscriptionsBlocked;
    }

    public void setSubscriptionsBlocked(boolean subscriptionsBlocked) {
        this.subscriptionsBlocked = subscriptionsBlocked;
    }

    /*
    public boolean notificationsAllowed() {
        if (optionSelected == BlockOptions.NONE) {
            return true;
        }

        if (optionSelected == BlockOptions.PERMANENTLY) {
            return false;
        }

        if (optionSelected == BlockOptions.TODAY) {
            return isSameDay();
        }

        if (optionSelected == BlockOptions.INTERVAL) {

            Date date = new Date();
            if (date.after(start) && date.before(end)) {
                return true;
            }
            return false;
        }
        return true;
    }
    */

    /*
    private boolean isSameDay() {
        if (blockedToday == null) {
            return false;
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(blockedToday);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(new Date());

        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
    */




}
