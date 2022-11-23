package client;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import common.IAuctionListener;
import common.Item;

import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.List;
import java.awt.*;

import server.IAuctionServer;

public class ClientGui extends JFrame implements ActionListener, IAuctionListener {
    IAuctionServer serverInstance;
    private final int numBids = 2;
    private JTextField bidderNums[];
    private JTextField itemNums[];
    private JTextField bidAmounts[];
    
    private final String name;
    private JLabel itemLabels[];
    private JLabel currentPrices[];
    private JLabel currentBuyers[];
    private JButton makeBidButtons[];

    private JButton makeBids;
    private JButton clearBids;

    private List<ClientItem> itemList;

    private static String generateName(){
        return "Client" + ( System.currentTimeMillis() / 1000 );
    }

    private void refreshServerData(){
        System.out.println("PULLING FROM SERVER");
        try {
            Item[] items = serverInstance.getItems();
            for (Item item : items) {
                System.out.println(item);
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void updateAuctions(){

    }

    private void init() {
        setLayout(new GridLayout(numBids + 2, 4));

        // add the column headings
        add(new JLabel("Item name"));
        add(new JLabel("Current Price"));
        add(new JLabel("Current Buyer"));
        add(new JLabel("Make a bid"));

        // add the Textfield to enter bids
        itemLabels = new JLabel[numBids];
        currentPrices = new JLabel[numBids];
        currentBuyers = new JLabel[numBids];
        makeBidButtons = new JButton[numBids];

        bidderNums = new JTextField[numBids];
        itemNums = new JTextField[numBids];
        bidAmounts = new JTextField[numBids];

        for (int count = 0; count < numBids; count++) {
            itemLabels[count] = new JLabel("Przedmiot 123");
            add(itemLabels[count]);
            currentPrices[count] = new JLabel("200");
            add(currentPrices[count]);
            currentBuyers[count] = new JLabel("KUPUJACY");
            add(currentBuyers[count]);
            makeBidButtons[count] = new JButton("KUP");
            add(makeBidButtons[count]);
        }

        // add the make bid button
        makeBids = new JButton("Record Bids");
        makeBids.addActionListener(this);
        add(makeBids);

        // add the make bid button
        clearBids = new JButton("Clear Bids");
        clearBids.addActionListener(this);
        add(clearBids);

        setSize(700, 150 + numBids * 60);
        setVisible(true);
    }

    public ClientGui(IAuctionServer serverInstance) {
        super("Client app");
        this.name = generateName();
        this.init();
        this.serverInstance = serverInstance;
        this.refreshServerData();
    }

    public ClientGui() {
        super("Client app");
        this.name = generateName();
        this.init();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == makeBids)
            doMakeBids();

        else if (event.getSource() == clearBids)
            doClearBids();
    }

    public void doMakeBids() {
        String sbnum;
        String inum;
        String sbamount;
        int bnum;
        int bamount;

        try {

            // check for valid data
            for (int count = 0; count < numBids; count++) {
                sbnum = bidderNums[count].getText();
                inum = itemNums[count].getText();
                sbamount = bidAmounts[count].getText();

                // System.out.println ("*" + sbnum + "*" + inum + "*" + sbamount + "*");
                if (sbnum.length() == 0 && inum.length() == 0 && sbamount.length() == 0)
                    ;
                else if (sbnum.length() == 0) {
                    // custom title, error icon
                    JOptionPane.showMessageDialog(this,
                            "Bidder Number, Item Number and Bid Amount must be filled in.",
                            "Error: Blank Bidder Number",
                            JOptionPane.ERROR_MESSAGE);
                    bidderNums[count].requestFocus();
                    return;
                } else if (inum.length() == 0) {
                    // custom title, error icon
                    JOptionPane.showMessageDialog(this,
                            "Bidder Number, Item Number and Bid Amount must be filled in.",
                            "Error: Blank Item Number",
                            JOptionPane.ERROR_MESSAGE);
                    itemNums[count].requestFocus();
                    return;
                } else if (sbamount.length() == 0) {
                    // custom title, error icon
                    JOptionPane.showMessageDialog(this,
                            "Bidder Number, Item Number and Bid Amount must be filled in.",
                            "Error: Blank Bidder Amount",
                            JOptionPane.ERROR_MESSAGE);
                    bidAmounts[count].requestFocus();
                    return;
                } else {
                    // if (!dc.validateItemNumber(inum))
                    // {
                    // //custom title, error icon
                    // JOptionPane.showMessageDialog(this,
                    // "Item Number does not exist.",
                    // "Error: Invalid Bidder Number",
                    // JOptionPane.ERROR_MESSAGE);
                    // itemNums[count].requestFocus();
                    // return;
                    // }

                    // try{
                    // bnum = Integer.parseInt(sbnum);
                    // //System.out.println (sbnum + ", " + bnum );
                    // if (!dc.validateBidderNumber(bnum))
                    // {
                    // //custom title, error icon
                    // JOptionPane.showMessageDialog(this,
                    // "Bidder Number does not exist.",
                    // "Error: Invalid Bidder Number",
                    // JOptionPane.ERROR_MESSAGE);
                    // bidderNums[count].requestFocus();
                    // return;
                    // }
                    // }
                    // catch (NumberFormatException e)
                    // {
                    // //custom title, error icon
                    // JOptionPane.showMessageDialog(this,
                    // "Bidder Number must be a number.",
                    // "Error: Invalid Bidder Number",
                    // JOptionPane.ERROR_MESSAGE);
                    // bidderNums[count].requestFocus();
                    // return;
                    // }

                    try {
                        bamount = Integer.parseInt(sbamount);
                    } catch (NumberFormatException e) {
                        // custom title, error icon
                        JOptionPane.showMessageDialog(this,
                                "Bid Amount must be a number.",
                                "Error: Invalid Bid Amount",
                                JOptionPane.ERROR_MESSAGE);
                        bidAmounts[count].requestFocus();
                        return;
                    }
                }
            }

            // process entries
            for (int count = 0; count < numBids; count++) {
                sbnum = bidderNums[count].getText();
                inum = itemNums[count].getText();
                sbamount = bidAmounts[count].getText();

                if (sbnum.length() == 0 && inum.length() == 0 && sbamount.length() == 0)
                    ;
                else {
                    try {
                        bnum = Integer.parseInt(sbnum);
                        bamount = Integer.parseInt(sbamount);
                        // Item retItem = dc.makeBid (bnum, inum, bamount, false);

                        // check for valid return
                        // if (retItem.getCode() == -3)
                        // {
                        // int n = JOptionPane.showConfirmDialog(this,
                        // "The bid for item " + inum +
                        // " that was just entered \n for Bidder: " +
                        // bnum + "\n for Amount: " + bamount +
                        // "\nalready has a bid.\n\n" +
                        // "The previous bid is\n for Bidder: " +
                        // retItem.getBidder() + "\n for Amount: " +
                        // retItem.getBidAmount() +
                        // "\n\nDo you wish to proceed with this new bid?",
                        // "Bid Already Exists for Item",
                        // JOptionPane.YES_NO_OPTION);
                        // if (n == JOptionPane.YES_OPTION)

                        // retItem = dc.makeBid (bnum, inum, bamount, true);

                        // }

                        // // check for valid return
                        // if (retItem.getCode() == 1)
                        // {
                        // bidderNums[ count ].setText ("");
                        // itemNums[ count ].setText ("");
                        // bidAmounts[ count ].setText ("");
                        // }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            itemNums[0].requestFocus();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doClearBids() {
        for (int count = 0; count < numBids; count++) {
            bidderNums[count].setText("");
            itemNums[count].setText("");
            bidAmounts[count].setText("");
        }
        itemNums[0].requestFocus();
    }

    public static void main(String[] args) {
        ClientGui clientGui = new ClientGui();
    }

    @Override
    public void notifyAboutAuctions(Item it) {
        this.refreshServerData();
    }

    @Override
    public String getListenerName() {
        return name;
    }
}
