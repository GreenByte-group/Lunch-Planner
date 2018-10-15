package group.greenbyte.lunchplanner.user;

import java.io.Serializable;


public class SubscribeJson implements Serializable{

    private static final long serialVersionUID = 465186153151351686L;

    private String username;
    private String location;
    private String locationName;

    public SubscribeJson(){ }

    public SubscribeJson(String username, String location, String locationName){
        this.username=username;
        this.location=location;
        this.locationName=locationName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
