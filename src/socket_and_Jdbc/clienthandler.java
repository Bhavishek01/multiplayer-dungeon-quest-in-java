package socket_and_Jdbc;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
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
                else if (message.startsWith("SHOOT|")) {
                    if (playerId == null || !GameEnter) continue;

                    String[] parts = message.substring(6).split("\\|");
                    if (parts.length != 2) continue;

                    try {
                        double targetX = Double.parseDouble(parts[0]);
                        double targetY = Double.parseDouble(parts[1]);

                        // Validate distance
                        double dx = targetX - x;
                        double dy = targetY - y;
                        double dist = Math.hypot(dx, dy);
                        if (dist < 50 || dist > 500) continue;

                        // Start from player center
                        double startX = x + 24;
                        double startY = y + 24;

                        // Build one-time creation message
                        String projMsg = "PROJECTILE|" + playerId + "|" + 
                                        (int)startX + "|" + (int)startY + "|" + 
                                        (int)targetX + "|" + (int)targetY;

                        // Broadcast to ALL clients (including shooter)
                        synchronized (allClients) {
                            for (ClientHandler client : allClients) {
                                try {
                                    client.out.println(projMsg);
                                } catch (Exception ignored) {}
                            }
                        }

                        System.out.println(playerName + " shot projectile to (" + targetX + "," + targetY + ")");

                    } catch (Exception ignored) {}
                    continue;
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
        
        if ("POS".equals(parts[0]))
        {
        try 
            {
            this.x = Integer.parseInt(parts[1]);
            this.y = Integer.parseInt(parts[2]);
            this.direction = parts[3];
            broadcastWorld();  // Send updated world to everyone
            } 
        catch (Exception ignored) {}
        }

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
        

    private void broadcastWorld() 
    {
        StringBuilder world = new StringBuilder("WORLD");
        synchronized (allClients) 
        {
            for (ClientHandler client : allClients ) 
            {
                if (client.playerId != null && client.GameEnter) 
                {
                    world.append("|")
                        .append(client.playerId).append("|")
                        .append(client.playerName).append("|")
                        .append(client.x).append("|")
                        .append(client.y).append("|")
                        .append(client.direction);
                }
            }
        }

        StringBuilder projData = new StringBuilder();
        synchronized (projectiles) 
        {
            Iterator<Projectile> it = projectiles.iterator();
            while (it.hasNext()) 
            {
                Projectile p = it.next();
                p.update(); // Update position

                if (!p.active) {
                    it.remove();
                    continue;
                }

                if (projData.length() > 0) projData.append("#");
                projData.append(p.id).append(",")
                        .append(p.ownerId).append(",")
                        .append((int)p.x).append(",")
                        .append((int)p.y);
            }
        }

        // Modify finalMsg to include projectiles
        String finalMsg = world.toString();
        if (projData.length() > 0) {
            finalMsg += "|PROJECTILES|" + projData.toString();
        } else {
            finalMsg += "|PROJECTILES|";
        }

        synchronized (allClients) {
            for (ClientHandler client : allClients) {
                try {
                    client.out.println(finalMsg);
                    System.out.println("broadcasting" + finalMsg);
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