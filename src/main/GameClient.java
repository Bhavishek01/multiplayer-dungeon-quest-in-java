package main;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class gameclient {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    public boolean idexists = false;
    public boolean nameexists = false;
    public String name;
    public String id;

    public boolean connection = false;
    public Map<String, OtherPlayer> otherPlayers = new ConcurrentHashMap<>();
    public gamehandler gameHandler;
    
    public List<PlayerItem> Items = new ArrayList<>();

    public static class OtherPlayer 
    {
        public int x, y;
        public String direction;
        public String name;
        public long lastUpdate = System.currentTimeMillis();
    }
    
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

            // Start listening thread
            new Thread(() -> {
                String msg;
                try {
                    while ((msg = in.readLine()) != null) {
                        System.out.println(msg);
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
                for (int i = 1; i < parts.length; i += 2) {
                    if (i + 2 >= parts.length) break;
                    String name = parts[i];
                    int qty = Integer.parseInt(parts[i + 1]);
                    Items.add(new PlayerItem(name, qty));
                }
                break;

            case "WORLD":
                    if (gameHandler != null) 
                        {
                            gameHandler.updateOtherPlayers(message);
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

    public void requestInventory() {
        send("INVENTORY");
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
}