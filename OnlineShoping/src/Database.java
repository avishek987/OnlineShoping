import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/OnlineShop";
        String user = "root";
        String password = "Root@123";
        return DriverManager.getConnection(url, user, password);
    }
}



