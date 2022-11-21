package server;

import java.rmi.RemoteException;
import java.util.SortedMap;

import common.Bidder;
import common.IAuctionListener;

public class AuctionServerInstance extends java.rmi.server.UnicastRemoteObject implements IAuctionServer {
    private static AuctionServerInstance ins = null;
    private SortedMap<Integer, Bidder> bidders;
    private SortedMap<Integer, Bidder> auctions;

    public AuctionServerInstance() throws java.rmi.RemoteException {
        super();
    }

    public static AuctionServerInstance getInstance() {
        if (ins == null) {
            try {
                ins = new AuctionServerInstance();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return ins;
    }

    /**
     * Puts a new item up for auction by the owner with name ownerName.
     * The itemName argument uniquely identifies the new item to be auctioned.
     * If an item by that name already is up for auction in the server, a
     * RemoteException is thrown.
     * A description of the item is given by the itemDesc argument. The starting
     * (minimum) bid is given by the startBid argument.
     * The item will be available for auction for the number of seconds given by the
     * auctionTime argument.
     */
    @Override
    public void placeItemForBid(String ownerName, String itemName, String itemDesc, double startBid, int auctionTime)
            throws RemoteException {
        // TODO Auto-generated method stub

    }

    @Override
    public void bidOnItem(String bidderName, String itemName, double bid) throws RemoteException {
        // TODO Auto-generated method stub

    }

    @Override
    public Item getItems() throws RemoteException {
        // TODO Auto-generated method stub
        System.out.println("CHCE PRZEDMIOTY AUKCYJNE");
        return null;
    }

    @Override
    public void registerListener(IAuctionListener al, String itemName) throws RemoteException {
        // TODO Auto-generated method stub

    }

}
