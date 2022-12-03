package server;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

public class ServerGui extends JFrame implements ActionListener{
    private JButton exitButton;
    AuctionServerInstance auctionServerInstance = null;

    private void init(){
        FlowLayout layout = new FlowLayout();
        Container container = getContentPane();
        container.setLayout(layout);  

        Container innerContainer = getContentPane();
        innerContainer.setLayout(new BoxLayout(innerContainer, BoxLayout.Y_AXIS));

        exitButton = new JButton ("Shut Down Server");
        exitButton.addActionListener (this);
        exitButton.setAlignmentX(CENTER_ALIGNMENT);
        innerContainer.add(exitButton);

        setSize( 700, 500 );
        setVisible( true );
    }

    public ServerGui() {
        super ("Auction Server Interface");
        this.init();
    }

    public ServerGui(AuctionServerInstance server) {
        super ("Auction Server Interface");
        this.init();
        auctionServerInstance = server;
    }

    @Override
    public void actionPerformed( ActionEvent event ) {
        if (event.getSource() == exitButton)
            doShutDownServer();
    }

    public void doShutDownServer() {

        int n = JOptionPane.showConfirmDialog(this,
            "Would you really wish to shut down the server?",
            "Shut Down Server",
            JOptionPane.YES_NO_OPTION);

        if (n == JOptionPane.YES_OPTION)
            System.exit( 0 );
    }

    public static void main(String[] args) {
        new ServerGui();
    }
}
