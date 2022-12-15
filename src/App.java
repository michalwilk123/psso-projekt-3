import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


import client.ClientGui;
import server.GuiServerFactory;
import server.HeadlessServerFactory;
import server.IAuctionServer;
import server.IServer;
import server.IServerFactory;

public class App {
    private static void startClient() {
        String hostName;
        System.out.println(
                "Enter the RMIRegistry host namer:");
        hostName = "localhost:1099";

        IAuctionServer dc;

        try {
            dc = (IAuctionServer) Naming.lookup(
                    "rmi://" + hostName + "/AuctionServer");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        } catch (NotBoundException e) {
            e.printStackTrace();
            return;
        }

        new ClientGui(dc);
    }

    public static void main(String[] args) throws Exception {
        if (args.length >= 1) {
            if (args[0].equals("client")) {
                System.out.println("Starting auction client..");
                startClient();
            } else if (args[0].equals("server")) {
                IServerFactory serverFactory = null;

                if (args.length >= 2 && args[1].equals("headless")){
                    serverFactory = new HeadlessServerFactory();
                } else {
                    serverFactory = new GuiServerFactory();
                }
                IServer server = serverFactory.createServer();
                server.start();
            } else {
                System.out.println("Unknown command: " + args[0]);
            }
        } else {
            System.out.println("Command unspecified");
        }
    }
}
