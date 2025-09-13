package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class playercheck {

    // Initialize database
    Connection conn = DatabaseConnection.getConnection();

    String playerid = null ;
    boolean exists  = false ;

    gamepannel gp = new gamepannel();

    public void checkid() throws SQLException
    {
        String query = "SELECT playerid FROM players WHERE playerid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, playerid);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) 
                {
                    exists = true; // player exists in database
                }
                else
                {
                    exists = false; // player doesnt exists in database
                }
            }
        }
    }


    public void startchecking()
    {
        

    }

    public void enterid()
    {



    }

}
