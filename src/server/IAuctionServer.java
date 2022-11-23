package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import common.IAuctionListener;
import common.Item;

public interface IAuctionServer extends Remote {
    public void placeItemForBid(String ownerName, String itemName, String itemDesc, double startBid, int auctionTime) throws RemoteException;
    public void bidOnItem(String bidderName, String itemName, double bid) throws RemoteException;
    public Item[] getItems() throws RemoteException;
    public Item getItem(String name) throws RemoteException;
    public void registerListener(IAuctionListener al, String itemName) throws RemoteException;
}
