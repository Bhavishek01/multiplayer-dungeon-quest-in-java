package socket_and_Jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    static Connection con=null;
    public static Connection getConnection()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url="jdbc:mysql://localhost:3306/dungeon_quest_players";
            String user="root";
            String pass="";
            
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("connection established");

            }
        catch (ClassNotFoundException | SQLException e) 
            {
            e.printStackTrace();
            }
        return con; 
    }

    public static void main(String[] args) {
        Connection con = DatabaseConnection.getConnection();
    }
}
