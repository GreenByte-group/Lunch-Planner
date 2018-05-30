package group.greenbyte.lunchplanner.event.database;

public class BringServiceDatabase {

    private int serviceId;
    private int eventId;
    private String food;
    private String userName;
    private String accepter;
    private String description;

    public BringService getBringService(){
        BringService bringService = new BringService();
        bringService.setServiceId(this.serviceId);
        bringService.setFood(this.food);
        bringService.setEventId(this.eventId);
        bringService.setCreatorName(this.userName);
        bringService.setAccepter(this.accepter);
        bringService.setDescription(this.description);

        return bringService;
    }


    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setAccepter(String accepter) {
        this.accepter = accepter;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
