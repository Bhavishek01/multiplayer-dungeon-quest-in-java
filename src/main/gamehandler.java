package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.util.Iterator;

import background.backgroundmanager;
import environment.environment_manager;
import gameplayers.OtherPlayer;
import gameplayers.player;
import items.itemsdetail;


public class gamehandler extends gamepannel implements Runnable
{
    int fps = 60;
    input key = new input();
    CardLayout cardLayout;
    JPanel cardPanel;
    public pausemenu pause;
    public chat chat;
    public gameinventory gameinventory;
    public equipped equipmentManager;

        // === EQUIPPED ITEMS HUD (Top-Right) ===
    public BufferedImage[] equippedHudIcons = new BufferedImage[3];
    public String[] equippedHudNames = new String[3];
    public long shoeStartTime = 0;
    public static final long SHOE_DURATION = 60_000; // 60 seconds
    public BufferedImage shoeHudIcon; // For timer display

    public List<Projectile> localProjectiles = new ArrayList<>();
    public List<Projectile> clientProjectiles = new ArrayList<>();


    public gameclient gc;
    public Map<String, OtherPlayer> otherPlayers = new HashMap<>();

    public volatile boolean ispaused = false;  // VOLATILE for thread safet
    public volatile boolean resumed = false; 

    public volatile boolean ischatting = false;  // VOLATILE for thread safety
    public volatile boolean isinventory_open = false;  // VOLATILE for thread safety

    public volatile boolean light_on = true;  // VOLATILE for thread safety
    public volatile boolean light_use = false;  // VOLATILE for thread safety
    

        // VOLATILE, removed isresumed

    public colisiondetection cd = new colisiondetection(this);
    public player p1 = new player(key,cd,this);
    public backgroundmanager bgm = new backgroundmanager(p1);
    public itemsdetail gameitems[] = new itemsdetail[9];
    public itemspawn itemspawn = new itemspawn(this);
    public environment_manager environmentManager = new environment_manager(this);
    BufferedImage projectileImage;
    
    Thread gameThread;

    public gamehandler(CardLayout cardlayout, JPanel cardpanel, gameclient gc) {
        
        this.cardLayout = cardlayout;
        this.cardPanel = cardpanel;
        this.gc = gc;
        chat = new chat(cardLayout, cardPanel, this);
        cardPanel.add(chat, "chat");
        gameinventory = new gameinventory(cardLayout, cardPanel, this);
        cardPanel.add(gameinventory, "gameinventory");
        cardPanel.add(chat, "chat");
        gc.setGameHandler(this);
        this.addKeyListener(key);
       

        addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() != MouseEvent.BUTTON1) return;

            int screenX = e.getX();
            int screenY = e.getY();

            double worldX = p1.entity_map_X + screenX - p1.centerx;
            double worldY = p1.entity_map_Y + screenY - p1.centery;

            // Optional: snap to tile center (remove if you want free aim)
            // int targetMapX = (int)(Math.round(worldX / tiles) * tiles + tiles / 2.0);
            // int targetMapY = (int)(Math.round(worldY / tiles) * tiles + tiles / 2.0);

            double startX = p1.entity_map_X + 24; // player center
            double startY = p1.entity_map_Y + 24;

            // === ADD LOCAL PROJECTILE FOR INSTANT VISUAL ===
            Projectile localProj = new Projectile(startX, startY, worldX, worldY);
            synchronized (localProjectiles) {
                localProjectiles.add(localProj);
            }

