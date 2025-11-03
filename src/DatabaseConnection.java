import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:expenses.db";
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (account TEXT PRIMARY KEY, password TEXT NOT NULL)");
        }
        return conn;
    }
}

