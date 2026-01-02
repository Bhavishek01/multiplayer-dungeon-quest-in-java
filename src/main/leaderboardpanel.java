// New file: main/leaderboardpanel.java (or leaderboard.java)
package main;

import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

import background.loginphoto;

public class leaderboardpanel extends loginphoto implements ActionListener {
    private JButton back;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private gameclient gc;
    private JPanel contentPanel;
    private JLabel titleLabel;

    public leaderboardpanel(CardLayout cardLayout, JPanel cardPanel, gameclient gc) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        this.gc = gc;

        setLayout(new BorderLayout());
        setOpaque(true);

        // Title
        titleLabel = new JLabel("Leaderboard - Top 10 Wins", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(false);
        add(titleLabel, BorderLayout.NORTH);

        // Content for rankings
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBackground(new Color(0, 0, 0, 0));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        add(scrollPane, BorderLayout.CENTER);

        // Back button
        back = new JButton("Back to Menu");
        back.setFont(new Font("Arial", Font.BOLD, 24));
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.setOpaque(true);
        back.addActionListener(this);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(back);
        add(bottomPanel, BorderLayout.SOUTH);

        // Request leaderboard data
        gc.send("LEADERBOARD");

        // Poll for response (every 200ms for 2 seconds max)
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int attempts = 0;
            @Override
            public void run() {
                attempts++;
                SwingUtilities.invokeLater(() -> {
                    if (gc.topPlayers != null && !gc.topPlayers.isEmpty()) {
                        displayLeaderboard(gc.topPlayers);
                        gc.topPlayers = null;  // Clear after display
                        timer.cancel();
                    } else if (attempts > 10) {  // Timeout after ~2s
                        displayLeaderboard(java.util.Collections.emptyList());
                        timer.cancel();
                    }
                });
            }
        }, 200, 200);
    }

    private void displayLeaderboard(java.util.List<PlayerRanking> topPlayers) {
        contentPanel.removeAll();
        if (topPlayers.isEmpty()) {
            JLabel noData = new JLabel("No data available or failed to load.");
            noData.setFont(new Font("Arial", Font.PLAIN, 20));
            noData.setForeground(Color.YELLOW);
            noData.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(noData);
        } else {
            for (int i = 0; i < topPlayers.size(); i++) {
                PlayerRanking pr = topPlayers.get(i);
                JLabel rankLabel = new JLabel((i + 1) + ". " + pr.name + " - " + pr.wins + " Wins");
                rankLabel.setFont(new Font("Arial", i == 0 ? Font.BOLD : Font.PLAIN, 22));
                rankLabel.setForeground(i == 0 ? Color.blue : Color.WHITE);
                rankLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                contentPanel.add(Box.createVerticalStrut(10));
                contentPanel.add(rankLabel);
            }
        }
        contentPanel.revalidate();
        contentPanel.repaint();
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            cardLayout.show(cardPanel, "gamemenu");
        }
    }
}