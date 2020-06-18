package common;

import java.io.Serializable;

final public class DisconnectMessage implements Serializable {
    /**
     * The message associated with the disconnect.  When the common.Hub
     * sends disconnects because it is shutting down, the message
     * is "*shutdown*".
     */
    final public String message;

    /**
     * Creates a common.DisconnectMessage containing a given String, which
     * is meant to describe the reason for the disconnection.
     */
    public DisconnectMessage(String message) {
        this.message = message;
    }
}
