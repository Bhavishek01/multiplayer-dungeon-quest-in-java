package main;

import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import background.backgroundmanager;
import gameobj.OtherPlayer;
import gameobj.player;

public class gamehandler extends gamepannel implements Runnable
{
    int fps = 60;
    input key = new input();
    CardLayout cardLayout;
    JPanel cardPanel;

    public gameclient gc;
    public Map<String, OtherPlayer> otherPlayers = new HashMap<>();

    public volatile boolean ispaused = false;  // VOLATILE for thread safety
    public volatile boolean paused = false;    // VOLATILE, removed isresumed

    public colisiondetection cd = new colisiondetection(this);
    public player p1 = new player(key,cd,this);
    public backgroundmanager bgm = new backgroundmanager(p1);
    
    Thread gameThread;

    public gamehandler(CardLayout cardlayout, JPanel cardpanel, gameclient gc) {
        this.cardLayout = cardlayout;
        this.cardPanel = cardpanel;
        this.gc = gc;
        gc.setGameHandler(this);
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

    public void updateWorld(String data) {
    if (!data.startsWith("WORLD|")) return;
    String[] parts = data.substring(6).split("\\|");
    HashMap<String, OtherPlayer> temp = new HashMap<>();

    for (int i = 0; i < parts.length; i += 5) {
        if (i + 4 >= parts.length) break;
        String id = parts[i];
        String name = parts[i+1];
        int x = Integer.parseInt(parts[i+2]);
        int y = Integer.parseInt(parts[i+3]);
        String dir = parts[i+4];

        if (id.equals(gc.id)) continue; // skip self

        OtherPlayer op = temp.get(id);
        if (op == null) {
            op = new OtherPlayer(name);
            op.up1 = p1.up1; op.up2 = p1.up2;
            op.down1 = p1.down1; op.down2 = p1.down2;
            op.left1 = p1.left1; op.left2 = p1.left2;
            op.right1 = p1.right1; op.right2 = p1.right2;
            op.idle1 = p1.idle1;
        }
        op.entity_map_X = x;
        op.entity_map_Y = y;
        op.direction = dir;
        op.count++;
        temp.put(id, op);
    }
    otherPlayers = temp;
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

        Graphics2D g2 = (Graphics2D) g;

        // 1. Draw background/tiles (world)
        bgm.draw(g2);

        // 2. Draw ALL other players (they appear behind you)
        for (OtherPlayer op : otherPlayers.values()) {
            int screenX = op.entity_map_X - p1.entity_map_X + p1.centerx;
            int screenY = op.entity_map_Y - p1.entity_map_Y + p1.centery;
            op.draw(g2, screenX, screenY);
        }

        // 3. Draw local player ON TOP
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
    
    public void updateOtherPlayers(String otherplayers) {

        String[] parts = otherplayers.split("\\|");

        Map<String, OtherPlayer> newPlayers = new HashMap<>();

        for (int i = 0; i + 4 < parts.length; i += 5) {
            String id = parts[i + 1];
            String name = parts[i + 2];
            int x = Integer.parseInt(parts[i + 3]);
            int y = Integer.parseInt(parts[i + 4]);
            String dir = parts[i + 5];

            if (id.equals(gc.id)) continue;

            // Get existing player or create new one
            OtherPlayer op = otherPlayers.get(id);
            if (op == null) {
                op = new OtherPlayer(name);
                // Copy sprite references from local player (fixes blank sprites)
                op.up1 = p1.up1; op.up2 = p1.up2;
                op.down1 = p1.down1; op.down2 = p1.down2;
                op.left1 = p1.left1; op.left2 = p1.left2;
                op.right1 = p1.right1; op.right2 = p1.right2;
                op.idle1 = p1.idle1; op.idle2 = p1.idle2;
            }

            op.entity_map_X = x;
            op.entity_map_Y = y;
            op.direction = dir;
            op.count++; // for animation

            newPlayers.put(id, op);
            }

            // Replace old list (removes disconnected players)
            otherPlayers.clear();
            otherPlayers.putAll(newPlayers);
    }
}
