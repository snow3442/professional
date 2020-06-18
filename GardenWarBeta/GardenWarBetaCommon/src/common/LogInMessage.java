package common;

import java.io.Serializable;

public class LogInMessage implements Serializable {

    private String username;

    public LogInMessage(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
