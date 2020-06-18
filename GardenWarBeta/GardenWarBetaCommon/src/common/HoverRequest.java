package common;

import java.io.Serializable;

public class HoverRequest implements Serializable {
    private int x;
    private int y;
    public HoverRequest(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
