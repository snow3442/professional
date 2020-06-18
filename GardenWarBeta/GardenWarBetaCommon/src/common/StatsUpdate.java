package common;

import java.io.Serializable;

//updates temperature and  weather and player stats

public class StatsUpdate extends MessageObject {
    private int id;
    private int newQuant;
    private String statItem;

    public StatsUpdate(int id, int newQuant,String StatItem){
        this.id = id;
        this.newQuant = newQuant;
        this.statItem = StatItem;
    }

    public int getNewQuant() {
        return newQuant;
    }

    public String getStatItem() {
        return statItem;
    }

    public int getId() {
        return id;
    }
}
