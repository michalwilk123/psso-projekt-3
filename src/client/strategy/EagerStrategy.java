package client.strategy;

import java.rmi.RemoteException;

import common.Item;
import server.IAuctionServer;

public class EagerStrategy extends AbstractStrategy implements IStrategy {

    public EagerStrategy(Item item, double maxValue, String ownerName) {
        super(item, maxValue, ownerName);
    }

    @Override
    public boolean isCallbackRunning() {
        // check if we are bidding
        if (ownerName.equals(item.getCurrentBuyer())){
            return false;
        }

        // check if we got above max value
        if (item.getPrice() >= maxValue){
            return false;
        }

        System.out.println("RUNNING EAGER STRATEGY");
        return true;
    }

    @Override
    public void runAction(IAuctionServer server) {
        try {
            server.bidOnItem(ownerName, item.getName(), item.getPrice() + 1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
