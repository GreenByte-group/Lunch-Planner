package group.greenbyte.lunchplanner.team;

import java.io.Serializable;

/**
 * This class stores all data that can be sent / received over the REST API.
 * This class is used to convert JSON into java objects an java objects into JSON
 */
public class TeamJson implements Serializable {

    public TeamJson() {

    }

    public TeamJson(int parent, String teamName, String description, boolean visible){
        this.parent = parent;
        this.teamName = teamName;
        this.description = description;
        this.visible = visible;

    }

    public TeamJson(int parent, String teamName, String description, boolean visible){
        this.parent = parent;
        this.teamName = teamName;
        this.description = description;
        this.visible = visible;
    }

    private Integer parent;
    private String teamName;
    private String description;
    private boolean visible;

    public Integer getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isVisible() {
        return visible;
    }
}
