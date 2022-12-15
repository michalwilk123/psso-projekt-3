package client.strategy;

import common.Item;
import server.IAuctionServer;

public interface IStrategy {
    public boolean isCallbackRunning();
    public void runAction(IAuctionServer server);
    public String getIdentifier();
    public Item getItem();
    public void setItem(Item item);
}