            // === SEND TO SERVER FOR OTHERS TO SEE ===
            gc.send("SHOOT|" + worldX + "|" + worldY);
        }
    });
        
        this.setFocusable(true);  // NEW
        this.requestFocusInWindow();  // NEW

        try 
        {
            projectileImage = ImageIO.read(new File("resource/object/bigrock.png"));
            shoeHudIcon = ImageIO.read(new File("resource/items/fast_shoe.png"));
        } catch (IOException e) {
            e.printStackTrace();
            shoeHudIcon = null;
        }

        gc.send("ENTER_GAME");
        environmentManager.setup();
    }

    public void startgame() {
        if (gameThread == null) { // Only start if no thread exists
            equipmentManager = new equipped( gc.equippedItems,this);
            System.out.println("playing state");
            gameThread = new Thread(this);
            gameThread.start();
            equipmentManager.apply_effects();
        }
    }

    @Override
    public void run() {
        double interval = 1000000000 / fps;
        double next = System.nanoTime() + interval;

        while (gameThread != null) 
        {
            if (!resumed && ispaused) {
                resumed = true;
                System.out.println("paused");
                pause = new pausemenu(cardLayout, cardPanel, this);
                cardPanel.add(pause, "pausemenu");
                cardLayout.show(cardPanel, "pausemenu");
                pause.requestFocusInWindow();
            }

            else if (!resumed && ischatting)
            {
                resumed = true;
                
                cardLayout.show(cardPanel, "chat");
                chat.requestFocusInWindow(); 
            }

            else if (!resumed && isinventory_open) 
            {
                resumed = true;
                System.out.println("inventory open");
                cardLayout.show(cardPanel, "gameinventory");
                gameinventory.refreshInventory();
                gameinventory.requestFocusInWindow(); 
            }

            else if (!ispaused && !ischatting && !isinventory_open) {
                p1.update();
                synchronized (localProjectiles) {
                Iterator<Projectile> it = localProjectiles.iterator();
                while (it.hasNext()) {
                    Projectile p = it.next();
                    p.update();
                    if (!p.active) {
                        it.remove(); // safe removal during iteration
                    }
                }
            }
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

        // draw items // for local
        for(int i = 0; i<gameitems.length; i++)
        {
            if(gameitems[i] != null)
            gameitems[i].draw(g2, this);
        }

        // 2. Draw ALL other players (they appear behind you)
        for (OtherPlayer op : otherPlayers.values()) {
            int screenX = op.entity_map_X - p1.entity_map_X + p1.centerx;
            int screenY = op.entity_map_Y - p1.entity_map_Y + p1.centery;
            op.draw(g2, screenX, screenY);
        }
        // 3. Draw local player ON TOP
        p1.draw(g2);

        if(light_on && !light_use)
        {
            environmentManager.draw(g2);
        }
        
        // Draw local projectiles (your own shots)
        synchronized (localProjectiles) {
            for (Projectile proj : localProjectiles) {
                if (proj.active) {
                    int screenX = proj.getScreenX(this);
                    int screenY = proj.getScreenY(this);
                    if (screenX > -48 && screenX < base && screenY > -48 && screenY < height) {
                        g2.drawImage(projectileImage, screenX - 8, screenY - 8, 16, 16, null);
                    }
                }
            }
        }

        // Draw other players' projectiles (received from server)
        synchronized (clientProjectiles) {
            for (Projectile proj : clientProjectiles) {
                if (proj.active) {
                    int screenX = proj.getScreenX(this);
                    int screenY = proj.getScreenY(this);
                    if (screenX > -48 && screenX < base && screenY > -48 && screenY < height) {
                        g2.drawImage(projectileImage, screenX - 8, screenY - 8, 16, 16, null);
                    }
                }
            }
        }

        // ==================== EQUIPPED ITEMS HUD (Top-Right) ====================
    int hudX = base - 210;        // Adjust if needed
    int hudY = 5;
    int slotSize = 64;
    int spacing = 9;

    boolean hasAnyEquipped = false;
    int shoeSlotIndex = -1; // Track which slot has the shoe

    for (int i = 0; i < 3; i++) {
        if (equippedHudIcons[i] != null) {
            hasAnyEquipped = true;

                shoeSlotIndex = i;
            
        }
    }

    if (hasAnyEquipped || shoeStartTime > 0) {

        // Main background
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(hudX , hudY , 220, 64, 25, 25);

        // Draw 3 slots horizontally
        for (int i = 0; i < 3; i++) {
            int slotX = hudX + (i * (slotSize + spacing));
            int slotY = hudY ;

            // Slot background
            g2.setColor(new Color(30, 30, 60, 220));
            g2.fillRoundRect(slotX, slotY, slotSize, slotSize, 15, 15);
            g2.setColor(Color.CYAN);
            g2.drawRoundRect(slotX, slotY, slotSize, slotSize, 15, 15);

            // Icon
            if (equippedHudIcons[i] != null) {

                Image icon = equippedHudIcons[i].getScaledInstance(slotSize - 12, slotSize - 12, Image.SCALE_SMOOTH);
                g2.drawImage(icon, slotX + 6, slotY + 6, null);

        // ==================== SHOE TIMER (Only when active) ====================
                if (i == shoeSlotIndex && shoeStartTime > 0)
                {
                    long elapsed = System.currentTimeMillis() - shoeStartTime;
                    long remaining = SHOE_DURATION - elapsed;

                    if (remaining > 0) {
                        int secondsLeft = (int) (remaining / 1000);
                        String timeText = secondsLeft + "s";

                        g2.setColor(new Color(0, 0, 0, 150));
                        g2.fillRoundRect(hudX +75, hudY+64, slotSize - 8, 22, 8, 8);

                        // Timer text (small, bold, centered)
                        g2.setColor(secondsLeft <= 10 ? Color.RED : Color.WHITE);
                        g2.setFont(new Font("Arial", Font.BOLD, 16));

                        TextLayout layout = new TextLayout(timeText, g2.getFont(), g2.getFontRenderContext());

                        float textX = hudX +83;
                        float textY = hudY+80;

                        layout.draw(g2, textX, textY);
                    }
                }

            } else {
                g2.setColor(Color.DARK_GRAY);
                g2.setFont(new Font("Arial", Font.BOLD, 30));
                g2.drawString("—", slotX + 20, slotY + 45);
            }
        }

    }

        g2.dispose();
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

