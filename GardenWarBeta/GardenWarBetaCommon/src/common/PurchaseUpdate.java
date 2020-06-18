package common;

import java.io.Serializable;

public class PurchaseUpdate extends MessageObject {

    private String purchasedItem;
    private int newQuant;

    public PurchaseUpdate(String purchasedItem, int newQuant){
        this.purchasedItem = purchasedItem;
        this.newQuant = newQuant;
    }


    public String getPurchasedItem() {
        return purchasedItem;
    }


    public int getNewQuant() {
        return newQuant;
    }

}
