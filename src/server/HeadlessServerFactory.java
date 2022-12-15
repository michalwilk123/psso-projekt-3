package server;

public class HeadlessServerFactory implements IServerFactory{

    @Override
    public IServer createServer() {
        return new HeadlessAuctionServer();
    }
    
}
