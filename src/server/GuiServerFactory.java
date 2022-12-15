package server;

public class GuiServerFactory implements IServerFactory{

    @Override
    public IServer createServer() {
        return new GuiAuctionServer();
    }
    
}
