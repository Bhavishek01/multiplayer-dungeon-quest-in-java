package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import background.loginphoto;

public class newplayer extends loginphoto implements ActionListener 
{
    private Font arial_40;
    private JLabel pl, pid, er;
    private JTextField playername;
    private JButton submitButton, start,back;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private gameclient gc;

    int id;
    String pname, playerid;

    public newplayer(CardLayout cl, JPanel cp, gameclient client) {
        this.cardLayout = cl;
        this.cardPanel = cp;
        this.gc = client;

        arial_40 = new Font("Arial", Font.BOLD, 25);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(Box.createVerticalGlue());

        pl = new JLabel("Enter Your Name");
        pl.setFont(arial_40);
        pl.setForeground(Color.WHITE);
        pl.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(pl);

        add(Box.createRigidArea(new Dimension(0, 20)));

        playername = new JTextField();
        playername.setFont(new Font("Arial", Font.PLAIN, 20));
        playername.setForeground(Color.black);
        playername.setBackground(Color.white);
        Dimension labelSize = pl.getPreferredSize();
        playername.setMaximumSize(new Dimension(labelSize.width, 40));
        playername.setAlignmentX(Component.CENTER_ALIGNMENT);
        playername.requestFocusInWindow();
        playername.addActionListener(e -> enter());
        add(playername);

        add(Box.createRigidArea(new Dimension(0, 20)));

        pid = new JLabel();
        pid.setFont(arial_40);
        pid.setForeground(Color.WHITE);
        pid.setAlignmentX(Component.CENTER_ALIGNMENT);
        pid.setVisible(false);
        add(pid);

        er = new JLabel("Name already exists");
        er.setFont(arial_40);
        er.setForeground(Color.WHITE);
        er.setAlignmentX(Component.CENTER_ALIGNMENT);
        er.setVisible(false);
        add(er);

        add(Box.createRigidArea(new Dimension(0, 20)));

        start = new JButton("Start Game");
        start.setFont(arial_40);
        start.setVisible(false);
        start.setBorderPainted(true);
        start.setBackground(Color.BLACK);
        start.setForeground(Color.WHITE);
        start.setOpaque(false);
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.addActionListener(this);
        add(start);

        add(Box.createVerticalGlue());

        submitButton = new JButton("Enter");
        submitButton.setFont(arial_40);
        submitButton.setBorderPainted(true);
        submitButton.setBackground(Color.BLACK);
        submitButton.setForeground(Color.WHITE);
        submitButton.setOpaque(false);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(this);
        add(submitButton);
        
        add(Box.createRigidArea(new Dimension(0, 20)));

        back = new JButton("Back");
        back.setFont(arial_40);
        back.setBorderPainted(true);
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.setOpaque(false);
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.addActionListener(this);
        add(back);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) 
            {
            enter();
        }
         else if (e.getSource() == start) {
            gamemenu gamemenu = new gamemenu(cardLayout, cardPanel, gc);
            cardPanel.add(gamemenu, "gamemenu");
            cardLayout.show(cardPanel, "gamemenu"); // Changed to "gamepannel" assuming this is correct
        }
         else if (e.getSource() == back) {
            cardLayout.show(cardPanel, "loginornew");
        }
    }

    public void enter()
    {
        pname = playername.getText().trim();

            if (!pname.isEmpty()) 
                {
                    gc.send("REGISTER|"+ pname);
                    gc.send(pname); 
                    try 
                    {
                        Thread.sleep(400);  // Pause for 1000 ms = .1 second
                    } catch (InterruptedException ae) {
                    ae.printStackTrace();
                    }

                    if (!gc.nameExists()) 
                        {
                        playername.setText("");
                        er.setVisible(true);
                        }
                        else 
                        {
                        submitButton.setVisible(false);
                        remove(submitButton);
                        er.setVisible(false);
                        remove(er);
                        pid.setText("Your Player ID is " + gc.id);
                        pid.setVisible(true);
                        start.setVisible(true);
                        back.setVisible(false);
                        revalidate(); // Update the layout
                        repaint();    // Redraw the panel
                    }
                }
    }
}