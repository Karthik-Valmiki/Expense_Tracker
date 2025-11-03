import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class DashboardFrame extends JFrame {
    private final String account;

    public DashboardFrame(String account) {
        this.account = account;
        setTitle("Expense Tracker: " + account);
        setSize(400, 230);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton addIncomeBtn = new JButton("Add Income");
        JButton addExpenseBtn = new JButton("Add Expense");
        JButton pieBtn = new JButton("View Pie Chart");
        JButton balanceBtn = new JButton("View Balance");
        JButton logoutBtn = new JButton("Logout");

        JPanel btnPanel = new JPanel();
        btnPanel.add(addIncomeBtn);
        btnPanel.add(addExpenseBtn);
        btnPanel.add(pieBtn);
        btnPanel.add(balanceBtn);
        btnPanel.add(logoutBtn);
        add(btnPanel);

        addIncomeBtn.addActionListener(e -> addTransaction("income"));
        addExpenseBtn.addActionListener(e -> addTransaction("expense"));
        pieBtn.addActionListener(e -> showPie());
        balanceBtn.addActionListener(e -> showBalance());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginRegisterFrame().setVisible(true);
        });
    }

    private void addTransaction(String type) {
        String cat = (type.equals("income")) ? "Income" : JOptionPane.showInputDialog(this, "Category:");
        if (cat == null || cat.isBlank()) return;
        String amtStr = JOptionPane.showInputDialog(this, "Amount:");
        if (amtStr == null) return;
        double amt;
        try { amt = Double.parseDouble(amtStr); }
        catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid amount"); return; }
        try (Connection c = DatabaseConnection.getConnection()) {
            PreparedStatement ps = c.prepareStatement("INSERT INTO transactions (account, type, category, amount) VALUES (?, ?, ?, ?)");
            ps.setString(1, account);
            ps.setString(2, type);
            ps.setString(3, cat);
            ps.setDouble(4, amt);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Added!");
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, "DB Error!"); }
    }

    private void showBalance() {
        double inc = 0, exp = 0;
        try (Connection c = DatabaseConnection.getConnection()) {
            PreparedStatement ps = c.prepareStatement("SELECT SUM(amount) FROM transactions WHERE account=? AND type='income'");
            ps.setString(1, account);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) inc = rs.getDouble(1);
            ps = c.prepareStatement("SELECT SUM(amount) FROM transactions WHERE account=? AND type='expense'");
            ps.setString(1, account);
            rs = ps.executeQuery();
            if (rs.next()) exp = rs.getDouble(1);
        } catch (SQLException ex) { }
        JOptionPane.showMessageDialog(this, "Balance: " + (inc - exp));
    }

    private void showPie() {
        HashMap<String, Double> map = new HashMap<>();
        try (Connection c = DatabaseConnection.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "SELECT category, SUM(amount) FROM transactions WHERE account=? AND type='expense' GROUP BY category");
            ps.setString(1, account);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) map.put(rs.getString(1), rs.getDouble(2));
        } catch (SQLException ex) {}
        PieChartPanel.showChart(map);
    }
}
