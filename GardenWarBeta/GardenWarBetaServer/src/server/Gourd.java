package server;

import common.ImageUpdate;
import common.StatsUpdate;

public class Gourd extends Plant {

    public Gourd(TurnManager turnManager, Square square){
        super(turnManager, square);
    }

    @Override
    public void setData() {
        setPlantName("GOURD");
        setTurnToMaturity(2);
        setProductionAmount(3);
        setState("SEED");
    }


    @Override
    void castAbility(){

    }
}
