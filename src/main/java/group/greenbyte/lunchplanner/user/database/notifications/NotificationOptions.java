package group.greenbyte.lunchplanner.user.database.notifications;

import group.greenbyte.lunchplanner.user.database.User;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
public class NotificationOptions {

    @Id
    @OneToOne
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userName")
    private User user;

    private BlockOptions optionSelected;
    //Option 2: block all notifications for today
    private Date blockedToday;
    //Option 3: block all notifications for specific time interval
    private Date start;
    private Date end;

    @Transient
    private String username;

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

    public void setOptionSelected(BlockOptions optionSelected) {
        this.optionSelected = optionSelected;
    }

    public BlockOptions getOptionSelected() {
        return optionSelected;
    }

    public void setBlockedToday(Date blockedToday) {
        this.blockedToday = blockedToday;
    }

    public Date getBlockedToday() {
        return blockedToday;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
