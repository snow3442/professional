package common;

import java.util.HashSet;

public class SelectionUpdate extends MessageObject {

    private int playerID;
    private HashSet<Integer[]> coords = new HashSet<>();

    public SelectionUpdate(int playerID, HashSet<Integer[]> coords){
        this.playerID = playerID;
        this.coords = coords;
    }

    public int getPlayerID() {
        return playerID;
    }

    public HashSet<Integer[]> getCoords() {
        return coords;
    }
}
