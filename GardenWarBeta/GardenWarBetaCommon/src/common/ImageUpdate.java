package common;

import java.io.Serializable;

public class ImageUpdate extends MessageObject {
    private int playerID;
    private int x;
    private int y;
    private String itemName;
    private AnimationStrategy animationStrategy;

    public ImageUpdate(int playerID, int x, int y, String itemName, AnimationStrategy animationStrategy){
        this.playerID = playerID;
        this.x=x;
        this.y=y;
        this.itemName=itemName;
        this.animationStrategy = animationStrategy;
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

    public String getItemName() {
        return itemName;
    }

    public AnimationStrategy getAnimationStrategy() {
        return animationStrategy;
    }
}
