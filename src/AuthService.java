import javax.swing.*;
import java.sql.*;

public class AuthService {

    public static boolean register(String account, String password) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO users (account, password) VALUES (?, ?)");
            ps.setString(1, account);
            ps.setString(2, password);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Registered successfully!");
            return true;
        } catch (SQLException ex) {
            String msg = ex.getMessage().toLowerCase();
            if (msg.contains("unique") || msg.contains("primary")) {
                JOptionPane.showMessageDialog(null, "Account already exists! Choose a different number.");
            } else {
                JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
            }
            return false;
        }
    }

    public static boolean login(String account, String password) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE account=? AND password=?");
            ps.setString(1, account);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
            return false;
        }
    }
}
