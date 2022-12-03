package client.strategy;

import common.Item;
import server.IAuctionServer;

public class EagerStrategy extends AbstractStrategy implements IStrategy {

    public EagerStrategy(Item item) {
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
