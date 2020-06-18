package server;

import common.*;


public class Patato extends Plant {

    public Patato(TurnManager turnManager, Square square){
        super(turnManager, square);

    }

    @Override
    public void setData() {
        setPlantName("PATATO");
        setTurnToMaturity(1);
        setProductionAmount(1);
        setState("SEED");
    }


    @Override
    public void castAbility(){
        getOwner().addItemToPlayer("PATATO SEED");
        //broadcast
        //update image
        PurchaseUpdate update = new PurchaseUpdate("PATATO SEED", getOwner().getItemsMap().get("PATATO SEED"));
        getMessageObjects().add(update);

    }
}
