package server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;

import common.Auction;
import common.Item;

public class AuctionServerInstance extends java.rmi.server.UnicastRemoteObject implements IAuctionServer {
    private static AuctionServerInstance ins = null;
    private SortedMap<String, Thread> threads;
    private SortedMap<String, Auction> runningAuctions;
    private Item tempItemContainer;
    private Map<String, Semaphore> polledAuctionConsumers;
    private List<Auction> finishedAuctions;
    // private List<IAuctionListener> listeners;

    private void notifyClient(String clientName, Item item) {
        var listener = polledAuctionConsumers.get(clientName);
        tempItemContainer = item;

        if (listener != null && listener.availablePermits() == 0) {
            listener.release();
        }
    }

    public void finalizeAuction(Auction auction) {
        System.out.println(
                "AUCTION ENDED FOR ITEM: " + auction.getItem());

        threads.remove(auction.getItem().getName());

        for (Map.Entry<String, Semaphore> listenerEntry : this.polledAuctionConsumers.entrySet()) {
            notifyClient(listenerEntry.getKey(), auction.getItem());
        }

        runningAuctions.remove(auction.getItem().getName());
        this.finishedAuctions.add(auction);
    }

    private void createAuction(Item item) {
        Auction auction = new Auction(item, (clientName) -> notifyClient(clientName, item));
        Auctioneer auctioneer = new Auctioneer(
                (int) (item.getEolTime() - System.currentTimeMillis() / 1000), () -> this.finalizeAuction(auction));
        Thread t = new Thread(auctioneer);
        t.start();

        threads.put(item.getName(), t);
        runningAuctions.put(item.getName(), auction);
    }

    public void initializeTestData() {
        var currTime = System.currentTimeMillis() / 1000;
        // Creating 4 test auctions
        Item item1 = new Item("Kubek do kawy", "opis", 800, "foo", currTime + 100);
        Item item2 = new Item("Samochód opel Vectra", "opis", 20, "foo", currTime + 110);
        Item item3 = new Item("Łagodne wprowadzenie do algorytmów", "opis", 10000, "foo", currTime + 120);
        Item item4 = new Item("Poprawka do AKO", "dasdsa", 10, "foo", currTime + 90);

        createAuction(item1);
        createAuction(item2);
        createAuction(item3);
        createAuction(item4);
    }

    public AuctionServerInstance() throws java.rmi.RemoteException {
        super();
        System.out.println("SERVER STARTED");
        System.out.println("Creating dummy data");
        this.runningAuctions = new TreeMap<String, Auction>();
        this.threads = new TreeMap<String, Thread>();
        this.finishedAuctions = new ArrayList<>();
        this.polledAuctionConsumers = new TreeMap<>();
        // this.initializeTestData();
    }

    public static synchronized AuctionServerInstance getInstance() {
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
        Item it = new Item(
                itemName, itemDesc, startBid, ownerName,
                System.currentTimeMillis() / 1000 + auctionTime);
        createAuction(it);
    }

    @Override
    public void bidOnItem(String bidderName, String itemName, double bid) throws RemoteException {
        var it = runningAuctions.get(itemName);

        if (it != null) {
            it.makeBid(bid, bidderName);
        }
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

    /*
     * this is a function just to update on regular non essential
     * operations, like when new item is registered
     * or auction has finished, does not get fired when item has new bidder
     */
    @Override
    public Item registerListener(String lisName) throws RemoteException {
        System.out.println("Connecting new user: " + lisName);
        Semaphore s = new Semaphore(1);

        try {
            s.acquire();
            this.polledAuctionConsumers.put(lisName, s);
            System.out.println("Blocking client " + lisName);
            s.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Sending notification to user: " + lisName);
        return tempItemContainer;
    }

    @Override
    public Item getItem(String name) throws RemoteException {
        Item item = this.runningAuctions.get(name).getItem();

        if (item == null) {
            throw new RemoteException("Cannot find item: " + name);
        }

        return item;
    }

    @Override
    public void observeItem(String clientName, String itemName) throws RemoteException {
        Item item = this.runningAuctions.get(itemName).getItem();

        if (!item.getSubscribersNames().contains(clientName)) {
            item.addSubscriber(clientName);
        } else {
            throw new RemoteException("Cannot observe already observed item");
        }
    }
}
