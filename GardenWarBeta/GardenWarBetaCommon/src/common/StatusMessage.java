package common;

import java.io.Serializable;

public class StatusMessage implements Serializable {
    /**
     * The ID number of the player who has connected or disconnected.
     */
    public final int playerID;

    /**
     * True if the player has just connected; false if the player has just
     * disconnected.
     */
    public final boolean connecting;

    /**
     * The list of players after the change has been made.
     */
    public final int[] players;

    public StatusMessage(int playerID, boolean connecting, int[] players) {
        this.playerID = playerID;
        this.connecting = connecting;
        this.players = players;
    }
}
