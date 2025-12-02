package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class pausemenu extends JPanel implements ActionListener {
    private start st;
    private JButton resume, backToMenu, exit;

    public pausemenu(start st) {
        this.gh = gh;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        JLabel title = new JLabel("Paused");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalGlue());
        add(title);
        add(Box.createRigidArea(new Dimension(0, 20)));

        resume = new JButton("Resume");
        resume.setFont(new Font("Arial", Font.BOLD, 20));
        resume.setAlignmentX(Component.CENTER_ALIGNMENT);
        resume.addActionListener(this);
        add(resume);
        add(Box.createRigidArea(new Dimension(0, 20)));

        backToMenu = new JButton("Back to Menu");
        backToMenu.setFont(new Font("Arial", Font.BOLD, 20));
        backToMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToMenu.addActionListener(this);
        add(backToMenu);
        add(Box.createRigidArea(new Dimension(0, 20)));

        exit = new JButton("Exit");
        exit.setFont(new Font("Arial", Font.BOLD, 20));
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.addActionListener(this);
        add(exit);
        add(Box.createVerticalGlue());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 150));  // Semi-transparent black
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resume) {
            gh.isPaused = false;
            setVisible(false);
        } else if (e.getSource() == backToMenu) {
            // Switch back to gamemenu via CardLayout
            Container cardPanel = gh.getParent();
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "gamemenu");
            // Stop game thread
            gh.gameThread = null;
        } else if (e.getSource() == exit) {
            System.exit(0);
        }
    }
}