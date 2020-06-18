package server;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class TurnManager {
    private IntegerProperty smallTurn = new SimpleIntegerProperty(0);
    private IntegerProperty turn = new SimpleIntegerProperty(0);

    public int getSmallTurn() {
        return smallTurn.get();
    }

    public IntegerProperty smallTurnProperty() {
        return smallTurn;
    }

    public int getTurn() {
        return turn.get();
    }

    public IntegerProperty turnProperty() {
        return turn;
    }

    public void setSmallTurn(int smallTurn) {
        this.smallTurn.set(smallTurn);
    }

    public void setTurn(int turn) {
        this.turn.set(turn);
    }
}
