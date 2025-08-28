package socket_and_Jdbc;

import java.io.*;
import java.net.*;

public class client {
    public static void main(String[] args) {
        try {
            // Connect to the server
            Socket socket = new Socket("localhost", 5555);
            System.out.println("Connected to server");

            // Set up input and output streams
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            // Send client ID
            System.out.print("Enter your client ID: ");
            String clientId = consoleReader.readLine();
            out.println(clientId);

            // Start a thread to read server responses
            Thread responseThread = new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    System.out.println("Server disconnected");
                }
            });
            responseThread.start();

            // Read user input and send to server
            String message;
            while ((message = consoleReader.readLine()) != null) {
                out.println(message);
            }

            // Clean up
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
