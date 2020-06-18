package common;

import java.io.Serializable;

public class TurnChangeUpdate implements Serializable {
    private int currentID;
    public TurnChangeUpdate(int currentID){
        this.currentID = currentID;
    }

    public int getCurrentID() {
        return currentID;
    }
}
