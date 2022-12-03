package common;

import java.rmi.RemoteException;
import java.util.function.Consumer;

public class Auction {
    public Auction(Item item, Consumer<String> notifyCallback) {
        this.item = item;
        this.callback = notifyCallback;
    }

    private Item item;
    // private List<String> subscribersNames;
    private Consumer<String> callback;

    private void notifySubscribers(){
        for (var listener : item.getSubscribersNames()){
            callback.accept(listener);
        }
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void makeBid(double newPrice, String bidder) throws RemoteException{
        if (item.getSubscribersNames().get(0).equals(bidder)){
            throw new RemoteException("Cannot bid for item you are selling!");
        } 

        if (item.getPrice() >= newPrice){
            throw new RemoteException("Cannot bid for lower price!");
        } 

        if (!item.getSubscribersNames().contains(bidder)){
            item.addSubscriber(bidder);
        }
        
        item.setCurrentBuyer(bidder);
        item.setPrice(newPrice);
        notifySubscribers();
    }
}
