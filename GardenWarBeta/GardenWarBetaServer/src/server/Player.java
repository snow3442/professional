package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Player {

    private int playerID;
    private String username;
    private int food;
    private int actionPoints;
    private int actionIncremenent;
    private int consumption;
    private HashMap<String, Integer> itemsMap = new HashMap<>();
    private ArrayList<Plant> plants = new ArrayList<>();
    private Stack<String> usedItem = new Stack<>();


    public Player(int playerID, String username){
        this.playerID = playerID;
        this.username = username;
        this.food = 10;
        this.actionPoints = 0;
        this.consumption = 2;
        this.actionIncremenent=5;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public int getActionPoints() {
        return actionPoints;
    }

    public void setActionPoints(int actionPoints) {
        this.actionPoints = actionPoints;
    }

    public int getConsumption() {
        return consumption;
    }

    public void setConsumption(int consumption) {
        this.consumption = consumption;
    }

    public HashMap<String, Integer> getItemsMap() {
        return itemsMap;
    }

    public void setItemsMap(HashMap<String, Integer> itemsMap) {
        this.itemsMap = itemsMap;
    }

    public Stack<String> getUsedItem() {
        return usedItem;
    }

    public void setUsedItem(Stack<String> usedItem) {
        this.usedItem = usedItem;
    }

    public ArrayList<Plant> getPlants() {
        return plants;
    }

    public void setPlants(ArrayList<Plant> plants) {
        this.plants = plants;
    }

    public int getActionIncremenent() {
        return actionIncremenent;
    }

    public void setActionIncremenent(int actionIncremenent) {
        this.actionIncremenent = actionIncremenent;
    }

    /**
     * add the item to player's itemMap
     * @param item {String} name of the item to be added
     */
    public void addItemToPlayer(String item) {
        if (getItemsMap().containsKey(item)) {
            getItemsMap().put(item, getItemsMap().get(item) + 1);
        } else {
           getItemsMap().put(item, 1);
        }
    }

    /**
     * removes the item from player's itemMap
     * so the player no longer has the item
     * @param item {String} name of the item to be removed
     */
    public void removeItemFromPlayer(String item) {
        getItemsMap().put(item, getItemsMap().get(item) - 1);

    }
}
