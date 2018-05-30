package group.greenbyte.lunchplanner.user.database;

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
    private boolean blockedPermanently;

    //Option 2: block all notifications for today
    private boolean option2BlockedToday;
    private Date blockedToday;

    //Option 3: block all notifications in a specific time interval
    private boolean option3TimeInterval;
    private Date start;
    private Date end;


    @Transient
    private String username;

    public boolean notificationsAllowed() {
        if (blockedPermanently) {
            return false;
        }

        if (option2BlockedToday) {
            return isSameDay();
        }

        if (option3TimeInterval) {
            Date date = new Date();
            if (date.after(start) && date.before(end)) {
                return true;
            }
            return false;
        }
        return true;
    }

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

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public void setBlockedPermanently(boolean blocked) {
        this.blockedPermanently = blocked;
    }

    public boolean isBlockedPermanently() {
        return this.blockedPermanently;
    }

    public boolean isOption2BlockedToday() {
        return option2BlockedToday;
    }

    public void setOption2(boolean option2BlockedToday) {
        this.option2BlockedToday = option2BlockedToday;
    }

    public void setBlockedToday(Date blockedToday) {
        this.blockedToday = blockedToday;
    }

    public Date getBlockedToday() {
        return this.blockedToday;
    }

    public void setOption3TimeIntervall(boolean option3TimeIntervall) {
        this.option3TimeInterval = option3TimeIntervall;
    }

    public boolean isOption3TimeIntervall() {
        return this.option3TimeInterval;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getStart() {
        return this.start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getEnd() {
        return this.end;
    }
}
