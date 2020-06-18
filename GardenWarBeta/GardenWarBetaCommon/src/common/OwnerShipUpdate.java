package common;

import java.io.Serializable;

public class OwnerShipUpdate extends MessageObject{
    private int x;
    private int y;
    private int playerID;

    public OwnerShipUpdate(int playerID, int x, int y){
        this.x = x;
        this.y = y;
        this.playerID = playerID;
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
}
