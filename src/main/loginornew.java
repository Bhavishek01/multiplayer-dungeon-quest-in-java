package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class loginornew extends JPanel implements ActionListener {

    private gamepannel gp = new gamepannel();
    private Font arial_40;
    private JButton login, new_player;
    private CardLayout cardLayout;
    private JPanel cardPanel;


    public loginornew() {
        // Set up panel properties
        this.setPreferredSize(new Dimension(gp.base, gp.height));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        // Fixed font: Use BOLD (CENTER_BASELINE isn't valid)
        arial_40 = new Font("Arial", Font.BOLD, 25);

        // Use CardLayout for panel switching
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(this, "loginornew"); // Add this panel to cards

        // Create and add login panel
        login loginPanel = new login();
        cardPanel.add(loginPanel, "login");

        // Use BoxLayout for vertical stacking
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel promptLabel = new JLabel("Dungeon Quest");
        promptLabel.setFont(arial_40);
        promptLabel.setForeground(Color.WHITE);
        promptLabel.setAlignmentY(Component.TOP_ALIGNMENT);
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center horizontally
        add(promptLabel);

        // Add flexible space above for vertical centering
        add(Box.createVerticalGlue());
        
        // Login button (stacked vertically, right-aligned)
        login = new JButton("Login");
        login.setFont(arial_40);
        login.setBorderPainted(false);
        login.setBackground(getBackground());
        login.setForeground(Color.WHITE);
        login.setAlignmentX(Component.CENTER_ALIGNMENT);  // Right-align horizontally
        login.addActionListener(this);
        add(login);

        // New Player button (below Login, right-aligned)
        new_player = new JButton("New Player");
        new_player.setFont(arial_40);
        new_player.setBorderPainted(false);
        new_player.setBackground(getBackground());
        new_player.setForeground(Color.WHITE);
        new_player.setAlignmentX(Component.CENTER_ALIGNMENT);  // Right-align horizontally
        new_player.addActionListener(this);
        add(new_player);

        // Add flexible space below for vertical centering
        add(Box.createVerticalGlue());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login) 
        {
            cardLayout.show(cardPanel, "login");
        }
        else if (e.getSource() == new_player) 
        {
            System.out.println("New Player clicked!"); // Add your logic here
        }
    }

    // Method to get the card panel for frame
    public JPanel getCardPanel() 
    {
        return cardPanel;
    }
}