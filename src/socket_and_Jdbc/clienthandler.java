package socket_and_Jdbc;

import java.io.*;
import java.net.Socket;
import java.util.*;

class ClientHandler implements Runnable {
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    public playercheck checker = new playercheck();

    private String playerId = null;
    private String playerName = null;
    private int x = 96, y = 96;
    private String direction = "down";

    public static final List<ClientHandler> allClients = Collections.synchronizedList(new ArrayList<>());

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message;
            while ((message = in.readLine()) != null) {
             System.out.println(message);
                if (playerId == null) {
                   
                    handleLogin(message);
                } else {
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
                    if (checker.nameExists()) 
                    {
                        out.println("NAgain");
                        playerName = null;
                    }
                    else
                    {
                        Random rand = new Random();
                        playerId = playerName + rand.nextInt(255);
                        checker.addplayer(playerName, playerId);
                        out.println("REGISTER_SUCCESS|" + playerId + "|" + playerName);
                        System.out.println("New player registered: " + playerId + " (" + playerName + ")");
                        allClients.add(this);
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
            try {
                    this.x = Integer.parseInt(parts[1]);
                    this.y = Integer.parseInt(parts[2]);
                    this.direction = parts[3];
                    broadcastWorld();  // Send updated world to everyone

                } 
                catch (Exception ignored) {}
    }

    private void broadcastWorld() 
    {
        StringBuilder world = new StringBuilder("WORLD");
        synchronized (allClients) {
            for (ClientHandler client : allClients) {
                if (client.playerId != null) {  // only logged-in players
                    world.append("|")
                        .append(client.playerId).append("|")
                        .append(client.playerName).append("|")
                        .append(client.x).append("|")
                        .append(client.y).append("|")
                        .append(client.direction);
                }
            }
        }
        String finalMsg = world.toString();

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
}