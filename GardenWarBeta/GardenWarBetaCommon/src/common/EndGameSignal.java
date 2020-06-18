package common;

import java.io.Serializable;

/**
 * send to client when the game ends
 */
public class EndGameSignal implements Serializable {
    private String message;

    public EndGameSignal(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
