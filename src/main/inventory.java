package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import background.loginphoto;
import items.*;

public class inventory extends loginphoto implements ActionListener {
    private Font arial_40,arial_50;
    private JButton[] button; 
    private JButton back; 
    public CardLayout cardLayout;
    public JPanel cardPanel;
    JLabel name,inventory;
    gameclient gc;

    public List<PlayerItem> items = new ArrayList<>();

    public inventory(CardLayout cardLayout, JPanel cardPanel,gameclient gc) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.gc = gc;

        this.items = gc.Items;

        setLayout(new BorderLayout());

        arial_40 = new Font("Arial", Font.BOLD, 25);
        arial_50 = new Font("Arial", Font.BOLD, 40);

        JPanel top = new JPanel();

        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));

        name = new JLabel();
        name.setText("Player Name: " + gc.name);
        name.setFont(arial_40);
        name.setForeground(Color.WHITE);
        // name.setAlignmentX(Component.RIGHT_ALIGNMENT);
        // name.setAlignmentY(Component.TOP_ALIGNMENT);
        top.add(name,BorderLayout.WEST);
        top.add(Box.createHorizontalGlue());

        inventory = new JLabel("inventory");
        inventory.setFont(arial_50);
        inventory.setForeground(Color.WHITE); 
        inventory.setHorizontalAlignment(SwingConstants.CENTER);
        // promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // promptLabel.setAlignmentY(Component.TOP_ALIGNMENT);
        top.add(inventory,BorderLayout.CENTER);
        top.add(Box.createHorizontalGlue());

        back = new JButton("back");
        back.setFont(arial_40);
        back.setBorderPainted(true);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.setOpaque(false);
        back.addActionListener(this);
        top.add(back);
        
        add(top);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue());

            for (int i = 0; i < items.size(); i++) {
            PlayerItem item = items.get(i);
            itemsdetail detail;
            try 
            {
                detail = returnobj(item.name);

                button[i] = new JButton();
                button[i].setIcon(new ImageIcon(detail.image));
                button[i].setBorderPainted(true);
                button[i].setBackground(Color.BLACK);
                button[i].setForeground(Color.WHITE);
                button[i].setOpaque(false);
                button[i].setAlignmentX(Component.CENTER_ALIGNMENT);
                button[i].addActionListener(this);

                add(button[i]);
                add(Box.createRigidArea(new Dimension(0, 5)));
            }
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }

        add(Box.createVerticalGlue());

    }
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == back)
        {
            gamemenu gamemenu = new gamemenu(cardLayout, cardPanel, gc);
            cardPanel.add(gamemenu, "gamemenu");
            cardLayout.show(cardPanel, "gamemenu"); 
        }
        else if (e.getSource() == button) {
            
            inventory inv = new inventory(cardLayout, cardPanel, gc);
            cardPanel.add(inv, "inventory");
            cardLayout.show(cardPanel, "inventory");
        }
    }

    public itemsdetail returnobj(String a) throws IOException
    {
        switch (a) 
        {
            case "arrow":
                arrow arrow = new arrow();
                return arrow;

            case "bow":
                bow bow= new bow();
                return bow;

            case "bullet":
                bullet bullet = new bullet();
                return bullet;

            case "gold_bullet":
                gold_bullet gold_bullet = new gold_bullet();
                return gold_bullet;

            case "golden_gun":
                golden_gun golden_gun = new golden_gun();
                return golden_gun;

            case "shoe":
                shoe shoe = new shoe();
                return shoe;

            case "silver_gun":
                silver_gun silver_gun= new silver_gun();
                return silver_gun;

            case "sword":
                sword sword = new sword();
                return sword;
        
            default:
                return null;
        }
        
    }
}