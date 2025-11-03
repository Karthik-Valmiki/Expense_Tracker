import javax.swing.*;
import java.awt.*;

public class LoginRegisterFrame extends JFrame {

    public LoginRegisterFrame() {
        setTitle("Smart Expense Tracker");
        setSize(350, 220);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton regBtn = new JButton("Register");
        JButton loginBtn = new JButton("Login");

        add(new JLabel("Welcome!"));
        add(regBtn);
        add(loginBtn);

        regBtn.addActionListener(e -> showRegister());
        loginBtn.addActionListener(e -> showLogin());
    }

    private void showRegister() {
        JTextField accField = new JTextField(15);
        JTextField passField = new JTextField(15);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Account No:"));
        panel.add(accField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);

        int res = JOptionPane.showConfirmDialog(this, panel, "Register", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            boolean ok = AuthService.register(accField.getText(), passField.getText());
        }
    }

    private void showLogin() {
        JTextField accField = new JTextField(15);
        JTextField passField = new JTextField(15);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Account No:"));
        panel.add(accField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);

        int res = JOptionPane.showConfirmDialog(this, panel, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            boolean ok = AuthService.login(accField.getText(), passField.getText());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                this.dispose();
                new TrackerFrame(accField.getText()).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Login failed!");
            }
        }
    }
}
