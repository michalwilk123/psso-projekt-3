package client;

import java.rmi.RemoteException;
import java.util.function.Consumer;

import common.IAuctionListener;
import common.Item;
import server.IAuctionServer;

public class LongPollService implements Runnable {
    private Consumer<Item> callback;
    private IAuctionServer serverInstance;
    private IAuctionListener listener;
    private Runnable onConnectionClose;

    public LongPollService(IAuctionServer serverInstance, IAuctionListener listener, Consumer<Item> callback, Runnable onConnectionClose) {
        this.callback = callback;
        this.serverInstance = serverInstance;
        this.listener = listener;
        this.onConnectionClose = onConnectionClose;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Item it = null;
            try {
                it = serverInstance.registerListener(listener.getListenerName());
            } catch (RemoteException e) {
                e.printStackTrace();
                System.out.println("Connection ended");
                onConnectionClose.run();
                return;
            }
            System.out.println("Answer got, running callback");
            if (it != null) {
                callback.accept(it);
            }
        }
    }
}
