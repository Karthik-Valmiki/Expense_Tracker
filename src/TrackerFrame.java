import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class TrackerFrame extends JFrame {
    private final String account;

    public TrackerFrame(String account) {
        this.account = account;
        setTitle("Expense Tracker - " + account);
        setSize(430, 220);
        setLayout(new GridLayout(4, 1));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTextField earningField = new JTextField(10);
        JTextField spendCountField = new JTextField(10);
        JButton chartBtn = new JButton("Generate Pie Chart");

        JPanel p1 = new JPanel();
        p1.add(new JLabel("Total Earning:"));
        p1.add(earningField);

        JPanel p2 = new JPanel();
        p2.add(new JLabel("Number of Expenses:"));
        p2.add(spendCountField);

        add(p1);
        add(p2);
        add(chartBtn);

        chartBtn.addActionListener(e -> {
            double earning;
            int spendCount;
            try {
                earning = Double.parseDouble(earningField.getText());
                spendCount = Integer.parseInt(spendCountField.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid numbers!");
                return;
            }

            HashMap<String, Double> data = new HashMap<>();
            double totalSpent = 0;

            for (int i = 1; i <= spendCount; i++) {
                JTextField catField = new JTextField();
                JTextField amtField = new JTextField();
                JPanel p = new JPanel(new GridLayout(2, 2));
                p.add(new JLabel("Category " + i + " name:"));
                p.add(catField);
                p.add(new JLabel("Amount for " + i + ":"));
                p.add(amtField);

                int res = JOptionPane.showConfirmDialog(this, p, "Expense Entry " + i, JOptionPane.OK_CANCEL_OPTION);
                if (res == JOptionPane.OK_OPTION) {
                    String cat = catField.getText().trim();
                    double amt = Double.parseDouble(amtField.getText().trim());
                    data.put(cat, amt);
                    totalSpent += amt;
                }
            }
            double leftover = earning - totalSpent;
            if (leftover > 0) data.put("Leftover", leftover);

            PieChartPanel.showChart(data);
        });
    }
}
