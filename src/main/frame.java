package main;


import javax.swing.*;

public class frame{

    public static void main(String[] args) {
        
        JFrame f = new JFrame("Doungen quest");
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pannel pannel = new pannel();

        f.add(pannel);
        f.pack();

        f.setVisible(true);
        f.setLayout(null);
        f.setLocationRelativeTo(null);

        pannel.startgame();

        
    }
}
