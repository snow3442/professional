package common;

import java.io.Serializable;

public class SellRequest implements Serializable {

    private String itemName;

    public SellRequest(String itemName){
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

}
