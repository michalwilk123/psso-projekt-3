package common;

import java.util.ArrayList;
import java.util.List;

public class Auction {
    public Auction(int auctionId, Item item) {
        this.auctionId = auctionId;
        this.item = item;
        this.subscribers = new ArrayList<IAuctionListener>();
    }

    private int auctionId;
    private Item item;
    private IAuctionListener buyer;
    private List<IAuctionListener> subscribers;

    public List<IAuctionListener> getSubscribers() {
        return subscribers;
    }

    private void notifySubscribers(){
        for (IAuctionListener listener : subscribers){
            listener.notifyAboutAuctions(this.item);
        }
    }

    public int getAuctionId() {
        return auctionId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void makeBid(float newPrice, IAuctionListener bidder){
        if (item.getPrice() >= newPrice){
            System.out.println("Cannot bid for lower price!");
        } 
        
        this.buyer = bidder;
        notifySubscribers();
    }

    public IAuctionListener getBuyer() {
        return buyer;
    }

    public void setBuyer(IAuctionListener buyer) {
        this.buyer = buyer;
    }
}
