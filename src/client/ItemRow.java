package client;

import java.util.function.Consumer;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import client.strategy.StrategyVariants;

public class ItemRow extends JPanel implements ActionListener {
    private JLabel itemNameLabel;
    private JLabel currentBuyerLabel;
    private JLabel currentPriceLabel;
    private JPanel optionsBox;
    private JPanel strategyPanel;
    private JSpinner bidChooser;
    private JButton makeBidButton;
    private JButton observeButton;
    private JButton sniperStrategyButton;
    private JButton eagerStrategyButton;
    private Consumer<Double> onBidChangeCallback;
    private Consumer<StrategyVariants> onStrategyButtonClick;
    private Runnable onObserveButtonClick;

    private Color bgColor;
    private JPanel buttonOptionsBox;

    private static boolean areWeCurrentBuyer(ClientItem item, String clientName){
        return clientName.equals(item.getCurrentBuyer());
    }

    public ItemRow(
            ClientItem item,
            Consumer<Double> onBidChangeCallback,
            Runnable onObserveButtonClick,
            Consumer<StrategyVariants> onStrategyButtonClick,
            String clientName) {
        this.onBidChangeCallback = onBidChangeCallback;
        this.onStrategyButtonClick = onStrategyButtonClick;
        this.onObserveButtonClick = onObserveButtonClick;

        this.itemNameLabel = new JLabel(
            "<html>" + 
            item.getName() + 
            " - " + 
            item.getDescription() + 
            "</html>"
        );
        this.currentBuyerLabel = new JLabel(
                areWeCurrentBuyer(item, clientName)
                        ? "THIS CLIENT"
                        : item.getCurrentBuyer());
        this.currentPriceLabel = new JLabel(String.valueOf(item.getPrice()));

        this.setLayout(new GridLayout(1, 5));

        SpinnerModel model = new SpinnerNumberModel(
            item.getPrice(), 1, Float.POSITIVE_INFINITY, 1);
        this.bidChooser = new JSpinner(model);
        this.makeBidButton = new JButton("Confirm");
        makeBidButton.addActionListener(this);
        this.observeButton = new JButton("Observe");
        observeButton.addActionListener(this);

        this.sniperStrategyButton = new JButton("Set sniper strategy");
        sniperStrategyButton.addActionListener(this);
        this.eagerStrategyButton = new JButton("Set eager strategy");
        eagerStrategyButton.addActionListener(this);

        this.optionsBox = new JPanel();
        this.optionsBox.setLayout(new BoxLayout(optionsBox, BoxLayout.X_AXIS));

        this.buttonOptionsBox = new JPanel();
        this.buttonOptionsBox.setLayout(
            new BoxLayout(buttonOptionsBox, BoxLayout.Y_AXIS));
        this.buttonOptionsBox.add(makeBidButton);
        this.buttonOptionsBox.add(observeButton);

        this.optionsBox.add(bidChooser);
        this.optionsBox.add(buttonOptionsBox);

        this.strategyPanel = new JPanel();
        this.strategyPanel.setLayout(
            new BoxLayout(strategyPanel, BoxLayout.Y_AXIS));
        this.strategyPanel.add(sniperStrategyButton);
        this.strategyPanel.add(eagerStrategyButton);

        switch (item.getStatus()) {
            case AVAILABLE:
                bgColor = Color.WHITE;
                break;
            case ENDED:
                bgColor = Color.RED;
                break;
            case SELLING:
                bgColor = Color.YELLOW;
                break;
            case WINNING:
                bgColor = new Color(138, 255, 169);
                break;
            case WATCHING:
                bgColor = Color.BLUE;
                break;
            case BOUGHT:
                bgColor = Color.GREEN;
                break;
        }

        this.itemNameLabel.setOpaque(false);

        this.currentBuyerLabel.setOpaque(false);

        this.currentPriceLabel.setOpaque(false);
        this.setBackground(bgColor);
        this.optionsBox.setBackground(bgColor);

        this.add(itemNameLabel);
        this.add(currentBuyerLabel);
        this.add(currentPriceLabel);
        this.add(optionsBox);
        this.add(strategyPanel);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == this.makeBidButton) {
            this.onBidChangeCallback.accept(
                    (Double) bidChooser.getValue());
        } else if (event.getSource() == this.observeButton) {
            this.onObserveButtonClick.run();
        } else if (event.getSource() == this.sniperStrategyButton) {
            this.onStrategyButtonClick.accept(
                StrategyVariants.SNIPER_STRATEGY);
        } else if (event.getSource() == this.eagerStrategyButton) {
            this.onStrategyButtonClick.accept(
                StrategyVariants.EAGER_STRATEGY);
        }
    }
}
