package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class playercheck {
    private Connection conn = DatabaseConnection.getConnection();
    private String playerid = null;
    private boolean exists = false;
    private boolean game_entered = false;

    public void setPlayerId(String playerId) {
        this.playerid = playerId;
    }

    public boolean isExists() {
        return exists;
    }

    public boolean isGameEntered() {
        return game_entered;
    }

    public void setGameEntered(boolean gameEntered) {
        this.game_entered = gameEntered;
    }

    public void checkplayerid() throws SQLException {
        if (playerid == null || playerid.isEmpty()) {
            exists = false;
            return;
        }
        String query = "SELECT playerid FROM players WHERE playerid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, playerid);
            try (ResultSet rs = pstmt.executeQuery()) {
                exists = rs.next(); // true if player exists, false otherwise
            }
        }
    }

    public void checkplayername(String name) throws SQLException {
    if (name == null || name.isEmpty()) {
        exists = true;
        return;
    }
    String query = "SELECT playername FROM players WHERE playername = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, name);
        try (ResultSet rs = pstmt.executeQuery()) {
            exists = rs.next(); // true if a row with the name exists, false otherwise
        }
    }
}

    public void addplayer(String name, String id) throws SQLException {
    String query = "INSERT INTO players (PlayerId, PlayerName) VALUES (?, ?)"; 
    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, id); // Set the player ID
        pstmt.setString(2, name); // Set the player name
        int rowsAffected = pstmt.executeUpdate(); // Execute the insert
        if (rowsAffected > 0) {
            System.out.println("Player " + id + " (" + name + ") added successfully!");
        } else {
            System.out.println("Failed to add player.");
        }
    }
}

}