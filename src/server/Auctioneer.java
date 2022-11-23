package server;

/*
 * The auctioneer has only one Item to watch over and
 */
public class Auctioneer implements Runnable {
    private int lifeOfAuction;
    private Runnable onAuctionEnd;

    public Auctioneer(int lifeOfAuction, Runnable onAuctionEnd) {
        this.lifeOfAuction = lifeOfAuction;
        this.onAuctionEnd = onAuctionEnd;
    }

    @Override
    public void run() {
        System.out.println(
                "AUCTION STARTED. Waiting " + this.lifeOfAuction + " till end");
        try {
            Thread.sleep(100 * lifeOfAuction);
            // Thread.sleep(1000 * lifeOfAuction);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.onAuctionEnd.run();
        System.out.println(
                "AUCTION ENDED. Waited for " + this.lifeOfAuction);
    }
}
