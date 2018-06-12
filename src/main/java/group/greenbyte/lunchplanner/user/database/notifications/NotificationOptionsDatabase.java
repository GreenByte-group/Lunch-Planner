package group.greenbyte.lunchplanner.user.database.notifications;

import java.util.Date;

public class NotificationOptionsDatabase {
    private String userName;
    private int id;
    private boolean blockAll;
    private Date block_until;
    private boolean blockedForWork;
    private Integer start_working;
    private Integer stop_working;
    private boolean subscriptionsBlocked;
    private boolean teamsBlocked;
    private boolean eventsBlocked;


    public NotificationOptions getNotificationOptions(){
        NotificationOptions notificationOptions = new NotificationOptions();
        notificationOptions.setBlockAll(blockAll);
        notificationOptions.setBlock_until(block_until);
        notificationOptions.setBlockedForWork(blockedForWork);
        notificationOptions.setStart_working(start_working);
        notificationOptions.setStop_working(stop_working);
        notificationOptions.setTeamsBlocked(teamsBlocked);
        notificationOptions.setSubscriptionsBlocked(subscriptionsBlocked);
        notificationOptions.setUsername(userName);
        notificationOptions.setEventsBlocked(eventsBlocked);

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

    public void setStart_working(Integer start_working) {
        this.start_working = start_working;
    }

    public void setStop_working(Integer stop_working) {
        this.stop_working = stop_working;
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

    public void setEventsBlocked(boolean eventsBlocked) {
        this.eventsBlocked = eventsBlocked;
    }
}
