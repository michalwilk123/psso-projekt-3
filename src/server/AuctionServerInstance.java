package server;

import java.rmi.RemoteException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import common.Auction;
import common.IAuctionListener;
import common.Item;

public class AuctionServerInstance extends java.rmi.server.UnicastRemoteObject implements IAuctionServer {
    private static AuctionServerInstance ins = null;
    // private SortedMap<Integer, Bidder> bidders;
    // private SortedMap<Integer, Bidder> auctions;
    private SortedMap<Integer, Thread> threads;
    private SortedMap<Integer, Auction> runningAuctions;
    private List<Auction> finishedAuctions;

    public void finalizeAuction(Auction auction) {
        System.out.println(
                "AUCTION ENDED FOR ITEM: " + auction.getItem());

        Thread t = threads.remove(auction.getAuctionId());
        if (t != null) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        runningAuctions.remove(auction.getAuctionId());
        this.finishedAuctions.add(auction);
    }

    private void createAuction(int auctionId, Item item, int lifeOfAuction) {
        Auction auction = new Auction(auctionId, item);
        Auctioneer auctioneer = new Auctioneer(lifeOfAuction, () -> this.finalizeAuction(auction));
        Thread t = new Thread(auctioneer);
        t.start();

        threads.put(auctionId, t);
        runningAuctions.put(auctionId, auction);
    }

    private void initializeTestData() {
        // Creating 4 test auctions
        Item item1 = new Item("Kubek do kawy", 800);
        Item item2 = new Item("Samochód opel Vectra", 20);
        Item item3 = new Item("Łagodne wprowadzenie do algorytmów", 10000);
        Item item4 = new Item("Poprawka do AKO", 10);

        createAuction(1, item1, 100);
        createAuction(2, item2, 110);
        createAuction(3, item3, 120);
        createAuction(4, item4, 90);
    }

    public AuctionServerInstance() throws java.rmi.RemoteException {
        super();
        System.out.println("SERVER STARTED");
        System.out.println("Creating dummy data");
        this.runningAuctions = new TreeMap<Integer, Auction>();
        this.threads = new TreeMap<Integer, Thread>();
        this.initializeTestData();
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
    public Item[] getItems() throws RemoteException {
        Item[] items = this.runningAuctions
            .values()
            .stream()
            .map(a -> (Item) a.getItem())
            .toArray(size -> new Item[size]);

        return items;
    }

    @Override
    public void registerListener(IAuctionListener al, String itemName) throws RemoteException {
        // TODO Auto-generated method stub

    }

}
