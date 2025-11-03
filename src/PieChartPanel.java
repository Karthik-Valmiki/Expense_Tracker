import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PieChartPanel extends JPanel {
    private final HashMap<String, Double> data;
    private final double total;

    public PieChartPanel(HashMap<String, Double> data) {
        this.data = data;
        this.total = data.values().stream().mapToDouble(Double::doubleValue).sum();
        setPreferredSize(new Dimension(510, 430));
        setBackground(new Color(245, 247, 255));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (data == null || data.isEmpty() || total == 0) return;

        int x = 70, y = 70, wh = 250, startAngle = 0;
        Color[] colors = {
                new Color(68,138,255),   // blue
                new Color(255,171,145),  // soft orange
                new Color(102,225,142),  // green
                new Color(189,147,249),  // purple
                new Color(255,241,118),  // yellow
                new Color(244,143,177),  // pink
                new Color(128,222,234),  // cyan
                new Color(255,213,79),   // gold
                new Color(29,233,182)    // teal
        };

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Add a shadow
        g2.setColor(new Color(200, 200, 200, 220));
        g2.fillOval(x+10, y+10, wh, wh);

        int i = 0;
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            double value = entry.getValue();
            int angle = (int)Math.round((value / total) * 360);

            // Pie slice with border
            g2.setColor(colors[i % colors.length]);
            g2.fillArc(x, y, wh, wh, startAngle, angle);

            g2.setStroke(new BasicStroke(2f));  // Pie segment border
            g2.setColor(new Color(60,60,60,180));
            g2.drawArc(x, y, wh, wh, startAngle, angle);

            // Legend
            g2.setColor(colors[i % colors.length]);
            g2.fillRoundRect(x + wh + 40, y + i * 40, 30, 22, 8, 8);
            g2.setColor(new Color(50, 50, 50));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2.drawString(entry.getKey() + " : ₹" + value +
                            " (" + String.format("%.1f", (value / total) * 100) + "%)",
                    x + wh + 80, y + 18 + i * 40);

            startAngle += angle;
            i++;
        }

        g2.setColor(new Color(35,37,39,225));
        g2.setStroke(new BasicStroke(5f));
        g2.drawOval(x, y, wh, wh);
    }

    public static void showChart(HashMap<String, Double> data) {
        JFrame frame = new JFrame("Expense Pie Chart");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        PieChartPanel chart = new PieChartPanel(data);
        frame.setContentPane(chart);

        frame.pack();
        frame.setLocationRelativeTo(null);

        // Stylish outer frame
        frame.getContentPane().setBackground(new Color(236, 239, 241));
        frame.setVisible(true);
    }
}
