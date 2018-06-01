package group.greenbyte.lunchplanner.user.database.notifications;

import java.util.Date;

public class NotificationOptionsDatabase {
    private int id;
    private int optionSelected;
    private String userName;
    private Date blockedToday;
    private Date start;
    private Date end;

    public NotificationOptions getNotificationOptions(){
        NotificationOptions notificationOptions = new NotificationOptions();
        notificationOptions.setOptionSelected(BlockOptions.fromInt(optionSelected));
        notificationOptions.setBlockedToday(blockedToday);
        notificationOptions.setStart(start);
        notificationOptions.setEnd(end);
        notificationOptions.setUsername(userName);

        return notificationOptions;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOptionSelected(int optionSelected) {
        this.optionSelected = optionSelected;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBlockedToday(Date blockedToday) {
        this.blockedToday = blockedToday;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
