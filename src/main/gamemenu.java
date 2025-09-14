package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import background.background;

public class gamemenu extends background implements ActionListener {
    private Font arial_40;
    private JButton single,multi,inventory;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private gamepannel gp; // Store gamepannel reference

    public gamemenu(CardLayout cardLayout, JPanel cardPanel, gamepannel gp) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.gp = gp;

        // Set up panel properties
        this.setPreferredSize(new Dimension(gp.base, gp.height)); // Match gamepannel dimensions
        this.setBackground(Color.BLACK);
        this.setOpaque(true);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        arial_40 = new Font("Arial", Font.BOLD, 25);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel promptLabel = new JLabel("Menu");
        promptLabel.setFont(arial_40);
        promptLabel.setForeground(Color.WHITE);
        promptLabel.setAlignmentX(Component.TOP_ALIGNMENT);
        add(promptLabel);

        add(Box.createVerticalGlue());

        single = new JButton("Single Player");
        single.setFont(arial_40);
        single.setBorderPainted(true);
        single.setBackground(Color.BLACK);
        single.setForeground(Color.WHITE);
        single.setOpaque(false);
        single.setAlignmentX(Component.CENTER_ALIGNMENT);
        single.addActionListener(this);
        add(single);


        add(Box.createRigidArea(new Dimension(0, 20)));

        multi = new JButton("Multi Player");
        multi.setFont(arial_40);
        multi.setBorderPainted(true);
        multi.setBackground(Color.BLACK);
        multi.setForeground(Color.WHITE);
        multi.setOpaque(false);
        multi.setAlignmentX(Component.CENTER_ALIGNMENT);
        multi.addActionListener(this);
        add(multi);


        add(Box.createRigidArea(new Dimension(0, 20)));

        inventory = new JButton("Inventory");
        inventory.setFont(arial_40);
        inventory.setBorderPainted(true);
        inventory.setBackground(Color.BLACK);
        inventory.setForeground(Color.WHITE);
        inventory.setOpaque(false);
        inventory.setAlignmentX(Component.CENTER_ALIGNMENT);
        inventory.addActionListener(this);
        add(inventory);

        add(Box.createVerticalGlue());

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == single ) 
        {
            cardLayout.show(cardPanel, "gamepannel");
            cardPanel.revalidate(); // Ensure layout updates
            cardPanel.repaint(); // Force repaint
            gp.requestFocusInWindow();
            gp.startgame(); // Directly call startgame on gamePanel
        }
        else if (e.getSource() == multi ) {
            
            cardLayout.show(cardPanel, "gamepannel");
            cardPanel.revalidate(); // Ensure layout updates
            cardPanel.repaint(); // Force repaint
            gp.requestFocusInWindow();
            gp.startgame(); // Directly call startgame on gamePanel
        }
        else if (e.getSource() == inventory ) {
            
            // cardLayout.show(cardPanel, "inventory");
        }
    }
}