package group.greenbyte.lunchplanner.event.database;

public class BringServiceDatabase {

    private int serviceId;
    private int event_Id;
    private String food;
    private String creater;
    private String accepter;
    private String description;

    public BringService getBringService(){
        BringService bringService = new BringService();
        bringService.setServiceId(this.serviceId);
        bringService.setFood(this.food);
        bringService.setEvent_ID(this.event_Id);
        bringService.setCreaterName(this.creater);
        bringService.setAccepter(this.accepter);
        bringService.setDescription(this.description);

        return bringService;
    }


    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setEvent_Id(int event_Id) {
        this.event_Id = event_Id;
    }

    public void setCreater(String creater) {
        this.creater = creater;
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
}
