package main;


import java.awt.Graphics;
import java.awt.Graphics2D;


import background.backgroundmanager;
import gameobj.player;

public class gamehandler extends gamepannel implements Runnable
{
    int fps = 60;
    input key = new input();

    public colisiondetection cd = new colisiondetection(this);
    public player p1 = new player(key,cd);
    public backgroundmanager bgm = new backgroundmanager(p1);
    Thread gameThread;

    public gamehandler()
    {
        this.addKeyListener(key);
    }

    public void startgame() {
        if (gameThread == null) { // Only start if no thread exists
            System.out.println("start game");
            gameThread = new Thread(this);
            gameThread.start();
        }
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


    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        bgm.draw(g2);
        p1.draw(g2);
        g2.dispose();
    }
}
