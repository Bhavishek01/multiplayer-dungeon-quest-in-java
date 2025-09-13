package main;

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
import javax.swing.JTextField;

public class login extends JPanel implements ActionListener {

    private gamepannel gp = new gamepannel();
    private Font arial_40;
    private JTextField playerIdField;
    private JButton submitButton;

    public login() {
        // Set up panel properties
        this.setPreferredSize(new Dimension(gp.base, gp.height));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        // Fixed font: Use BOLD
        arial_40 = new Font("Arial", Font.BOLD, 25);

        // Use BoxLayout for vertical stacking
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Add flexible space above for vertical centering
        add(Box.createVerticalGlue());

        // Prompt label
        JLabel promptLabel = new JLabel("Enter Player ID");
        promptLabel.setFont(arial_40);
        promptLabel.setForeground(Color.WHITE);
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Right-align prompt
        add(promptLabel);

        // Fixed gap between prompt and input
        add(Box.createRigidArea(new Dimension(0, 20)));

        // Input field: Compact size matching label width
        

        playerIdField = new JTextField(); 
        playerIdField.setBounds(100, 100, 100, 50);
        playerIdField.setFont(new Font("Arial", Font.PLAIN, 20)); // Smaller font for input
        playerIdField.setForeground(Color.WHITE);
        playerIdField.setBackground(Color.DARK_GRAY);
        Dimension labelSize = promptLabel.getPreferredSize();
        playerIdField.setPreferredSize(new Dimension(labelSize.width, labelSize.height));
        playerIdField.setMaximumSize(new Dimension(labelSize.width, labelSize.height));
        playerIdField.setAlignmentX(Component.CENTER_ALIGNMENT); 
        playerIdField.requestFocusInWindow(); // Auto-focus for user convenience
        add(playerIdField);


        // Add flexible space below for vertical centering
        add(Box.createVerticalGlue());

        // Submit button
        submitButton = new JButton("Submit");
        submitButton.setFont(arial_40);
        submitButton.setBorderPainted(false);
        submitButton.setBackground(getBackground());
        submitButton.setForeground(Color.WHITE);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Right-align button
        submitButton.setAlignmentY(Component.BOTTOM_ALIGNMENT); // Right-align button
        submitButton.addActionListener(this);
  
        add(submitButton);

        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }
}