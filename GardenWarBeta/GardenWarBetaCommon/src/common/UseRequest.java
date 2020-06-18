package common;

import java.io.Serializable;

public class UseRequest implements Serializable {

    private String itemName;

    public UseRequest(String itemName){
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


}
