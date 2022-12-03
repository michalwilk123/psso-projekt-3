package client.strategy;

import common.Item;

public abstract class AbstractStrategy {
    private Item item;

    public AbstractStrategy(Item item) {
        this.item = item;
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
