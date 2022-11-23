package client;

import common.Item;

public class ClientItem extends Item{
    private final ItemStatus status;

    public ItemStatus getStatus() {
        return status;
    }

    public ClientItem(String name, float price, String clientName, String buyer) {
        super(name, price);

        if (this.isFinished()) {
            if (clientName.equals(buyer)) {
                status = ItemStatus.WON;
            } else {
                status = ItemStatus.ENDED;
            }
        } else if (clientName.equals(buyer)){
            status = ItemStatus.WATCHING;
        } else {
            status = ItemStatus.AVAILABLE;
        }
    }


    
}
