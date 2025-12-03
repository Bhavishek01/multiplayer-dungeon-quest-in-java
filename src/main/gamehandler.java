package main;

import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import background.backgroundmanager;
import gameobj.player;

public class gamehandler extends gamepannel implements Runnable
{
    int fps = 60;
    input key = new input();
    CardLayout cardLayout;
    JPanel cardPanel;
    GameClient gc;
    public volatile boolean ispaused = false;  // VOLATILE for thread safety
    public volatile boolean paused = false;    // VOLATILE, removed isresumed

    public colisiondetection cd = new colisiondetection(this);
    public player p1 = new player(key,cd,this);
    public backgroundmanager bgm = new backgroundmanager(p1);
    
    Thread gameThread;

    public gamehandler(CardLayout cardlayout, JPanel cardpanel, GameClient gc) {
        this.cardLayout = cardlayout;
        this.cardPanel = cardpanel;
        this.gc = gc;
        this.addKeyListener(key);
        this.setFocusable(true);  // NEW
        this.requestFocusInWindow();  // NEW
    }

    public void startgame() {
        if (gameThread == null) { // Only start if no thread exists
            System.out.println("playing state");
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void run() {
        double interval = 1000000000 / fps;
        double next = System.nanoTime() + interval;

        while (gameThread != null) {
            if (ispaused && !paused) {
                pause();
                paused = true;
            }

            if (!ispaused) {
                p1.update();
                repaint();
            }
            // NOTE: If you have multiplayer network updates (e.g., gc.pollServer()), add them here outside the if(!ispaused) so they continue in background.

            try {
                double remainingtime = next - System.nanoTime();
                remainingtime = remainingtime / 1000000;
                if (remainingtime < 0) {
                    remainingtime = 0;
                }
                Thread.sleep((long) remainingtime);
                next += interval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            
        }
    }
}
    


    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        bgm.draw(g2);
        p1.draw(g2);
        g2.dispose();
    }

    public void pause() {
        System.out.println("paused");
        pausemenu pause = new pausemenu(cardLayout, cardPanel, this);
        cardPanel.add(pause, "pausemenu");
        cardLayout.show(cardPanel, "pausemenu");
        pause.requestFocusInWindow();
    }
}
