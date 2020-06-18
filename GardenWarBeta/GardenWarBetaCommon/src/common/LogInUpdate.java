package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

//updates all players
public class LogInUpdate implements Serializable {
    private int id;
    private ArrayList<String> userNames;
    public LogInUpdate(int id, ArrayList<String> userNames){
        this.id = id;
        this.userNames=userNames;
    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getUserNames() {
        return userNames;
    }
}
