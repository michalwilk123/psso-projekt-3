package client.strategy;

import common.Item;

public abstract class AbstractStrategy {
    protected Item item;
    protected final double maxValue;
    protected final String ownerName;

    public AbstractStrategy(Item item, double maxValue, String ownerName) {
        if (item.getPrice() > maxValue){
            throw new RuntimeException(
                "Cannot set max value lower than actual price"
            );
        }

        this.item = item;
        this.maxValue = maxValue;
        this.ownerName = ownerName;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getIdentifier() {
        return item.getName();
    }
}
