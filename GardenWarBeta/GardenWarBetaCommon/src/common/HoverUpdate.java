package common;

import java.io.Serializable;

public class HoverUpdate implements Serializable {
    private int x;
    private int y;
    private int playerID;
    private String description;

    public HoverUpdate(int playerID, int x, int y, String description){
        this.x = x;
        this.y = y;
        this.playerID =playerID;
        this.description = description;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getDescription() {
        return description;
    }
}
