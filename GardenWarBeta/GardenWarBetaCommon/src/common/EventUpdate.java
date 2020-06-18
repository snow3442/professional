package common;

import java.io.Serializable;

public class EventUpdate implements Serializable {
    //only need a message
    private String message;

    public EventUpdate(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
