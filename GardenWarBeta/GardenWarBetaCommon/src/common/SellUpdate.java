package common;

import java.io.Serializable;

public class SellUpdate implements Serializable {

    private String itemName;
    private int quantity;

    public SellUpdate(String itemName, int quantity){
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }
}
