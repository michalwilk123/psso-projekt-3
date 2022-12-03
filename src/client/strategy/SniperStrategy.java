package client.strategy;

import common.Item;
import server.IAuctionServer;

public class SniperStrategy extends AbstractStrategy implements IStrategy {


    public SniperStrategy(Item item) {
        super(item);
        //TODO Auto-generated constructor stub
    }

    @Override
    public boolean isCallbackRunning(long currentTimeSeconds) {
        System.out.println("RUNNING SNIPER STRATEGY");
        return false;
    }

    @Override
    public void runAction(IAuctionServer server) {
        // TODO Auto-generated method stub
        
    }
}
