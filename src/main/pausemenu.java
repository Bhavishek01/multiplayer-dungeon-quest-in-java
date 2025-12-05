package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import background.pausebackground;

public class pausemenu extends pausebackground implements ActionListener {
    private JButton resume, backToMenu, exit;
    private Font arial_40;
    CardLayout cardLayout;
    JPanel cardPanel;
    public gamehandler gh;
    gameclient gc;

    public pausemenu( CardLayout cl, JPanel cp,gamehandler gh) {

        this.cardLayout = cl;
        this.cardPanel = cp;
        this.gh = gh;
        this.gc = gh.gc;

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    resume();
                }
            }
        }); 

        
        arial_40 = new Font("Arial", Font.BOLD, 25);

        // Use BoxLayout for vertical stacking
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel promptLabel = new JLabel("Paused");
        promptLabel.setFont(new Font("Arial", Font.BOLD, 50));
        promptLabel.setForeground(Color.WHITE);
        promptLabel.setBackground(Color.BLACK);
        promptLabel.setAlignmentY(Component.TOP_ALIGNMENT);
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center horizontally
        add(promptLabel);

        // Add flexible space above for vertical centering
        add(Box.createVerticalGlue());
        
        // resume button (stacked vertically, right-aligned)
        resume = new JButton("Resume");
        resume.setFont(arial_40);
        resume.setBorderPainted(true);
        resume.setOpaque(false);
        resume.setForeground(Color.white);
        resume.setBackground(Color.white);
        resume.setAlignmentX(Component.CENTER_ALIGNMENT);  // Right-align horizontally
        resume.addActionListener(this);
        add(resume);

        add(Box.createRigidArea(new Dimension(0, 20)));

        // New Player button (below resume, right-aligned)
        backToMenu = new JButton("Back To Menu");
        backToMenu.setFont(arial_40);
        backToMenu.setBorderPainted(true);
        backToMenu.setOpaque(false);
        backToMenu.setForeground(Color.white);
        backToMenu.setBackground(Color.white);
        backToMenu.setAlignmentX(Component.CENTER_ALIGNMENT);  // Right-align horizontally
        backToMenu.addActionListener(this);
        add(backToMenu);

        add(Box.createRigidArea(new Dimension(0, 20)));

        exit = new JButton("Exit");
        exit.setFont(arial_40);
        exit.setBorderPainted(true);
        exit.setBackground(Color.BLACK);
        exit.setForeground(Color.WHITE);
        exit.setOpaque(false);
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.addActionListener(this);
        add(exit);

        // Add flexible space below for vertical centering
        add(Box.createVerticalGlue());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == resume )
        {
            resume();
        }
        else if (e.getSource() == backToMenu) 
        {
            gamemenu gamemenu = new gamemenu(cardLayout, cardPanel, gc);
            cardPanel.add(gamemenu, "gamemenu");
            cardLayout.show(cardPanel, "gamemenu");
        }
        else if (e.getSource() == exit) 
        {
            System.exit(0);
        }
    }

    public void resume() 
    {
        gh.ispaused = false;
        gh.paused = false;  // Simplified, removed isresumed
        cardLayout.show(cardPanel, "gamehandler");
        gh.requestFocusInWindow();  // NEW: Ensure gamehandler gets focus for inputs
    }
}



