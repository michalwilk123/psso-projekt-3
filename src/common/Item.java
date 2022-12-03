package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable{
    private String name;
    private String description;
    public String getDescription() {
        return description;
    }

    private double price;
    private String currentBuyer;
    private List<String> subscribersNames;

    public List<String> getSubscribersNames() {
        return subscribersNames;
    }

    private boolean finished = false;

    public boolean isFinished() {
        return finished;
    }

    public void setItemSold() {
        this.finished = true;
    }

    public Item(String name, String description, double price, String seller) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.subscribersNames = new ArrayList<String>();
        this.subscribersNames.add(seller);
    }

    public Item(String name, String description, double price, List<String> subs) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.subscribersNames = subs;
    }
    
    @Override
    public String toString() {
        return "Item: " + name + " Price: " + price + 
        " Buyer: " + currentBuyer + " Seller: " + this.subscribersNames.get(0);
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrentBuyer() {
        return currentBuyer;
    }

    public void setCurrentBuyer(String currentBuyer) {
        this.currentBuyer = currentBuyer;
    }

    public void addSubscriber(String clientName) {
        this.subscribersNames.add(clientName);
    }

    public boolean equals(Item it){
        return this.name.equals(it.getName());
    }
}
