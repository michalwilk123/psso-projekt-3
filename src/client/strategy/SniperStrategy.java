package client.strategy;

import java.rmi.RemoteException;

import common.Item;
import server.IAuctionServer;

public class SniperStrategy extends AbstractStrategy {


    public SniperStrategy(Item item, double maxValue, String ownerName) {
        super(item, maxValue, ownerName);
    }

    @Override
    public boolean isCallbackRunning() {
        // check if we are bidding
        if (ownerName.equals(item.getCurrentBuyer())){
            return false;
        }

        // check if we got above max value
        if (item.getPrice() * 2 > maxValue){
            return false;
        }

        // check if time left is greater than one minute
        if (item.getEolTime() - System.currentTimeMillis() / 1000 > 60){
            return false;
        }

        System.out.println("RUNNING SNIPER STRATEGY");
        return true;
    }

    @Override
    public void runAction(IAuctionServer server) {
        // because we have implemented observer in server, we do not have
        // to an explit refresh
        try {
            server.bidOnItem(ownerName, item.getName(), item.getPrice() * 2);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
