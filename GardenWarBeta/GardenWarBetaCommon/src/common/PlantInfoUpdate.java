package common;

public class PlantInfoUpdate extends MessageObject {

    private int playerID;
    private int x;
    private int y;
    private int totalTurn;
    private int turnsAchieved;

    public PlantInfoUpdate(int playerID, int x, int y, int totalTurn, int turnsAchieved) {
        this.playerID = playerID;
        this.x = x;
        this.y = y;
        this.totalTurn = totalTurn;
        this.turnsAchieved = turnsAchieved;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTotalTurn() {
        return totalTurn;
    }

    public int getTurnsAchieved() {
        return turnsAchieved;
    }
}
