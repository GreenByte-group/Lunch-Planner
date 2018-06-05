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
    @JoinColumn(name = "userName", unique = true)
    private User user;

    //Option 1: block all notifications permanently
    private boolean blockAll = false;

    //Option 2: block all notifications for today
    private Date block_until;

    //Option 3: block all notifications for specific time interval
    private boolean blockedForWork = false;
    private Integer start_working;
    private Integer stop_working;

    //Option 4: block all event notifications
    private boolean eventsBlocked = false;

    //Option 5: block all team notifications
    private boolean teamsBlocked = false;

    //Option 6: block notifications from subscriptions
    private boolean subscriptionsBlocked = false;

    @Transient
    private String username;

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

    public boolean isBlockedForWork() {
        return blockedForWork;
    }

    public void setBlockedForWork(boolean blockedForWork) {
        this.blockedForWork = blockedForWork;
    }

    public Integer getStart_working() {
        return start_working;
    }

    public void setStart_working(Integer start_working) {
        this.start_working = start_working;
    }

    public Integer getStop_working() {
        return stop_working;
    }

    public void setStop_working(Integer stop_working) {
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


    public boolean notificationsAllowed() {
        if (blockAll) {
            return false;
        }

        Date now = new Date();
        if(block_until == null || now.after(block_until)) {
            return false;
        }

        if (blockedForWork) {
            if(start_working != null && stop_working != null) {
                Calendar calendar = Calendar.getInstance();
                int minutes = calendar.get(Calendar.HOUR) * 60 + calendar.get(Calendar.MINUTE);
                if(start_working < minutes || stop_working > minutes)
                    return false;
            } else {
                return false;
            }
        }
        return true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
