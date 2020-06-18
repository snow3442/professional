package server;

import common.*;

import java.util.ArrayList;

abstract public class Plant {
    private String State;
    private String plantName;
    private Player owner;
    private Square square;
    private int bornTurnNumber;
    private int turnToMaturity;
    private int productionAmount;
    private TurnManager turnManager;
    private ArrayList<MessageObject> messageObjects = new ArrayList<>();


    //create plant only on square click
    public Plant(TurnManager turnManager, Square square) {
        this.turnManager = turnManager;
        bornTurnNumber = turnManager.getTurn();
        this.square = square;
        //set the data and send the info to the client
        setData();

    }

    public abstract void setData();

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(
            Player owner) {
        this.owner = owner;
        if (owner != null) {
            owner.getPlants().add(this);
        }
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public int getBornTurnNumber() {
        return bornTurnNumber;
    }

    public void setBornTurnNumber(int bornTurnNumber) {
        this.bornTurnNumber = bornTurnNumber;
    }

    public int getTurnToMaturity() {
        return turnToMaturity;
    }

    public void setTurnToMaturity(int turnToMaturity) {
        this.turnToMaturity = turnToMaturity;
    }

    public int getProductionAmount() {
        return productionAmount;
    }

    public void setProductionAmount(int productionAmount) {
        this.productionAmount = productionAmount;
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }


    public ArrayList<MessageObject> getMessageObjects() {
        return messageObjects;
    }


    public void setMessageObjects(ArrayList<MessageObject> messageObjects) {
        this.messageObjects = messageObjects;
    }

    public void grow() {
        if (turnManager.getTurn()-bornTurnNumber==turnToMaturity) {
            setState("PLANT");
            ImageUpdate update = new ImageUpdate(getOwner().getPlayerID(), getSquare().getxCoor(), getSquare().getyCoor(), getPlantName(), AnimationStrategy.PLANTEVOLVE);
            getMessageObjects().add(update);
        }
        if(turnManager.getTurn()-bornTurnNumber<=turnToMaturity){
            sendData();
        }

    }

    public void collect() {
        //update model
        castAbility();
        getOwner().setFood(getOwner().getFood() + getProductionAmount());
        StatsUpdate update1 = new StatsUpdate(getOwner().getPlayerID(),
                getOwner().getFood(), "food");
        FoodUpdate update2 = new FoodUpdate(getOwner().getPlayerID(), getSquare().getxCoor(), getSquare().getyCoor(),
                getProductionAmount());
        ImageUpdate update3 = new ImageUpdate(getOwner().getPlayerID(), getSquare().getxCoor(), getSquare().getyCoor(),
                "USED", AnimationStrategy.COLLECT);
        getMessageObjects().add(update1);
        getMessageObjects().add(update2);
        getMessageObjects().add(update3);

    }

    /**
     * send the plant data as a plantInfoUpdate to client
     */
    public void sendData() {
        PlantInfoUpdate update = new PlantInfoUpdate(getOwner().getPlayerID(), getSquare().getxCoor(),
                getSquare().getyCoor(), turnToMaturity, getTurnManager().getTurn() - bornTurnNumber);
        getMessageObjects().add(update);
    }

    abstract void castAbility();

}
