package common;

import java.io.Serializable;

public class SquareClickRequest implements Serializable {
    private int x;
    private int y;

    public SquareClickRequest(int x, int y){
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
