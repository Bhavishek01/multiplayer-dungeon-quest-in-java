package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import background.loginphoto;

public class gamemenu extends loginphoto implements ActionListener {
    private Font arial_40,arial_50,arial_30;
    private JButton single,inventory,exit;  //,loginmenu;
    public CardLayout cardLayout;
    public JPanel cardPanel;
    JLabel name,promptLabel,id;
    gameclient gc;

    public gamemenu(CardLayout cardLayout, JPanel cardPanel,gameclient gc) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.gc = gc;

        setLayout(new BorderLayout());

        arial_40 = new Font("Arial", Font.BOLD, 25);
        arial_30 = new Font("Arial", Font.BOLD, 20);
        arial_50 = new Font("Arial", Font.BOLD, 40);

        JPanel top = new JPanel();

        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));

        name = new JLabel();
        name.setText("Player Name: " + gc.name);
        name.setFont(arial_30);
        name.setForeground(Color.WHITE);
        // name.setAlignmentX(Component.RIGHT_ALIGNMENT);
        // name.setAlignmentY(Component.TOP_ALIGNMENT);
        top.add(name,BorderLayout.WEST);
        top.add(Box.createHorizontalGlue());

        promptLabel = new JLabel("Menu");
        promptLabel.setFont(arial_50);
        promptLabel.setForeground(Color.WHITE); 
        promptLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // promptLabel.setAlignmentY(Component.TOP_ALIGNMENT);
        top.add(promptLabel,BorderLayout.CENTER);
        top.add(Box.createHorizontalGlue());

        id = new JLabel();
        id.setText("Player ID: "+gc.id);
        id.setFont(arial_30);
        id.setForeground(Color.WHITE);
        // id.setAlignmentX(Component.LEFT_ALIGNMENT);
        // id.setAlignmentY(Component.TOP_ALIGNMENT);
        top.add(id,BorderLayout.EAST);
        
        add(top);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue());

        single = new JButton("Start Game");
        single.setFont(arial_40);
        single.setBorderPainted(true);
        single.setBackground(Color.BLACK);
        single.setForeground(Color.WHITE);
        single.setOpaque(false);
        single.setAlignmentX(Component.CENTER_ALIGNMENT);
        single.addActionListener(this);
        add(single);

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

        // add(Box.createRigidArea(new Dimension(0, 20)));

        // loginmenu = new JButton("Logout");
        // loginmenu.setFont(arial_40);
        // loginmenu.setBorderPainted(true);
        // loginmenu.setBackground(Color.BLACK);
        // loginmenu.setForeground(Color.WHITE);
        // loginmenu.setOpaque(false);
        // loginmenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        // loginmenu.addActionListener(this);
        // add(loginmenu);

        add(Box.createVerticalGlue());

    }
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == single )
        {
            gamehandler gh = new gamehandler(cardLayout,cardPanel,gc);
            
            cardPanel.add(gh, "gamehandler");
            cardLayout.show(cardPanel, "gamehandler");
            cardPanel.revalidate();
            cardPanel.repaint();
            gh.requestFocusInWindow();
            gh.startgame();
        }
        else if (e.getSource() == inventory ) {

            gc.send("INVENTORY");
            
            inventory inv = new inventory(cardLayout, cardPanel, gc);
            cardPanel.add(inv, "inventory");
            cardLayout.show(cardPanel, "inventory");
        }
        else if (e.getSource() == exit ) {
            
            System.exit(0);    
        }
    //     else if (e.getSource() == loginmenu ) {
            
    //         cardLayout.show(cardPanel, "loginornew"); // Start with loginornew panel
    //     }
    }
}