package common;

public interface IAuctionListener {
    public String getListenerName();
    public void notifyAboutAuctions(Item it);
}
