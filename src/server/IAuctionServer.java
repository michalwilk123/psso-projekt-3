package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import common.Item;

public interface IAuctionServer extends Remote {
    public void placeItemForBid(String ownerName, String itemName, String itemDesc, double startBid, int auctionTime) throws RemoteException;
    public void bidOnItem(String bidderName, String itemName, double bid) throws RemoteException;
    public Item[] getItems() throws RemoteException;
    public Item getItem(String name) throws RemoteException;
    public Item registerListener(String al) throws RemoteException;
    public void observeItem(String al, String itemName) throws RemoteException;
}
