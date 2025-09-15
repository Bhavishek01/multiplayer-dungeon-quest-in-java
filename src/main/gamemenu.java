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

import background.loginphoto;

public class gamemenu extends loginphoto implements ActionListener {
    private Font arial_40;
    private JButton single,multi,inventory,exit,loginmenu;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private start st;

    public gamemenu(CardLayout cardLayout, JPanel cardPanel,start start) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.st = start;

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

        add(Box.createRigidArea(new Dimension(0, 20)));

        exit = new JButton("Exit");
        exit.setFont(arial_40);
        exit.setBorderPainted(true);
        exit.setBackground(Color.BLACK);
        exit.setForeground(Color.WHITE);
        exit.setOpaque(false);
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.addActionListener(this);
        add(exit);

        add(Box.createRigidArea(new Dimension(0, 20)));

        loginmenu = new JButton("Logout");
        loginmenu.setFont(arial_40);
        loginmenu.setBorderPainted(true);
        loginmenu.setBackground(Color.BLACK);
        loginmenu.setForeground(Color.WHITE);
        loginmenu.setOpaque(false);
        loginmenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginmenu.addActionListener(this);
        add(loginmenu);

        add(Box.createVerticalGlue());

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == single ) 
        {
            cardLayout.show(cardPanel, "start");
            cardPanel.revalidate();
            cardPanel.repaint();
            st.requestFocusInWindow();
            st.startgame();
            

        }
        else if (e.getSource() == multi ) {
            
            cardLayout.show(cardPanel, "start");
            cardPanel.revalidate();
            cardPanel.repaint();
            st.requestFocusInWindow();
            st.startgame();
        }
        else if (e.getSource() == inventory ) {
            
            // cardLayout.show(cardPanel, "inventory");
        }
        else if (e.getSource() == exit ) {
            
            System.exit(0);    
        }
        else if (e.getSource() == loginmenu ) {
            
            cardLayout.show(cardPanel, "loginornew"); // Start with loginornew panel
        }
    }
}