package group.greenbyte.lunchplanner.user.database;

public class SubscribeDatabase {

    private String subscriber;
    private String location;

    public Subscribe getSubscribe(){
        Subscribe subscribeLocation = new Subscribe();
        subscribeLocation.setLocation(this.location);
        subscribeLocation.setSubscriber(this.subscriber);

        return subscribeLocation;
    }

    //------------GETTER AND SETTER-----------------------
    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String userName) {
        this.subscriber = userName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
