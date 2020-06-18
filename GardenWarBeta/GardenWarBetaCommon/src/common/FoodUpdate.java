package common;

public class FoodUpdate extends MessageObject {
    private int playerID;
    private int x;
    private int y;
    private int increment;

    public FoodUpdate(int playerID, int x, int y, int increment) {
        this.playerID = playerID;
        this.x = x;
        this.y = y;
        this.increment = increment;
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

    public int getIncrement() {
        return increment;
    }
}
