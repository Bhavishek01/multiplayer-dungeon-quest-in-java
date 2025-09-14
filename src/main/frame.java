package main;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class frame{

    public static void main(String[] args) {

        JFrame f = new JFrame("Doungen quest");
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create CardLayout and container panel
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        // Create panels
        loginornew loginOrNewPanel = new loginornew(cardLayout, cardPanel);
        gamepannel gamePanel = new gamepannel();
        login loginPanel = new login(cardLayout, cardPanel);
        gamemenu gamemenu = new gamemenu(cardLayout, cardPanel, gamePanel);

        // Add panels to cardPanel
        cardPanel.add(loginOrNewPanel, "loginornew");
        cardPanel.add(loginPanel, "login");
        cardPanel.add(gamePanel, "gamepannel");
        cardPanel.add(gamemenu, "gamemenu");

        // Add cardPanel to frame
        f.add(cardPanel);
        cardLayout.show(cardPanel, "loginornew"); // Start with loginornew panel

        f.pack();

        f.setVisible(true);
        f.setLayout(null);
        f.setLocationRelativeTo(null);

    }
    
}
