package group.greenbyte.lunchplanner.user.database;

public class SubscribeDatabase {

    private String user;
    private String location;

    public Subscribe getSubscribe(){
        Subscribe subscribeLocation = new Subscribe();
        subscribeLocation.setLocation(this.location);
        subscribeLocation.setSubscriber(this.user);

        return subscribeLocation;
    }

    //------------GETTER AND SETTER-----------------------
    public String getSubscriber() {
        return user;
    }

    public void setSubscriber(String userName) {
        this.user = userName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
