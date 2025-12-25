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
            String url="jdbc:mysql://localhost:3306/game";
            String user="root";
            String pass="";
            con = DriverManager.getConnection(url, user, pass);
            }
        catch (ClassNotFoundException | SQLException e) 
            {
            e.printStackTrace();
            }
        return con; 
    }
}
