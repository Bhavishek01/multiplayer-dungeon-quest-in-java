package socket_and_Jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.PlayerRanking;


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

    public int getPlayerWins(String playerId) throws SQLException {
        String query = "SELECT wins FROM players WHERE player_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, playerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getInt("wins") : 0;
            }
        }
    }

    public void updateWins(String playerId, int wins) throws SQLException {
        String query = "UPDATE players SET wins = ? WHERE player_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, wins);
            pstmt.setString(2, playerId);
            pstmt.executeUpdate();
        }
    }

    public String getNameById(String id) throws SQLException {
            String query = "SELECT player_name FROM players WHERE player_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("player_name");
                    }
                }
            }
            return null;
        }

        public void addPlayer(String id, String name) throws SQLException {
            String sql = "INSERT INTO players (player_id, player_name, wins,kills) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE player_name = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, id);
                ps.setString(2, name);
                ps.setInt(3, 0);
                ps.executeUpdate();
            }
        }

        public void incrementWins(String playerId) throws SQLException {
    String sql = "UPDATE players SET wins = wins + 1 WHERE player_id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, playerId);
        ps.executeUpdate();
    }
}

        public List<PlayerRanking> getTopPlayers(int limit) throws SQLException {
    List<PlayerRanking> top = new ArrayList<>();
    String sql = "SELECT player_id, player_name, wins FROM players ORDER BY wins DESC LIMIT ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, limit);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                top.add(new PlayerRanking(
                    rs.getString("player_id"),
                    rs.getString("player_name"),
                    rs.getInt("wins")
                    // rs.getInt("kills")
                    
                )
            );
            }
        }
    }
    return top;
    
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

    public void add_items(String pid, int item_id, int qty)
    {
        String ADD_ITEM_SQL ="INSERT INTO inventory (player_id, item_id, qty) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE qty = qty + VALUES(qty)";

        try (PreparedStatement ps = conn.prepareStatement(ADD_ITEM_SQL)) {
            ps.setString(1, pid);
            ps.setInt(2, item_id);
            ps.setInt(3, qty);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
