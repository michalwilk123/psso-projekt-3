package client;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class ItemRow {
    private ClientItem item;
    private JLabel itemNameLabel;
    private ItemStatus itemStatusLabel;
    private JLabel currentBuyerLabel;
    private JLabel currentPriceLabel;
    private Box optionsBox;
    private JSpinner bidChooser;
    private JButton makeBidButton;
    private Consumer<Float> onBidChangeCallback;

    public Consumer<Float> getOnBidChangeCallback() {
        return onBidChangeCallback;
    }

    private Color bgColor;

    public ItemRow(
            ClientItem item,
            Consumer<Float> onBidChangeCallback) {
        this.onBidChangeCallback = onBidChangeCallback;
        this.item = item;
        this.itemNameLabel = new JLabel(item.getName());
        this.currentBuyerLabel = new JLabel(item.getCurrentBuyer());
        this.currentPriceLabel = new JLabel(String.valueOf(item.getPrice()));

        SpinnerModel model = new SpinnerNumberModel(9.9, 1, 15, 0.1);  
        this.bidChooser = new JSpinner(model);
        this.makeBidButton = new JButton("Confirm");
        makeBidButton.addActionListener(getOnBidChangeCallback());

        this.optionsBox.add(Box.createHorizontalGlue());
        this.optionsBox.add(bidChooser);
        this.optionsBox.add(makeBidButton);
        
        switch (item.getStatus()){
            case AVAILABLE:
                bgColor = Color.WHITE;
                break;
            case ENDED:
                bgColor = Color.RED;
                break;
            case WATCHING:
                bgColor = Color.BLUE;
                break;
            case WON:
                bgColor = Color.GREEN;
                break;
        }

        this.itemNameLabel.setOpaque(false);
        this.itemNameLabel.setBackground(bgColor);

        this.currentBuyerLabel.setOpaque(false);
        this.currentBuyerLabel.setBackground(bgColor);

        this.currentPriceLabel.setOpaque(false);
        this.currentPriceLabel.setBackground(bgColor);
    }


    // public JComponent[] createRow(){
    //     return {};
    // }
}
