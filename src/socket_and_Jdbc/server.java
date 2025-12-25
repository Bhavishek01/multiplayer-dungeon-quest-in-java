package socket_and_Jdbc;

import java.net.ServerSocket;
import java.net.Socket;

public class server {

    private static final int PORT = 5555;

    
    public static void main(String[] args) {
        System.out.println("=== Dungeon Quest Server Starting on port " + PORT + " ===");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening... Waiting for players...\n");

            while (true) {
                Socket clientSocket = serverSocket.accept(); 

                ClientHandler handler = new ClientHandler(clientSocket);
                 System.out.println("client added\n");

                new Thread(handler).start();
            }

        } catch (Exception e) {
            System.out.println("Server crashed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}