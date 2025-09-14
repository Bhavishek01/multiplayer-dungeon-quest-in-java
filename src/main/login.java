package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import background.background;

public class login extends background implements ActionListener {
    private Font arial_40;
    JLabel pl;
    private JTextField playerIdField;
    private JButton submitButton;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private playercheck playerCheck ;
    private gamepannel gp; // Store gamepannel reference

    public login(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.playerCheck = new playercheck();

        gp = new gamepannel();

        // Set up panel properties
        this.setPreferredSize(new Dimension(gp.base, gp.height)); // Match gamepannel dimensions
        this.setBackground(Color.BLACK);
        this.setOpaque(true);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        arial_40 = new Font("Arial", Font.BOLD, 25);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(Box.createVerticalGlue());

        JLabel promptLabel = new JLabel("Enter Player ID");
        promptLabel.setFont(arial_40);
        promptLabel.setForeground(Color.WHITE);
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(promptLabel);

        add(Box.createRigidArea(new Dimension(0, 20)));

        playerIdField = new JTextField();
        playerIdField.setFont(new Font("Arial", Font.PLAIN, 20));
        playerIdField.setForeground(Color.black);
        playerIdField.setBackground(Color.white);
        Dimension labelSize = promptLabel.getPreferredSize();
        playerIdField.setMaximumSize(new Dimension(labelSize.width, 40));
        playerIdField.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerIdField.requestFocusInWindow();
        add(playerIdField);

        add(Box.createRigidArea(new Dimension(0, 20)));

        pl = new JLabel("Invalid Player ID");
        pl.setFont(arial_40);
        pl.setForeground(Color.WHITE);
        pl.setAlignmentX(Component.CENTER_ALIGNMENT);
        pl.setVisible(false);
        add(pl);

        add(Box.createVerticalGlue());

        submitButton = new JButton("Submit");
        submitButton.setFont(arial_40);
        submitButton.setBorderPainted(true);
        submitButton.setBackground(Color.BLACK);
        submitButton.setForeground(Color.WHITE);
        submitButton.setOpaque(false);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(this);
        add(submitButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String playerId = playerIdField.getText().trim();
            if (!playerId.isEmpty()) {
                try {
                    playerCheck.setPlayerId(playerId);
                    playerCheck.checkid();
                    if (playerCheck.isExists()) {
                        playerCheck.setGameEntered(true);
                        cardLayout.show(cardPanel, "gamemenu");
                    } else {
                        playerIdField.setText(""); // Clear field
                        playerIdField.setBackground(Color.RED); // Indicate error
                        pl.setVisible(true);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    playerIdField.setBackground(Color.RED); // Indicate error
                    pl.setVisible(true);
                }
            }
        }
    }
}