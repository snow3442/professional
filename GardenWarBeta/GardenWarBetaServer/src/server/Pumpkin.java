package server;

import common.StatsUpdate;

public class Pumpkin extends Plant{

    public Pumpkin(TurnManager turnManager, Square square){
        super(turnManager, square);

    }

    @Override
    public void setData() {
        setPlantName("PUMPKIN");
        setTurnToMaturity(2);
        setProductionAmount(2);
        setState("SEED");
    }


    @Override
    void castAbility(){
        //update model
        getOwner().setActionPoints(getOwner().getActionPoints()+2);
        //broadcast
        StatsUpdate update = new StatsUpdate(getOwner().getPlayerID(),getOwner().getActionPoints(),"actionpoints");
        getMessageObjects().add(update);
    }
}
