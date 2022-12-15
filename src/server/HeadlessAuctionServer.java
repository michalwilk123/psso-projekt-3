package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HeadlessAuctionServer implements IServer{

    @Override
    public void start() {
        String registryURL;

        final int RMIPortNum = 1099;

        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            registry.list();
            // This call will throw an exception
            // if the registry does not already exist
        } catch (RemoteException e) {
            // No valid registry at that port.
            try {
                Registry registry = LocateRegistry.createRegistry(RMIPortNum);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }

        // set up the server instance
        AuctionServerInstance auctionServer = AuctionServerInstance.getInstance();

        registryURL = "rmi://localhost:" + RMIPortNum + "/AuctionServer";

        try {
            Naming.rebind(registryURL, auctionServer);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println("Headless auction Server ready.");

        AuctionServerInstance.getInstance();
    }
    
}
