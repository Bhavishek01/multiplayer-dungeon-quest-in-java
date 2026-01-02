package socket_and_Jdbc;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


import java.util.Iterator;
import main.PlayerRanking;  // Add at top


class ClientHandler implements Runnable {
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    public playercheck checker = new playercheck();
    private List<PlayerRanking> top;  // Now uses main.PlayerRanking

    public String playerId = null;
    private String playerName = null;
    private int x = 96, y = 96;
    private String direction = "down";
    private boolean GameEnter = false;
    public List<PlayerItem> items = new ArrayList<>();
    private long lastFireTime = 0;
    private static final long SERVER_FIRE_COOLDOWN_MS = 250;
    boolean validSpawn = false;

    public int life = 5;  // NEW: Server-tracked life
    public int kills = 0;  // NEW: Server-tracked kills

    public static final List<ClientHandler> allClients = Collections.synchronizedList(new ArrayList<>());

    
    public static final List<Projectile> projectiles = Collections.synchronizedList(new ArrayList<>());
    private static long projectileIdCounter = 0;

    private static final int MAX_CHAT_HISTORY = 10;
    private static final List<String> globalChatHistory = Collections.synchronizedList(new ArrayList<>());

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.life = 5;        // Everyone starts with 5 lives
        this.kills = 0;       // Start with 0 kills per session (or load later)
        System.out.println("ClientHandler created");
    }

    @Override
    public void run() 
    {
        try 
        {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message;
            while ((message = in.readLine()) != null) {
                String[] parts = message.split("\\|");
                System.out.println(message);
                if (playerId == null) {
                    handleLogin(message);
                }
                else if ("SHOOT".equals(parts[0])){
                    
                    double tx = Double.parseDouble(parts[1]);
                    double ty = Double.parseDouble(parts[2]);

                    long now = System.currentTimeMillis();
                    if (now - lastFireTime < SERVER_FIRE_COOLDOWN_MS) {
                        break; // Ignore spam
                    }
                    lastFireTime = now;

                    // Use player's current position (center)
                    double startX = this.x + 24;
                    double startY = this.y + 24;

                    Projectile proj = new Projectile(startX, startY, tx, ty, playerId);
                    proj.id = ++projectileIdCounter;  // Assign unique ID

                    synchronized (projectiles) {
                        projectiles.add(proj);
                    }
                    // broadcastWorld() will handle sending it to everyone
                }
                else 
                {
                    handleGameMessage(message);
                }

            }

        } catch (Exception e) {
            System.out.println("Player disconnected: " + playerId);
        } finally {
            logout();
        }
    }

    private void handleLogin(String msg) {
        String[] parts = msg.split("\\|");
        try 
            {
            if (parts.length >= 2 )
            {
                
                if ("LOGIN".equals(parts[0])) 
                {
                    this.playerId = parts[1];
                    checker.checkplayerid(playerId);
                    if (checker.idExists()) 
                        {
                            playerName = checker.giveplayername(playerId);          
                            System.out.println(playerName + "logined");              
                            out.println("LOGIN_SUCCESS|" + playerId + "|" + playerName);
                            System.out.println("client added: " + playerId);
                            allClients.add(this);
                            send_inventory();
                        } 
                        else 
                            {
                                out.println("IAgain");
                                playerId = null;
                            }
                }
                else if ("REGISTER".equals(parts[0])) 
                {
                    this.playerName = parts[1];
                    checker.checkplayername(playerName);
                    try 
                    {
                        Thread.sleep(200);  // Pause for 1000 ms = 1 second
                    } catch (InterruptedException ae) {
                        ae.printStackTrace();
                    }
                    if (checker.nameExists()) 
                    {
                        out.println("NAgain");
                        playerName = null;
                    }
                    else
                    {
                        System.out.println("name doesnt exists");
                        Random rand = new Random();
                        playerId = playerName + rand.nextInt(255);
                        System.out.println(playerId);
                        checker.addPlayer(playerId, playerName);
                        out.println("REGISTER_SUCCESS|" + playerId + "|" + playerName);
                        System.out.println("New player registered: " + playerId + " (" + playerName + ")");
                        allClients.add(this);
                        send_inventory();
                    }
                }
                else 
                {
                    out.println("ERROR|Use LOGIN|id|name or REGISTER|name");
                }
            }

        }
        catch (Exception ignored) {}
    }

    private void handleGameMessage(String msg)
    {
        String[] parts = msg.split("\\|");
        
        if("POS".equals(parts[0]))
            try {
                this.x = (int) Double.parseDouble(parts[1]);
                this.y = (int) Double.parseDouble(parts[2]);
                this.direction = parts[3];
                broadcastWorld();
            } catch (Exception ignored) {}
            

        else if("ENTER_GAME".equals(parts[0]))
        {
            GameEnter = true;
            this.kills = 0;  // Critical: Reset kills every new match!
            this.life = 5;   // Also reset life

            spawnRandom();

            synchronized (globalChatHistory) 
            {
                for (String histMsg : globalChatHistory) 
                {
                    out.println("CHAT_HISTORY|" + histMsg);
                }
            }
            broadcastWorld();
            return;
        }

        else if ("DEATH".equals(parts[0])) {
            String killerId = parts[1];

            // Broadcast to everyone: "Player X killed Player Y"
            // But more importantly: tell the killer they got a kill
            synchronized (allClients) {
                for (ClientHandler client : allClients) {
                    if (client.playerId != null && client.playerId.equals(killerId)) {
                        client.out.println("KILL_AWARD");  // Special message just for killer
                        System.out.println("Kill awarded to " + killerId);
                        break;
                    }
                }
            }

        }

        else if ("LEADERBOARD".equals(parts[0])){
                try {
                    top = checker.getTopPlayers(10);
                    StringBuilder sb = new StringBuilder("LEADERBOARD");
                    for (PlayerRanking pr : top) {
                        sb.append("|").append(pr.id).append(":").append(pr.name).append(":").append(pr.wins);
                        
                    }
                    out.println(sb.toString());
                } catch (SQLException e) {
                    out.println("ERROR|Leaderboard fetch failed");
                    e.printStackTrace();
                }
            }
        
        else if("ITEMS".equals(parts[0])) 
        {
            items.clear();
            for (int i = 1; i < parts.length; i +=2) 
            {
                int name = Integer.parseInt(parts[i]);
                int qty = Integer.parseInt(parts[i + 1]);
                items.add(new PlayerItem(name, qty));
                checker.add_items(playerId, name, qty);
            }
   
        }
        

        else if ("CHAT".equals(parts[0]))
        {
            String formattedMessage = playerName + ": " + parts[1];
                
            synchronized (globalChatHistory) 
            {
                globalChatHistory.add(formattedMessage);
                if (globalChatHistory.size() > MAX_CHAT_HISTORY) 
                {
                    globalChatHistory.remove(0);
                }
            }

            String broadcast = "SEND|" + formattedMessage;
            synchronized (allClients) {
                for (ClientHandler client : allClients) {
                    try {
                        client.out.println(broadcast);
                    } catch (Exception ignored) {}
                }
            }
            System.out.println("Chat: " + formattedMessage);
        }
    }
        

    private void broadcastWorld() {
    // === UPDATE ALL PROJECTILES + SERVER-SIDE WALL COLLISION ===
    synchronized (projectiles) {
    Iterator<Projectile> it = projectiles.iterator();
    while (it.hasNext()) {
        Projectile p = it.next();
        p.update();

        boolean hitWall = false;

            int col = (int) (p.x / 48);
            int row = (int) (p.y / 48);

            // Out of bounds = wall
            if (row < 0 || row >= 50 || col < 0 || col >= 50) {
                hitWall = true;
            } else {
                if (ServerTileMap.isWall(row, col)) {
                    hitWall = true;
                }
            }

        // WALL COLLISION (already there)
        if (hitWall || !p.active) {
            it.remove();
            continue;
        }

        // === NEW: SERVER-SIDE PLAYER HIT CHECK ===
                // === NEW: SERVER-SIDE PLAYER HIT CHECK ===
        boolean hitPlayer = false;
        synchronized (allClients) {
            for (ClientHandler targetClient : allClients) {
                if (targetClient.playerId.equals(p.ownerId)) continue; // Don't hit self

                // Player hitbox (same as client)
                int left = targetClient.x + 15;
                int top = targetClient.y + 10;
                int width = 18;
                int height = 33;

                if (p.x >= left && p.x <= left + width &&
                    p.y >= top && p.y <= top + height) {

                    hitPlayer = true;

                    // Reduce target's life
                    targetClient.life -= 1;

                    // Broadcast updated life to all clients
                    broadcast("HEALTH|" + targetClient.playerId + "|" + targetClient.life);

                    if (targetClient.life <= 0) {
                        // === KILL AWARDED TO SHOOTER ===
                        ClientHandler killer = findClientById(p.ownerId);
                        if (killer != null) {
                            killer.kills += 1;

                            // Broadcast kill count update
                            broadcast("KILLS|" + killer.playerId + "|" + killer.kills);

                            // === CHECK WIN CONDITION ===
                            if (killer.kills >= 10) {
                                String winnerName = killer.playerName;
                                String winnerId = killer.playerId;

                                // Increment wins in DB
                                try {
                                    int currentWins = checker.getPlayerWins(winnerId);
                                    checker.updateWins(winnerId, currentWins + 1);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                // Broadcast game over to ALL clients
                                broadcast("GAMEOVER|" + winnerId + "|" + winnerName);
                            }
                        }

                        // Respawn the dead player
                        targetClient.life = 5;
                        targetClient.spawnRandom();
                        targetClient.direction = "down";

                        // Tell everyone about respawn
                        broadcast("RESPAWN|" + targetClient.playerId + "|96|96|down");
                    }

                    break; // One bullet = one hit max
                }
            }
        } // ← End of synchronized(allClients)

        // Now safely remove projectile if it hit a player
        if (hitPlayer) {
            it.remove();
        }
        }
    }

    // === BUILD PLAYER DATA ===
    StringBuilder playerData = new StringBuilder();
    synchronized (allClients) {
        for (ClientHandler client : allClients) {
            if (client.playerId != null) {
                playerData.append(client.playerId).append("|")
                        .append(client.playerName).append("|")
                        .append(client.x).append("|")
                        .append(client.y).append("|")
                        .append(client.direction).append("|");
            }
        }
    }
    
    // === BUILD PROJECTILE DATA ===
    StringBuilder projData = new StringBuilder();
    synchronized (projectiles) {
        for (Projectile p : projectiles) {
            if (p.active) {
                projData.append("|").append(p.id).append(";")
                        .append(p.ownerId).append(";")
                        .append((int)p.x).append(";")
                        .append((int)p.y).append(";")
                        .append((int)p.targetX).append(";")
                        .append((int)p.targetY);
            }
        }
    }
    

    
    // === BROADCAST ===
    String worldMsg = "WORLD|" + playerData.toString() + "PROJECTILES|" + projData.toString();
    synchronized (allClients) {
        for (ClientHandler client : allClients) {
            try {
                client.out.println(worldMsg);
            } catch (Exception ignored) {}
        }
    }
}


    private void logout() 
    {
        if (playerId != null) {
            allClients.remove(this);
            broadcastWorld();
            System.out.println(playerName + " logged out");
        }
        try { socket.close(); } catch (Exception ignored) {}
    }

    class ServerTileMap {
    private static final int[][] map = new int[50][50];  // 50x50 map
    private static final int TILE_SIZE = 48;

    static {
        loadMap();
    }

    private static void loadMap() {
        try (BufferedReader br = new BufferedReader(new FileReader("resource/50x40.txt"))) {  // your map file
            for (int row = 0; row < 50; row++) {
                String line = br.readLine();
                if (line == null) break;
                String[] tokens = line.trim().split("\\s+");
                for (int col = 0; col < tokens.length && col < 50; col++) {
                    map[row][col] = Integer.parseInt(tokens[col]);
                }
            }
            System.out.println("Server map loaded successfully.");
        } catch (Exception e) {
            System.err.println("Failed to load server map: " + e.getMessage());
            e.printStackTrace();
            // Fallback: treat everything as ground
        }
    }

    public static boolean isWall(int row, int col) {
        if (row < 0 || row >= 50 || col < 0 || col >= 50) return true;
        int tile = map[row][col];
        return tile == 0 || tile == 1;  // 0 = wall, 1 = stone (adjust if needed)
    }
}

   private void send_inventory() 
        {
        try {
            List<PlayerItem> items = checker.getPlayerItems(playerId);
            StringBuilder itemMsg = new StringBuilder("ITEMS");
            for (PlayerItem item : items) 
            {
                itemMsg.append("|").append(item.name)
                .append("|").append(item.quantity);
            }
            out.println(itemMsg.toString());
            } 
            catch (SQLException e) 
            {
                out.println("ERROR|Failed to fetch items");
                e.printStackTrace();
            }
        }

        private void broadcast(String message) {
            synchronized (allClients) {
                for (ClientHandler client : allClients) {
                    if (client.out != null) {
                        client.out.println(message);
                    }
                }
            }
        }

        private ClientHandler findClientById(String id) {
            synchronized (allClients) {
                for (ClientHandler ch : allClients) {
                    if (ch.playerId.equals(id)) {
                        return ch;
                    }
                }
            }
            return null;
        }

        // Add this method to ClientHandler class (inside socket_and_Jdbc/ClientHandler.java)

        private void spawnRandom() {
            Random rand = new Random();
            int attempts = 0;
            final int MAX_ATTEMPTS = 200;  // Increased for safety with collision checks
            
            while (attempts < MAX_ATTEMPTS) {
                int row = rand.nextInt(50);  // 0-49
                int col = rand.nextInt(50);  // 0-49
                
                // Valid tile: NOT wall (ground=2 or water=3)
                if (ServerTileMap.isWall(row, col)) {
                    attempts++;
                    continue;
                }
                
                // Calculate center of tile
                int newX = col * 48 + 24;
                int newY = row * 48 + 24;
                
                // Check not too close to other players (min 3 tiles apart ~144 pixels)
                boolean tooClose = false;
                synchronized (allClients) {
                    for (ClientHandler other : allClients) {
                        if (other.playerId != null && other.GameEnter && other.playerId.equals(this.playerId)) continue;
                        
                        double dist = Math.hypot(newX - other.x, newY - other.y);
                        if (dist < 96) {  // ~2 tiles apart minimum
                            tooClose = true;
                            break;
                        }
                    }
                }
                
                if (!tooClose) {
                    // Valid spawn found!
                    this.x = newX;
                    this.y = newY;
                    this.direction = "down";
                    return;
                }
                
                attempts++;
            }
            
            // Fallback to safe spawn (center-ish, assuming valid)
            this.x = 24 * 48 + 24;  // Tile 24,24
            this.y = 24 * 48 + 24;
            System.out.println(playerName + " used fallback spawn after " + attempts + " attempts");
        }
        
}