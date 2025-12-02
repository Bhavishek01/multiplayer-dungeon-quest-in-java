package socket_and_Jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class playercheck {
    private Connection conn = DatabaseConnection.getConnection();
    private boolean idexists = false;
    private boolean nameexists = false;

    public boolean idExists() {
        return idexists;
    }

    public boolean nameExists() {
        return nameexists;
    }
    

    public void checkplayerid(String id) throws SQLException {

        if (id == null || id.isEmpty()) {
            idexists = false;
            return;
        }

        String query = "SELECT playerid FROM players WHERE playerid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                idexists = rs.next(); // true if player exists, false otherwise
            }
        }
    }

    public void checkplayername(String name) throws SQLException {
    if (name == null || name.isEmpty()) {
        nameexists = false;
        return;
    }

    String query = "SELECT playername FROM players WHERE playername = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, name);
        try (ResultSet rs = pstmt.executeQuery()) {
            nameexists = rs.next(); // true if a row with the name exists, false otherwise
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

    public String giveplayername(String id) throws SQLException {

    String query = "SELECT playername FROM players WHERE playerid = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, id);
        try (ResultSet rs = pstmt.executeQuery()) {
            if(rs.next()){
            return rs.getString("PlayerName"); 
        }}
    } return null;
    }

}