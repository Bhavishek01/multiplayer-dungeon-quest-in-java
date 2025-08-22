package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import background.backgroundmanager;
import gameobj.player;

public class gamepannel extends JPanel implements Runnable{

    int tilesize = 16;
    int scale = 3;

    public int tiles = tilesize * scale;
    int maxcol = 20;
    int maxrow = 16;

    int height = tiles * maxrow;
    int base = tiles *maxcol;

    int mapheight = tiles * 50;
    int mapwidth = tiles * 50;

    int fps = 60;

    input key = new input();
    backgroundmanager bgm = new backgroundmanager(this);
    player p1 = new player(this, key);
    Thread gameThread;


    

    public gamepannel()
    {
    this.setPreferredSize(new Dimension(base,height));
    this.setBackground(Color.BLACK);
    this.setDoubleBuffered(true);
    this.addKeyListener(key);
    this.setFocusable(true);

    }


    public void startgame()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() 
    {
        double interval = 1000000000/fps;
        double next = System.nanoTime() + interval;

        while (gameThread != null) 
        {
            
            p1.update();

            repaint();

            try {
                double remainingtime = next - System.nanoTime();
                remainingtime = remainingtime/1000000;
                if (remainingtime <0)
                {
                    remainingtime = 0;
                }
                Thread.sleep((long) remainingtime);
                next += interval;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;


        bgm.draw(g2);
        p1.draw(g2);
        g2.dispose();
    }

}
