package common;

import java.io.Serializable;

public class PurchaseRequest implements Serializable {
    private String itemName;
    public PurchaseRequest(String itemName){
        this.itemName = itemName;
    }
    public String getItemName() {
        return itemName;
    }

}
