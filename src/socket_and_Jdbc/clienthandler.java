package socket_and_Jdbc;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public String playerId = null;
    public String playerName = null;
    public playercheck checker = new playercheck();

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("SERVER: Connected. Send LOGIN|id or REGISTER|name");

            String message;
            while ((message = in.readLine()) != null) {
                    handleClient(message);
                    
            }

        } catch (IOException e) {
            System.out.println("Client disconnected.");
        } finally {
            cleanup();
        }
    }

    private void handleClient(String msg)
    {
        String[] parts = msg.split("\\|");
         
        
        try 
        {
           
            if ("LOGIN".equals(parts[0])) 
            {
                System.out.println("login");
                this.playerId = parts[1];
                System.out.println("+" + playerId);
                checker.checkplayerid(playerId);
                if (checker.idExists()) {
                    playerName = checker.giveplayername(playerId);
                    out.println("LOGIN_SUCCESS|" + playerId + "|" + playerName);
                } else {
                    out.println("IAgain");
                }

            }

            else if ("REGISTER".equals(parts[0])) 
            {
                this.playerName = parts[1];
                checker.checkplayername(playerName);

                if (checker.nameExists()) {
                    out.println("NAgain");
                }
                else 
                {
                    Random rand = new Random();
                    playerId = playerName + rand.nextInt(255);

                    checker.addplayer(playerName, playerId);

                    out.println("REGISTER_SUCCESS|" + playerId + "|" + playerName);
                    System.out.println("New player registered: " + playerId + " (" + playerName + ")");
                }
            } 
            
            else 
            {
                out.println("ERROR|Use LOGIN|id|name or REGISTER|name");
            }
            
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            out.println("ERROR|Server error");
        }
    }

    private void cleanup() {
        try {
            if (playerId != null) {
                System.out.println("Player logged out: " + playerId + " (" + playerName + ")");
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}