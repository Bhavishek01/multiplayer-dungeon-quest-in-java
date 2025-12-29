package main;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

import javax.swing.*;
import background.inventory_background;
import items.*;

public class inventory extends inventory_background implements ActionListener {

    private JButton back;
    public CardLayout cardLayout;
    public JPanel cardPanel;
    gameclient gc;

    public java.util.List<PlayerItem> items = new java.util.ArrayList<>();

    // Equip system
    private int[] equippedItems;
    private JLabel[] slotLabels = new JLabel[3];
    private JButton[] unequipButtons = new JButton[3];
    private Map<Integer, itemsdetail> itemCache = new HashMap<>();

    private JPanel centerPanel;

    public inventory(CardLayout cardLayout, JPanel cardPanel, gameclient gc) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.gc = gc;
        this.items = gc.Items;
        this.equippedItems = gc.equippedItems;

        // Cache item details
        try {
            itemCache.put(1, new arrow());
            itemCache.put(2, new bow());
            itemCache.put(3, new bullet());
            itemCache.put(4, new gold_bullet());
            itemCache.put(5, new golden_gun());
            itemCache.put(6, new shoe());
            itemCache.put(7, new silver_gun());
            itemCache.put(8, new sword());
            itemCache.put(9, new light());
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // ==================== TITLE ====================
        JLabel title = new JLabel("INVENTORY", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 50));
        title.setForeground(Color.CYAN);
        add(title, BorderLayout.NORTH);

        // ==================== EQUIPPED SLOTS ====================
        JPanel equippedPanel = new JPanel(new GridLayout(1, 3, 50, 20));
        equippedPanel.setOpaque(false);

        for (int i = 0; i < 3; i++) {
            final int slotIndex = i;

            slotLabels[i] = new JLabel();
            slotLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            slotLabels[i].setPreferredSize(new Dimension(160, 140));
            slotLabels[i].setBorder(BorderFactory.createLineBorder(Color.CYAN, 5));
            slotLabels[i].setBackground(new Color(20, 20, 40));
            slotLabels[i].setForeground(Color.WHITE);
            slotLabels[i].setOpaque(true);
            slotLabels[i].setFont(new Font("Arial", Font.BOLD, 18));

            unequipButtons[i] = new JButton("Unequip");
            unequipButtons[i].setFont(new Font("Arial", Font.BOLD, 16));
            unequipButtons[i].setEnabled(false);
            unequipButtons[i].addActionListener(e -> unequipItem(slotIndex));

            JPanel slotPanel = new JPanel(new BorderLayout(10, 10));
            slotPanel.setOpaque(false);
            slotPanel.add(slotLabels[i], BorderLayout.CENTER);
            slotPanel.add(unequipButtons[i], BorderLayout.SOUTH);

            equippedPanel.add(slotPanel);
        }

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setOpaque(false);
        topWrapper.add(equippedPanel, BorderLayout.CENTER);
        add(topWrapper, BorderLayout.PAGE_START);

