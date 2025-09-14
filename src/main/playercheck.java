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

    public void checkid() throws SQLException {
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

}