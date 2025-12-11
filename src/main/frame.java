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
        
        loginornew loginOrNewPanel = new loginornew(cardLayout, cardPanel);
        cardPanel.add(loginOrNewPanel, "loginornew");

        f.add(cardPanel);

        cardLayout.show(cardPanel, "loginornew"); // Start with loginornew pane

        f.pack();
        f.setVisible(true);
        f.setLayout(null);
        f.setLocationRelativeTo(null);
    }
}