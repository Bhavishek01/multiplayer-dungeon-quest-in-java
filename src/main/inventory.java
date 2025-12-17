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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import background.loginphoto;
import items.*;

public class inventory extends loginphoto implements ActionListener {
    private Font arial_40,arial_50;
    private List <JButton> buttons = new ArrayList<>(); 
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

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    openmenu();
                }
            }
        });

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
        
        // add(top);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // add(Box.createVerticalGlue());
        add(top, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(Box.createVerticalGlue());

            for (PlayerItem item : items) {
                System.out.println(item.name);
            if (item.quantity != 0) {
                itemsdetail detail;
                try {
                    detail = returnobj(item.name);
                    if (detail != null) {
                        JButton button = new JButton();
                        button.setIcon(new ImageIcon(detail.image));
                        button.setBorderPainted(true);
                        button.setBackground(Color.white);
                        button.setForeground(Color.WHITE);
                        button.setOpaque(true);
                        button.setAlignmentX(Component.CENTER_ALIGNMENT);
                        button.addActionListener(this);

                        button.putClientProperty("item", item);
                        button.putClientProperty("detail", detail);

                        buttons.add(button);
                        centerPanel.add(button);
                        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                        
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        }

        centerPanel.add(Box.createVerticalGlue());
        add(centerPanel, BorderLayout.CENTER);

    }
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == back)
        {
            openmenu();
        }
        for (JButton button : buttons) {
            if (e.getSource() == button) {
                PlayerItem item = (PlayerItem) button.getClientProperty("item");
                itemsdetail detail = (itemsdetail) button.getClientProperty("detail");

                if (item != null && detail != null) {
                    String message = "<html>" +
                            "<b>Item:</b> " + detail.name + "<br>" +
                            "<b>Quantity:</b> " + item.quantity + "<br>" +
                            "<b>About:</b> " + detail.about +
                            "</html>";

                    JOptionPane.showMessageDialog(
                    this,
                    message,
                    "Item Details",
                    JOptionPane.PLAIN_MESSAGE
                );
                }
                break;
            }
        }
    }


    public itemsdetail returnobj(int a) throws IOException
    {
        switch (a) 
        {
            case 1:
                arrow arrow = new arrow();
                return arrow;

            case 2:
                bow bow= new bow();
                return bow;

            case 3:
                bullet bullet = new bullet();
                return bullet;

            case 4:
                gold_bullet gold_bullet = new gold_bullet();
                return gold_bullet;

            case 5:
                golden_gun golden_gun = new golden_gun();
                return golden_gun;

            case 6:
                shoe shoe = new shoe();
                return shoe;

            case 7:
                silver_gun silver_gun= new silver_gun();
                return silver_gun;

            case 8:
                sword sword = new sword();
                return sword;
        
            default:
                return null;
        }
        
    }

    public void openmenu()
    {
        System.out.println("back to menu");
        gamemenu gamemenu = new gamemenu(cardLayout, cardPanel, gc);
        cardPanel.add(gamemenu, "gamemenu");
        cardLayout.show(cardPanel, "gamemenu"); 

    }
}