package client;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.strategy.EagerStrategy;
import client.strategy.SniperStrategy;
import client.strategy.StrategyScheduler;
import client.strategy.StrategyVariants;
import common.IAuctionListener;
import common.Item;

import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import server.IAuctionServer;

public class ClientGui extends JFrame implements ActionListener, IAuctionListener {
    IAuctionServer serverInstance;
    private final int numBids = 2;

    private final String name;

    private JButton makeBids;

    private ArrayList<ClientItem> itemList;
    private List<ItemRow> itemRows;
    private JPanel rowsLayout;
    private JButton pullNewAuctions;
    private Thread strategyThread;
    private StrategyScheduler strategyScheduler;

    private static String generateName() {
        return "Client" + (System.currentTimeMillis() / 1000);
    }

    private void refreshServerData() {
        this.itemList.clear();
        try {
            Item[] items = serverInstance.getItems();
            System.out.println("Available items: " + items.length);
            for (Item item : items) {
                System.out.println(item);
                this.itemList.add(
                        new ClientItem(
                                item.getName(),
                                item.getDescription(),
                                item.getPrice(),
                                this.name,
                                item.getCurrentBuyer(),
                                item.getSubscribersNames()));

                this.strategyScheduler.synchronizeItem(item);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void refreshItem(Item item) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getName().equals(item.getName()) && item != null) {
                itemList.set(i,
                        new ClientItem(
                                item.getName(),
                                item.getDescription(),
                                item.getPrice(),
                                this.name,
                                item.getCurrentBuyer(),
                                item.getSubscribersNames()));
            }
        }

        this.strategyScheduler.synchronizeItem(item);
    }

    public void updateAuctions() {
        rowsLayout.removeAll();
        var oldSize = this.itemRows.size();
        this.itemRows.clear();

        for (ClientItem ci : this.itemList) {
            ItemRow row = new ItemRow(ci,
                    (val) -> {
                        try {
                            this.serverInstance.bidOnItem(
                                    this.name, ci.getName(), val);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    },
                    () -> {
                        try {
                            this.serverInstance.observeItem(this.name, ci.getName());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    },
                    (strategy) -> {
                        System.out.println("Strategy chosen: " + strategy);
                        this.strategyScheduler.removeStrategy(ci.getName());
                        if (strategy == StrategyVariants.SNIPER_STRATEGY) {
                            this.strategyScheduler.addStrategy(
                                    new SniperStrategy(ci));
                        } else if (strategy == StrategyVariants.EAGER_STRATEGY) {
                            this.strategyScheduler.addStrategy(
                                    new EagerStrategy(ci));
                        }
                    },
                    this.name);
            rowsLayout.add(row);
            this.itemRows.add(row);
        }

        rowsLayout.revalidate();
        rowsLayout.repaint();
        if (oldSize != itemList.size()) {
            setSize(800, 180 + this.itemRows.size() * 40);
        }
    }

    private void init() {
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        rowsLayout = new JPanel();
        rowsLayout.setLayout(new BoxLayout(rowsLayout, BoxLayout.Y_AXIS));
        add(rowsLayout);

        // add the make bid button
        makeBids = new JButton("Create an auction");
        makeBids.addActionListener(this);
        add(makeBids);

        pullNewAuctions = new JButton("Refresh auctions data");
        pullNewAuctions.addActionListener(this);
        add(pullNewAuctions);

        setSize(800, 180 + numBids * 60);
        setVisible(true);
    }

    public ClientGui(IAuctionServer serverInstance) {
        super("Client app");
        this.name = generateName();
        this.itemList = new ArrayList<ClientItem>();
        this.itemRows = new ArrayList<ItemRow>();
        this.init();

        this.strategyScheduler = new StrategyScheduler(serverInstance);
        this.strategyThread = new Thread(this.strategyScheduler);
        this.strategyThread.start();

        this.serverInstance = serverInstance;
        this.refreshServerData();
        this.updateAuctions();

        LongPollService lps = new LongPollService(serverInstance, this,
                (Item it) -> {
                    this.refreshItem(it);
                    this.updateAuctions();
                }, () -> {
                    this.strategyScheduler.stopScheduler();
                    try {
                        this.strategyThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
        lps.run();
        System.out.println("ROBIE AUKCJE");
        System.exit(0);
    }

    public ClientGui() {
        super("Client app");
        this.name = generateName();
        this.itemList = new ArrayList<ClientItem>();
        this.itemRows = new ArrayList<ItemRow>();
        this.init();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == makeBids) {
            var nameField = new JTextField(16);
            var descField = new JTextField(64);
            var priceField = new JTextField(16);
            var ttlField = new JTextField(16);

            var panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(new JLabel("name"));
            panel.add(nameField);

            panel.add(new JLabel("description"));
            panel.add(descField);

            panel.add(new JLabel("starting bid"));
            panel.add(priceField);

            panel.add(new JLabel("lifetime in secs"));
            panel.add(ttlField);

            int result = JOptionPane.showConfirmDialog(
                    null, panel, "Create new auction", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                if (this.serverInstance != null) {
                    try {
                        this.serverInstance.placeItemForBid(
                                this.name,
                                nameField.getText(),
                                descField.getText(),
                                Double.parseDouble(priceField.getText()),
                                Integer.parseInt(ttlField.getText()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("ROBIE AUKCJE");
            }
        } else if (event.getSource() == pullNewAuctions) {
            this.refreshServerData();
            this.updateAuctions();
        }
    }

    @Override
    public String getListenerName() {
        return name;
    }

    public void setItemList(List<ClientItem> itemList) {
        this.itemList = new ArrayList<>(itemList);
    }

    public static void main(String[] args) {
        ClientGui clientGui = new ClientGui();
        List<ClientItem> lci = new ArrayList<ClientItem>();

        for (int i = 0; i < 4; i++) {
            lci.add(
                    new ClientItem(
                            "item przedmiot numer: " + i,
                            "description dlugi opis lalalala",
                            (float) (10.0 + i),
                            "aaaaa1",
                            "aaaaa" + i,
                            Arrays.asList(new String[] { "creator" })));
        }

        clientGui.setItemList(lci);
        clientGui.updateAuctions();
    }
}
