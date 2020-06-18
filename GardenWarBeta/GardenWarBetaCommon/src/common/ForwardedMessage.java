package common;

import java.io.Serializable;

public class ForwardedMessage implements Serializable {
    public final Object message;  // Original message from a client.
    public final int senderID;    // The ID of the client who sent that message.

    /**
     * Create a ForwadedMessage to wrap a message sent by a client.
     *
     * @param senderID the ID number of the original sender.
     * @param message  the original message.
     */
    public ForwardedMessage(int senderID, Object message) {
        this.senderID = senderID;
        this.message = message;
    }
}
