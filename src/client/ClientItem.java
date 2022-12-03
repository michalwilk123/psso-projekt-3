package client;

import java.util.List;

import common.Item;

public class ClientItem extends Item {
    private final ItemStatus status;

    public ItemStatus getStatus() {
        return status;
    }

    public ClientItem(String name, String desc, double price, String clientName,
            String buyer, List<String> subs) {
        super(name, desc, price, subs);
        this.setCurrentBuyer(buyer);

        System.out.println("CURRENTLY WATCHING: " + this.getSubscribersNames().size());

        if (this.isFinished()) {
            if (clientName.equals(buyer)) {
                status = ItemStatus.BOUGHT;
            } else {
                status = ItemStatus.ENDED;
            }
        } else if (clientName.equals(subs.get(0))) {
            status = ItemStatus.SELLING;
        } else if (clientName.equals(buyer)) {
            status = ItemStatus.WINNING;
        } else if (this.getSubscribersNames().contains(clientName)) {
            status = ItemStatus.WATCHING;
        } else {
            status = ItemStatus.AVAILABLE;
        }
    }

}
