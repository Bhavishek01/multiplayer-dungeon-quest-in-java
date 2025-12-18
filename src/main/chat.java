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

    public chat(CardLayout cl, JPanel cp, gamehandler gh) 
    {
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
        add(chatDisplay, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBackground(new Color(0, 0, 0, 100));
        inputPanel.setBorder(new EmptyBorder(20, 50, 30, 50));

        chatInput = new JTextField();
        chatInput.setFont(new Font("Arial", Font.PLAIN, 18));
        chatInput.addActionListener(e-> send());
        inputPanel.add(chatInput, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setBackground(new Color(50, 50, 50));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> close());
        inputPanel.add(backButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        // Key listener to close
        setFocusable(true);
    }

    public void addMessage( String message) 
    {
        System.out.println("added" + message);
        SwingUtilities.invokeLater(() -> {
            chatHistory.add(message);
            refreshDisplay();
        });
    }

    public void refreshDisplay() {
        StringBuilder sb = new StringBuilder();
        for (String msg : chatHistory) 
        {
            sb.append(msg).append("\n");
        }
        System.out.println("displaying " + sb);
        chatDisplay.setText(sb.toString());
        chatDisplay.revalidate();
        chatDisplay.repaint();
        scrollToBottom();
    }

    public void scrollToBottom() 
    {
    chatDisplay.setCaretPosition(chatDisplay.getDocument().getLength());
    }

    public void close() 
    {
        gh.resumed = false;
        gh.ischatting = false;
        cardLayout.show(cardPanel, "gamehandler");
        gh.requestFocusInWindow();  
        chatInput.setText("");
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