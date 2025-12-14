package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import background.loginphoto;
import socket_and_Jdbc.DatabaseConnection;

public class inventory extends loginphoto 
{

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private String playerId;
    private Font arial_40 = new Font("Arial", Font.BOLD, 25);
    private Font arial_30 = new Font("Arial", Font.BOLD, 20);
    private JPanel listPanel;
    private JPanel detailsPanel;

    public inventory(CardLayout cardLayout, JPanel cardPanel, String playerId) 
    {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.playerId = playerId;
        
        setLayout(new CardLayout());
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false); // For background visibility
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        add(listPanel, "list");
        add(detailsPanel, "details");
        fetchAndDisplayItems();
        ((CardLayout) getLayout()).show(this, "list");
    }
private void fetchAndDisplayItems() {
listPanel.removeAll();
JLabel title = new JLabel("Your Inventory");
title.setFont(arial_40);
title.setForeground(Color.WHITE);
title.setAlignmentX(Component.CENTER_ALIGNMENT);
listPanel.add(title);
listPanel.add(Box.createVerticalGlue());
Connection conn = DatabaseConnection.getConnection();
try {
String query = "SELECT item_name, quantity, description FROM player_items WHERE playerid = ?";
PreparedStatement pstmt = conn.prepareStatement(query);
pstmt.setString(1, playerId);
ResultSet rs = pstmt.executeQuery();
boolean hasItems = false;
while (rs.next()) {
hasItems = true;
String name = rs.getString("item_name");
int qty = rs.getInt("quantity");
String desc = rs.getString("description");
JButton itemBtn = new JButton(name);
itemBtn.setFont(arial_30);
itemBtn.setBorderPainted(true);
itemBtn.setBackground(Color.BLACK);
itemBtn.setForeground(Color.WHITE);
itemBtn.setOpaque(false);
itemBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
itemBtn.addActionListener(new ActionListener() {
@Override
public void actionPerformed(ActionEvent e) {
showDetails(name, qty, desc);
}
});
listPanel.add(itemBtn);
listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
}
if (!hasItems) {
JLabel noItems = new JLabel("No items in inventory");
noItems.setFont(arial_30);
noItems.setForeground(Color.WHITE);
noItems.setAlignmentX(Component.CENTER_ALIGNMENT);
listPanel.add(noItems);
}
// Back to menu button
JButton backToMenu = new JButton("Back to Menu");
backToMenu.setFont(arial_30);
backToMenu.setBorderPainted(true);
backToMenu.setBackground(Color.BLACK);
backToMenu.setForeground(Color.WHITE);
backToMenu.setOpaque(false);
backToMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
backToMenu.addActionListener(e -> cardLayout.show(cardPanel, "gamemenu"));
listPanel.add(backToMenu);
} catch (SQLException e) {
e.printStackTrace();
JLabel errorLabel = new JLabel("Error fetching items");
errorLabel.setFont(arial_30);
errorLabel.setForeground(Color.RED);
errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
listPanel.add(errorLabel);
}
listPanel.add(Box.createVerticalGlue());
listPanel.revalidate();
listPanel.repaint();
}
private void showDetails(String name, int qty, String desc) {
detailsPanel.removeAll();
JLabel itemName = new JLabel("Item: " + name);
itemName.setFont(arial_40);
itemName.setForeground(Color.WHITE);
itemName.setAlignmentX(Component.CENTER_ALIGNMENT);
detailsPanel.add(itemName);
detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
JLabel quantity = new JLabel("Quantity: " + qty);
quantity.setFont(arial_30);
quantity.setForeground(Color.WHITE);
quantity.setAlignmentX(Component.CENTER_ALIGNMENT);
detailsPanel.add(quantity);
detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
JTextArea description = new JTextArea(desc);
description.setFont(arial_30);
description.setForeground(Color.WHITE);
description.setBackground(new Color(0, 0, 0, 0)); // Transparent
description.setWrapStyleWord(true);
description.setLineWrap(true);
description.setEditable(false);
description.setAlignmentX(Component.CENTER_ALIGNMENT);
detailsPanel.add(description);
detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
JButton backBtn = new JButton("Back to Inventory");
backBtn.setFont(arial_30);
backBtn.setBorderPainted(true);
backBtn.setBackground(Color.BLACK);
backBtn.setForeground(Color.WHITE);
backBtn.setOpaque(false);
backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
backBtn.addActionListener(e -> ((CardLayout) getLayout()).show(inventory.this, "list"));
detailsPanel.add(backBtn);
detailsPanel.revalidate();
detailsPanel.repaint();
((CardLayout) getLayout()).show(this, "details");
}
}