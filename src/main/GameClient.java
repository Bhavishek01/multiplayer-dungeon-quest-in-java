package main;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import gameplayers.OtherPlayer;


public class gameclient {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    public boolean idexists = false;
    public boolean nameexists = false;
    public String name;
    public String id;

    public final int[] equippedItems = new int[3];

    public boolean connection = false;
    public Map<String, OtherPlayer> otherPlayers = new ConcurrentHashMap<>();
    public gamehandler gameHandler;
    
    public List<PlayerItem> Items = new ArrayList<>();
    
    public boolean idExists() {
        return idexists;
    }

    public boolean connection() {
        return connection;
    }

    public boolean nameExists() {
        return nameexists;
    }

    public gameclient() {
        
        try {
            socket = new Socket("localhost", 5555);
            connection = true;
            System.out.println("Connected to server!");
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            java.util.Arrays.fill(equippedItems, 0);
            
            new Thread(() -> {
                String msg;
                try {
                    while ((msg = in.readLine()) != null) {
                        // System.out.println(msg);
                        handleServer(msg);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            }).start();

        } catch (IOException e) {
            System.out.println("Could not connect to server: " + e.getMessage());
            connection = false;
        }
    }

    private void handleServer(String message) {
        String[] parts = message.split("\\|");

        if (parts.length == 0) return;

        switch (parts[0]) {

            case "ITEMS":
                Items.clear();
                for (int i = 1; i < parts.length; i +=2) {
                    int name = Integer.parseInt(parts[i]);
                    int qty = Integer.parseInt(parts[i + 1]);
                    Items.add(new PlayerItem(name, qty));
                }
                break;

            case "SEND":
                    String newMsg = parts[1];
                    if (gameHandler != null && gameHandler.chat != null) {
                        gameHandler.chat.addMessage(newMsg);
                    }
                
                break;

            case "CHAT_HISTORY":
                System.out.print("recieved");
                    String histMsg = parts[1];
                    if (gameHandler != null && gameHandler.chat != null) {
                        gameHandler.chat.addMessage(histMsg);
                    }
                break;

            case "WORLD":
                if (message.length() <= 6) break;
                String data = message.substring(6);  // Everything after "WORLD|"
                
                // Split by | but find PROJECTILES delimiter first
                String[] allParts = data.split("\\|");
                
                // Find where PROJECTILES starts
                int projIndex = -1;
                for (int i = 0; i < allParts.length; i++) {
                    if ("PROJECTILES".equals(allParts[i])) {
                        projIndex = i;
                        break;
                    }
                }
                
                // === PARSE PLAYERS (everything BEFORE PROJECTILES) ===
                Map<String, OtherPlayer> newPlayers = new HashMap<>();
                if (projIndex == -1) {
                    // No PROJECTILES - parse entire data as players
                    projIndex = allParts.length;
                }
                
                // Parse player groups: id|name|x|y|dir (5 parts each)
                for (int i = 0; i < projIndex; i += 5) {
                    if (i + 4 >= projIndex) break;  // Not enough parts for full player
                    
                    try {
                        String playerId = allParts[i];
                        String playerName = allParts[i + 1];
                        int playerX = Integer.parseInt(allParts[i + 2]);  // x
                        int playerY = Integer.parseInt(allParts[i + 3]);  // y
                        String playerDir = allParts[i + 4];               // dir
                        
                        if (playerId.equals(this.id)) continue;  // Skip self
                        
                        OtherPlayer op = newPlayers.get(playerId);
                        if (op == null) {
                            op = new OtherPlayer(playerName);
                            if (gameHandler != null && gameHandler.p1 != null) {
                                // Copy sprites from local player
                                op.up1 = gameHandler.p1.up1; op.up2 = gameHandler.p1.up2;
                                op.down1 = gameHandler.p1.down1; op.down2 = gameHandler.p1.down2;
                                op.left1 = gameHandler.p1.left1; op.left2 = gameHandler.p1.left2;
                                op.right1 = gameHandler.p1.right1; op.right2 = gameHandler.p1.right2;
                                op.idle1 = gameHandler.p1.idle1; op.idle2 = gameHandler.p1.idle2;
                            }
                        }
                        op.entity_map_X = playerX;
                        op.entity_map_Y = playerY;
                        op.direction = playerDir;
                        op.count++;  // For animation
                        newPlayers.put(playerId, op);
                        
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        // Skip malformed player data - prevents crash
                        System.err.println("Skipping malformed player data at index " + i + ": " + e.getMessage());
                        continue;
                    }
                }
                
                // Update other players
                if (gameHandler != null) {
                    gameHandler.otherPlayers.clear();
                    gameHandler.otherPlayers.putAll(newPlayers);
                }
                
                // === PARSE PROJECTILES (everything AFTER PROJECTILES) ===
                if (projIndex + 1 < allParts.length) {
                    String projSection = allParts[projIndex + 1];  // "owner1,x1,y1,tx1,ty1;owner2,x2,y2,tx2,ty2;..."
                    synchronized (gameHandler.clientProjectiles) {
                        gameHandler.clientProjectiles.clear();
                        if (!projSection.isEmpty()) {
                            String[] projStrings = projSection.split(";");
                            for (String projStr : projStrings) {
                                if (projStr.trim().isEmpty()) continue;
                                try {
                                    String[] projParts = projStr.split(",");
                                    if (projParts.length >= 5) {
                                        String owner = projParts[0];
                                        double sx = Double.parseDouble(projParts[1]);
                                        double sy = Double.parseDouble(projParts[2]);
                                        double tx = Double.parseDouble(projParts[3]);
                                        double ty = Double.parseDouble(projParts[4]);
                                        
                                        Projectile proj = new Projectile(sx, sy, tx, ty, owner);
                                        gameHandler.clientProjectiles.add(proj);
                                    }
                                } catch (NumberFormatException e) {
                                    // Skip malformed projectile
                                    System.err.println("Skipping malformed projectile: " + projStr);
                                    continue;
                                }
                            }
                        }
                    }
                }
                break;

            case "LOGIN_SUCCESS":
                        
                    this.name = parts[2];
                    this.id = parts[1];                        
                    idexists = true;

                    System.out.println("player " + name + " connected");
                    break;

            case "REGISTER_SUCCESS":
                
                    this.name = parts[2];
                    this.id = parts[1];
                    nameexists = true;
                
                break;

            case "IAgain":
                idexists = false;
                break;

            case "NAgain":
                nameexists = false;
                break;

            case "ERROR":
                break;

            default:
                System.out.println("Server: " + message);
                break;
        }
    }



    // Send any message to server
    public void send(String message) {
        if (out != null) {
            out.println(message);
        }
    }
    // Close connection (call when game quits)
    public void disconnect() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setGameHandler(gamehandler gh) 
    {
        this.gameHandler = gh;
    }

    public void save_item() 
    {
        StringBuilder itemMsg = new StringBuilder("ITEMS");
            for (PlayerItem item : Items) 
            {
                itemMsg.append("|").append(item.id)
                .append("|").append(item.quantity);

                
            }
            out.println(itemMsg.toString());
    }
}