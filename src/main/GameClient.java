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

            case "PROJECTILES":
                String projData = message.substring("PROJECTILES|".length());
                synchronized (gameHandler.clientProjectiles) {
                    gameHandler.clientProjectiles.clear();
                    if (!projData.isEmpty()) {
                        String[] projArray = projData.split(";");
                        for (String p : projArray) {
                            String[] partss = p.split(",");
                            if (partss.length >= 4) {
                                double x = Double.parseDouble(partss[0]);
                                double y = Double.parseDouble(partss[1]);
                                double tx = Double.parseDouble(partss[2]);
                                double ty = Double.parseDouble(partss[3]);
                                Projectile proj = new Projectile(x, y, tx, ty);
                                // override start position to current
                                proj.x = x;
                                proj.y = y;
                                gameHandler.clientProjectiles.add(proj);
                            }
                        }
                    }
                }
                break;

            case "WORLD":
                // If gameHandler is not ready yet, ignore WORLD messages
                if (gameHandler == null) {
                    System.out.println("Received WORLD message too early - ignoring until game starts");
                    break;
                }

                if (message.length() <= 6) break;
                String data = message.substring(6);

                // === Parse Players ===
                HashMap<String, OtherPlayer> newPlayers = new HashMap<>();

                if (!data.isEmpty()) {
                    String[] playerParts = data.split("\\|");
                    for (int i = 0; i < playerParts.length; i += 5) {
                        if (i + 4 >= playerParts.length) break;

                        String id = playerParts[i];
                        String name = playerParts[i + 1];
                        int x = Integer.parseInt(playerParts[i + 2]);
                        int y = Integer.parseInt(playerParts[i + 3]);
                        String dir = playerParts[i + 4];

                        if (id.equals(this.id)) continue;

                        OtherPlayer op = newPlayers.get(id);
                        if (op == null) {
                            op = new OtherPlayer(name);
                            // Safe to copy sprites now — gameHandler.p1 exists
                            op.up1 = gameHandler.p1.up1;    op.up2 = gameHandler.p1.up2;
                            op.down1 = gameHandler.p1.down1;  op.down2 = gameHandler.p1.down2;
                            op.left1 = gameHandler.p1.left1;  op.left2 = gameHandler.p1.left2;
                            op.right1 = gameHandler.p1.right1; op.right2 = gameHandler.p1.right2;
                            op.idle1 = gameHandler.p1.idle1;  op.idle2 = gameHandler.p1.idle2;
                        }

                        op.entity_map_X = x;
                        op.entity_map_Y = y;
                        op.direction = dir;
                        op.count++;

                        newPlayers.put(id, op);
                    }
                }

                gameHandler.otherPlayers.clear();
                gameHandler.otherPlayers.putAll(newPlayers);
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