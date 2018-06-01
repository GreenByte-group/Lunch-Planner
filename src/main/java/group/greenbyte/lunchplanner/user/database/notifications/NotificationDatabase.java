package group.greenbyte.lunchplanner.user.database.notifications;

import java.util.Date;

public class NotificationDatabase {

    private int notificationId;
    private Date date;
    private String titel;
    private String message;
    private String receiver;
    private String builder;
    private String link;
    private String picture;

    public Notifications getNotification(){
        Notifications notification = new Notifications();
        notification.setTitel(this.titel);
        notification.setBuilder(this.builder);
        notification.setLink(this.link);
        notification.setMessage(this.message);
        notification.setPicture(this.picture);
        notification.setReceiverName(this.receiver);
        notification.setNotificationId(this.notificationId);
        notification.setDate(this.date);

        return notification;
    }


    //gettER AN SETTER---------------------------------------------------------


    public void setMessage(String message) {
        this.message = message;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setBuilder(String builder) {
        this.builder = builder;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
