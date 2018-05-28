package group.greenbyte.lunchplanner.event;

import java.io.Serializable;

public class BringServiceJson implements Serializable {

    private static final long serialVersionUID = 265186153151351686L;

    public BringServiceJson() {

    }

    public BringServiceJson(String food, String description){
        this.description = description;
        this.food = food;
    }

    private String food;
    private String description;

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
