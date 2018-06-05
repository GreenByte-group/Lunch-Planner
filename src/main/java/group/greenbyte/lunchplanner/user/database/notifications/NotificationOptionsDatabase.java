package group.greenbyte.lunchplanner.user.database.notifications;

import java.util.Date;

public class NotificationOptionsDatabase {
    private String userName;
    private int id;
    private boolean blockAll;
    private Date block_until;
    private boolean blockedForWork;
    private boolean blockedUntil;
    private boolean eventsBlocked;
    private Date start_working;
    private Date stop_working;
    private boolean subscriptionsBlocked;
    private boolean teamsBlocked;


    public NotificationOptions getNotificationOptions(){
        NotificationOptions notificationOptions = new NotificationOptions();
        notificationOptions.setBlockAll(blockAll);
        notificationOptions.setBlock_until(block_until);
        notificationOptions.setBlockedUntil(blockedUntil);
        notificationOptions.setBlockedForWork(blockedForWork);
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

    public void setBlockedForWork(boolean blockedForWork) {
        this.blockedForWork = blockedForWork;
    }
}