        // ==================== ITEM LIST ====================
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);

        // ==================== BACK BUTTON ====================
        back = new JButton("Back to Menu");
        back.setFont(new Font("Arial", Font.BOLD, 24));
        back.setPreferredSize(new Dimension(300, 50));
        back.setBackground(new Color(0, 100, 0));
        back.setForeground(Color.WHITE);
        back.addActionListener(this);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(back);
        add(bottomPanel, BorderLayout.SOUTH);

        // Build UI
        refreshInventory();
        updateEquippedDisplay();
    }

    public void refreshInventory() {
        centerPanel.removeAll();

        centerPanel.add(Box.createVerticalStrut(30));

        for (PlayerItem playerItem : items) {
            if (playerItem.quantity <= 0) continue;

            itemsdetail detail = itemCache.get(playerItem.id);
            if (detail == null) continue;

            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
            itemPanel.setOpaque(false);
            itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 180, 255), 3),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
            itemPanel.setMaximumSize(new Dimension(800, 100));
            itemPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Icon
            JLabel iconLabel = new JLabel();
            Image scaled = detail.image.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaled));

            // Text info
            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false);

            JLabel nameLabel = new JLabel(detail.name.toUpperCase());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 22));
            nameLabel.setForeground(Color.CYAN);

            JLabel qtyLabel = new JLabel("Quantity: " + playerItem.quantity);
            qtyLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            qtyLabel.setForeground(Color.YELLOW);

            JLabel descLabel = new JLabel("<html><div WIDTH=300>" + detail.about + "</div></html>");
            descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            descLabel.setForeground(Color.LIGHT_GRAY);

            textPanel.add(nameLabel);
            textPanel.add(qtyLabel);
            textPanel.add(Box.createVerticalStrut(5));
            textPanel.add(descLabel);

            // Equip Button
            JButton equipBtn = new JButton("EQUIP");
            equipBtn.setFont(new Font("Arial", Font.BOLD, 18));
            equipBtn.setBackground(new Color(0, 140, 0));
            equipBtn.setForeground(Color.WHITE);
            equipBtn.setPreferredSize(new Dimension(120, 60));

            equipBtn.addActionListener(e -> {
                int itemId = playerItem.id;

                // Find empty slot
                int emptySlot = -1;
                for (int i = 0; i < 3; i++) {
                    if (equippedItems[i] == 0) {
                        emptySlot = i;
                        break;
                    }
                }

                if (emptySlot == -1) {
                    JOptionPane.showMessageDialog(this, "All equipment slots are full!", 
                        "No Space", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Prevent duplicate equip
                for (int id : equippedItems) {
                    if (id == itemId) {
                        JOptionPane.showMessageDialog(this, "This item is already equipped!", 
                            "Duplicate", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }

                // Equip
                equippedItems[emptySlot] = itemId;

                // Consume one (shoe is consumed here too)
                playerItem.quantity -= 1;
                if (playerItem.quantity <= 0) {
                    items.remove(playerItem);
                }

                updateEquippedDisplay();
                refreshInventory();

                JOptionPane.showMessageDialog(this,
                    detail.name + " equipped to Slot " + (emptySlot + 1) + "!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            });

            itemPanel.add(iconLabel);
            itemPanel.add(Box.createHorizontalStrut(20));
            itemPanel.add(textPanel);
            itemPanel.add(Box.createHorizontalGlue());
            itemPanel.add(equipBtn);
            itemPanel.add(Box.createHorizontalStrut(20));

            centerPanel.add(itemPanel);
            centerPanel.add(Box.createVerticalStrut(15));
        }

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    public void updateEquippedDisplay() {
        for (int i = 0; i < 3; i++) {
            int itemId = equippedItems[i];
            if (itemId == 0) {
                slotLabels[i].setText("<html><center><b>EMPTY</b><br>Slot " + (i+1) + "</center></html>");
                slotLabels[i].setIcon(null);
                unequipButtons[i].setEnabled(false);
            } else {
                itemsdetail item = itemCache.get(itemId);
                if (item != null) {
                    Image scaled = item.image.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                    slotLabels[i].setIcon(new ImageIcon(scaled));
                    slotLabels[i].setText("<html><center><b>" + item.name.toUpperCase() + "</b><br>Slot " + (i+1) + "</center></html>");
                    unequipButtons[i].setEnabled(true);
                }
            }
        }
    }

    private void unequipItem(int slotIndex) {
        int itemId = equippedItems[slotIndex];
        if (itemId == 0) return;

        // Shoe is one-time use → do NOT return to inventory
        if (itemId != 6) {
            boolean found = false;
            for (PlayerItem pi : items) {
                if (pi.id == itemId) {
                    pi.quantity++;
                    found = true;
                    break;
                }
            }
            if (!found) {
                items.add(new PlayerItem(itemId, 1));
            }
        }

        equippedItems[slotIndex] = 0;

        updateEquippedDisplay();
        refreshInventory();

        JOptionPane.showMessageDialog(this, "Item unequipped!", "Unequipped", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            openmenu();
        }
    }

    public void openmenu() {
        gamemenu gamemenu = new gamemenu(cardLayout, cardPanel, gc);
        cardPanel.add(gamemenu, "gamemenu");
        cardLayout.show(cardPanel, "gamemenu");
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            refreshInventory();
            updateEquippedDisplay();
        }
    }
}