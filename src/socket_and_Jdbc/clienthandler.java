package socket_and_Jdbc;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Iterator;


class ClientHandler implements Runnable {
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    public playercheck checker = new playercheck();

    public String playerId = null;
    private String playerName = null;
    private int x = 96, y = 96;
    private String direction = "down";
    private boolean GameEnter = false;
    public List<PlayerItem> items = new ArrayList<>();

    public static final List<ClientHandler> allClients = Collections.synchronizedList(new ArrayList<>());
    
    public static final List<Projectile> projectiles = Collections.synchronizedList(new ArrayList<>());
    private static long projectileIdCounter = 0;

    private static final int MAX_CHAT_HISTORY = 10;
    private static final List<String> globalChatHistory = Collections.synchronizedList(new ArrayList<>());

    public ClientHandler(Socket socket) {
        this.socket = socket;
        System.out.println("client handler created");

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
                System.out.println(message);
                if (playerId == null) {
                    handleLogin(message);
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
                        checker.addplayer(playerName, playerId);
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
            
            synchronized (globalChatHistory) 
            {
                for (String histMsg : globalChatHistory) 
                    {
                    out.println("CHAT_HISTORY|" + histMsg);
                }
            }
            return;
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

        
        else if ("SHOOT".equals(parts[0])){
            double tx = Double.parseDouble(parts[1]);
            double ty = Double.parseDouble(parts[2]);

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

            // SERVER-SIDE TILE COLLISION CHECK
            int col = (int) (p.x / 48);  // tile size = 48
            int row = (int) (p.y / 48);

            boolean hitWall = false;

            // Out of bounds = wall
            if (row < 0 || row >= 50 || col < 0 || col >= 50) {  // map is 50x50
                hitWall = true;
            } else {
                // You need access to the map data here
                // We'll use a simple static map loader (see below)
                if (ServerTileMap.isWall(row, col)) {
                    hitWall = true;
                }
            }

            if (hitWall || !p.active) {
                it.remove();  // Remove immediately — all clients will stop seeing it
                continue;
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
}