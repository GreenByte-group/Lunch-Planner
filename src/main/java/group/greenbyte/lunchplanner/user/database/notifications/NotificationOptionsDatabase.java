package group.greenbyte.lunchplanner.user.database.notifications;

import java.util.Date;

public class NotificationOptionsDatabase {
    private int id;
    private String userName;
    private boolean blockAll;
    private Date block_until;
    private boolean blockedUntil;
    private Date start_working;
    private Date stop_working;
    private boolean eventsBlocked;
    private boolean teamsBlocked;
    private boolean subscriptionsBlocked;

    public NotificationOptions getNotificationOptions(){
        NotificationOptions notificationOptions = new NotificationOptions();
        notificationOptions.setId(id);
        notificationOptions.setBlockAll(blockAll);
        notificationOptions.setBlockedUntil(blockedUntil);
        notificationOptions.setBlock_until(block_until);
        notificationOptions.setStart_working(start_working);
        notificationOptions.setStop_working(stop_working);
        notificationOptions.setEventsBlocked(eventsBlocked);
        notificationOptions.setTeamsBlocked(teamsBlocked);
        notificationOptions.setSubscriptionsBlocked(subscriptionsBlocked);
        notificationOptions.setUsername(userName);

        return notificationOptions;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBlockAll(boolean blockAll) {
        this.blockAll = blockAll;
    }

    public void setBlock_until(Date block_until) {
        this.block_until = block_until;
    }

    public void setBlockedUntil(boolean blockedUntil) {
        this.blockedUntil = blockedUntil;
    }

    public void setStart_working(Date start_working) {
        this.start_working = start_working;
    }

    public void setStop_working(Date stop_working) {
        this.stop_working = stop_working;
    }

    public void setEventsBlocked(boolean eventsBlocked) {
        this.eventsBlocked = eventsBlocked;
    }

    public void setTeamsBlocked(boolean teamsBlocked) {
        this.teamsBlocked = teamsBlocked;
    }

    public void setSubscriptionsBlocked(boolean subscriptionsBlocked) {
        this.subscriptionsBlocked = subscriptionsBlocked;
    }

    public void setId(int id) {
        this.id = id;
    }
}
