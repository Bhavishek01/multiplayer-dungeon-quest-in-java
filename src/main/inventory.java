package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import background.loginphoto;

public class inventory extends loginphoto implements ActionListener {
    private Font arial_40,arial_50;
    private JButton[] button;  //,loginmenu;
    public CardLayout cardLayout;
    public JPanel cardPanel;
    JLabel name,inventory,id;
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

        button[0] = new JButton("back");
        button[0].setFont(arial_40);
        button[0].setBorderPainted(true);
        button[0].setBackground(Color.BLACK);
        button[0].setForeground(Color.WHITE);
        button[0].addActionListener(this);
        top.add(button[0]);
        
        add(top);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue());

        if(gc.Items != null)
        {
            for(PlayerItem item : items)
            {
                if(item.name == "gun")
                {}
                button[i] = new JButton()
                button[i] = new JButton("Inventory");
                button[i].setFont(arial_40);
                button[i].setBorderPainted(true);
                button[i].setBackground(Color.BLACK);
                button[i].setForeground(Color.WHITE);
                button[i].setOpaque(false);
                button[i].setAlignmentX(Component.CENTER_ALIGNMENT);
                button[i].addActionListener(this);
                add(button[i]);

                add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        add(Box.createVerticalGlue());

    }
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == button[0])
        {
            gamemenu gamemenu = new gamemenu(cardLayout, cardPanel, gc);
            cardPanel.add(gamemenu, "gamemenu");
            cardLayout.show(cardPanel, "gamemenu"); 
        }
        else if (e.getSource() == inventory ) {
            
            inventory inv = new inventory(cardLayout, cardPanel, gc);
            cardPanel.add(inv, "inventory");
            cardLayout.show(cardPanel, "inventory");
        }

    //     else if (e.getSource() == loginmenu ) {
            
    //         cardLayout.show(cardPanel, "loginornew"); // Start with loginornew panel
    //     }
    }
}