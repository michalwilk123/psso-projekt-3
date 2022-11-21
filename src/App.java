import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JFrame;

import client.ClientGui;
import server.AuctionServerInstance;
import server.IAuctionServer;
import server.ServerGui;

public class App {
    private static void configureServer() {
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        String portNum, registryURL;

        int RMIPortNum = 1099;
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
        System.out.println("Auction Server ready.");

    }

    private static void startClient() {
        String hostName;
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        System.out.println(
                "Enter the RMIRegistry host namer:");
        hostName = "localhost:1099";
        // hostName = br.readLine();

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

        ClientGui clientGui = new ClientGui(dc);
        clientGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private static void startServer() {
        ServerGui serverGui = new ServerGui(AuctionServerInstance.getInstance());
        serverGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            if (args[0].equals("client")) {
                System.out.println("Starting auction client..");
                startClient();
            } else if (args[0].equals("server")) {
                System.out.println("Starting auction server..");
                configureServer();
                startServer();
            } else {
                System.out.println("Unknown command: " + args[0]);
            }
        } else {
            System.out.println("Command unspecified");
        }
    }
}
