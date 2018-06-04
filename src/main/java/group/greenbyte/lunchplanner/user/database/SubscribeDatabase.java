package group.greenbyte.lunchplanner.user.database;

public class SubscribeDatabase {

    private String userName;
    private String location;

    public Subscribe getSubscribe(){
        Subscribe subscribeLocation = new Subscribe();
        subscribeLocation.setLocation(this.location);
        subscribeLocation.setSubscriber(this.userName);

        return subscribeLocation;
    }

    //------------GETTER AND SETTER-----------------------
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
