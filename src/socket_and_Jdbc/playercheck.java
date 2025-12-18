package socket_and_Jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class playercheck {
    private static Connection conn = DatabaseConnection.getConnection();
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

        String query = "SELECT player_id FROM players WHERE player_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, id);
            System.out.println(id + "checking");
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

    String query = "SELECT player_name FROM players WHERE player_name = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, name);
        System.out.println(name + "checking");
        try (ResultSet rs = pstmt.executeQuery()) {
            nameexists = rs.next(); // true if a row with the name exists, false otherwise
        }
        System.out.println("return form db check ");
    }
    }

    public void addplayer(String name, String id) throws SQLException {
    System.out.println("adding player");
    String query = "INSERT INTO players (player_id, player_name) VALUES (?, ?)";

    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, id);
        pstmt.setString(2, name);

        System.out.println("Executing INSERT for player: " + id + " - " + name);
        int rowsAffected = pstmt.executeUpdate();
        System.out.println("Rows inserted: " + rowsAffected);

        if (rowsAffected > 0) {
            System.out.println("Player added successfully!");
        } else {
            System.out.println("Insert failed - no rows affected");
        }
    } catch (SQLException e) {
        System.err.println("SQL Error in addplayer:");
        e.printStackTrace();
        throw e;
    }
}

    public String giveplayername(String id) throws SQLException {

    String query = "SELECT player_name FROM players WHERE player_id = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, id);
        try (ResultSet rs = pstmt.executeQuery()) {
            if(rs.next()){
            return rs.getString("player_name"); 
        }}
    } return null;
    }

    public List<PlayerItem> getPlayerItems(String playerId) throws SQLException
    {
        List<PlayerItem> items = new ArrayList<>();
        String query = "SELECT item_id, qty FROM inventory WHERE player_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, playerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(new PlayerItem(
                        rs.getInt("item_id"),
                        rs.getInt("qty")
                    ));
                }
            }
        }
        return items;
    }

}
