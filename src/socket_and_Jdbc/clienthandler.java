package socket_and_Jdbc;

import java.io.*;
import java.net.*;

public class clienthandler extends Thread {
    private Socket clientSocket;
    private String clientId;
    private BufferedReader in;
    private PrintWriter out;

    public clienthandler(Socket socket, String clientId) {
        this.clientSocket = socket;
        this.clientId = clientId;
    }

    @Override
    public void run() {
        try {
            // Set up input and output streams
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            System.out.println("Handling client with ID: " + clientId);

            // Handle client messages
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received from " + clientId + ": " + message);
                // Echo back the message
                out.println("Echo from server to " + clientId + ": " + message);
            }
        } catch (IOException e) {
            System.out.println("Error handling client " + clientId + ": " + e.getMessage());
        } finally {
            try {
                System.out.println("Client " + clientId + " disconnected");
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}