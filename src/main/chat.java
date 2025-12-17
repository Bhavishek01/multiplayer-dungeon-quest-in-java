// chat.java
package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import background.pausebackground;

import java.util.ArrayList;
import java.util.List;

public class chat extends pausebackground {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private gamehandler gh;
    private JTextArea chatDisplay;
    private JTextField chatInput;
    private List<String> chatHistory = new ArrayList<>();

    public chat(CardLayout cl, JPanel cp, gamehandler gh) {
        this.cardLayout = cl;
        this.cardPanel = cp;
        this.gh = gh;

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    close();
                }
            }
        });

        setBackground(new Color(0, 0, 0, 180)); // semi-transparent
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Global Chat", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        // Chat display
        chatDisplay = new JTextArea();
        chatDisplay.setEditable(false);
        chatDisplay.setBackground(new Color(20, 20, 20));
        chatDisplay.setForeground(Color.WHITE);
        chatDisplay.setFont(new Font("Arial", Font.PLAIN, 16));
        chatDisplay.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(chatDisplay);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBackground(new Color(0, 0, 0, 100));
        inputPanel.setBorder(new EmptyBorder(20, 50, 30, 50));

        chatInput = new JTextField();
        chatInput.setFont(new Font("Arial", Font.PLAIN, 18));
        chatInput.addActionListener(e-> send());
        inputPanel.add(chatInput, BorderLayout.CENTER);

        add(inputPanel, BorderLayout.SOUTH);

        // Key listener to close
        setFocusable(true);
    }

    public void addMessage(String playerName, String message) {
        chatHistory.add(playerName + ": " + message);
        if (chatHistory.size() > 200) chatHistory.remove(0);

        StringBuilder sb = new StringBuilder();
        for (String msg : chatHistory) sb.append(msg).append("\n");
        chatDisplay.setText(sb.toString());
        chatDisplay.setCaretPosition(chatDisplay.getDocument().getLength());
    }

    public void close() {
        System.out.println("close");
        gh.ischatting = false;
        gh.paused = false;  // Simplified, removed isresumed
        cardLayout.show(cardPanel, "gamehandler");
        gh.requestFocusInWindow();
    }

    public void send()
    {
        String text = chatInput.getText().trim();
        if (!text.isEmpty()) {
            gh.gc.send("CHAT|" + text);
            chatInput.setText("");
        }
    }
}