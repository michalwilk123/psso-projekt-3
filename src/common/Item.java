package common;

import java.io.Serializable;

public class Item implements Serializable{
    private String name;
    private float price;
    private String currentBuyer;

    public Item(String name, float price) {
        this.name = name;
        this.price = price;
    }
    
    @Override
    public String toString() {
        return "Item: " + name + " Price: " + price + " Buyer " + currentBuyer;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCurrentBuyer() {
        return currentBuyer;
    }

    public void setCurrentBuyer(String currentBuyer) {
        this.currentBuyer = currentBuyer;
    }
}
