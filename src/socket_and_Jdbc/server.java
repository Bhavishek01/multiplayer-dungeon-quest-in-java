package socket_and_Jdbc;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.time.LocalDateTime;

public class server {
    private static final String DB_URL = "jdbc:sqlite:clients.db";

    public static void main(String[] args) {
        // Initialize database
        initializeDatabase();

        try {
            try (ServerSocket serverSocket = new ServerSocket(5555)) {
                System.out.println("Server listening on port 5555");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Connection from " + clientSocket.getInetAddress());

                    // Receive client ID
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String clientId = in.readLine();
                    System.out.println("Received Client ID: " + clientId);

                    // Check if client is new or old
                    boolean isNewClient = checkClientStatus(clientId);
                    System.out.println("Client " + clientId + " is " + (isNewClient ? "new" : "returning"));

                    // Start a new ClientHandler thread
                    clienthandler handler = new clienthandler(clientSocket, clientId);
                    handler.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        
    }

    private static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS clients (" +
                         "client_id TEXT PRIMARY KEY, " +
                         "last_connected TEXT)";
            stmt.execute(sql);
            System.out.println("Database initialized");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkClientStatus(String clientId) {
        boolean isNewClient = true;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT client_id FROM clients WHERE client_id = ?")) {
            pstmt.setString(1, clientId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                isNewClient = false; // Client exists in database
            } else {
                // Insert new client
                try (PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO clients (client_id, last_connected) VALUES (?, ?)")) {
                    insertStmt.setString(1, clientId);
                    insertStmt.setString(2, LocalDateTime.now().toString());
                    insertStmt.executeUpdate();
                }
            }
            // Update last connected time
            try (PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE clients SET last_connected = ? WHERE client_id = ?")) {
                updateStmt.setString(1, LocalDateTime.now().toString());
                updateStmt.setString(2, clientId);
                updateStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isNewClient;
    }
}