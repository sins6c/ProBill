import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class Dashboard extends JPanel {

    private JLabel lblTotalProductsVal;
    private JLabel lblTotalStockVal;
    private JLabel lblTodaySalesVal;
    private JLabel lblLowStockVal;
    private JPanel pnlLowStock;

    public Dashboard() {
        setLayout(null);
        setBounds(100, 100, 840, 619);

        JLabel lblDashboard = new JLabel("DASHBOARD");
        lblDashboard.setBounds(328, 25, 182, 30);
        lblDashboard.setFont(new Font("Tahoma", Font.BOLD, 20));
        add(lblDashboard);

        // Card 1: Total Products
        JPanel pnlProducts = createCard("Total Products", 50, 80);
        lblTotalProductsVal = new JLabel("0");
        lblTotalProductsVal.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblTotalProductsVal.setHorizontalAlignment(SwingConstants.CENTER);
        pnlProducts.add(lblTotalProductsVal);
        add(pnlProducts);

        // Card 2: Total Stock Value
        JPanel pnlStock = createCard("Total Stock Value", 300, 80);
        lblTotalStockVal = new JLabel("0.0");
        lblTotalStockVal.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblTotalStockVal.setHorizontalAlignment(SwingConstants.CENTER);
        pnlStock.add(lblTotalStockVal);
        add(pnlStock);

        // Card 3: Today's Sales
        JPanel pnlSales = createCard("Today's Sales", 550, 80);
        lblTodaySalesVal = new JLabel("0.0");
        lblTodaySalesVal.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblTodaySalesVal.setHorizontalAlignment(SwingConstants.CENTER);
        pnlSales.add(lblTodaySalesVal);
        pnlSales.add(lblTodaySalesVal);
        add(pnlSales);

        // Feature 10: Low Stock Alerts
        pnlLowStock = createCard("Low Stock Alerts", 50, 240); // Second row
        lblLowStockVal = new JLabel("0");
        lblLowStockVal.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblLowStockVal.setHorizontalAlignment(SwingConstants.CENTER);
        pnlLowStock.add(lblLowStockVal);
        add(pnlLowStock);

        JButton btnRefresh = new JButton("Refresh Stats");
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateStats();
            }
        });
        btnRefresh.setBounds(350, 300, 140, 30);
        add(btnRefresh);

        updateStats(); // Load initial data
    }

    private JPanel createCard(String title, int x, int y) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, 220, 150);
        panel.setLayout(new GridLayout(2, 1));
        panel.setBorder(new LineBorder(Color.GRAY, 2));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitle);

        return panel;
    }

    public void updateStats() {
        lblTotalProductsVal.setText(String.valueOf(DB.getTotalProducts()));
        lblTotalStockVal.setText(String.format("%.2f", DB.getTotalStockValue()));
        lblTodaySalesVal.setText(String.format("%.2f", DB.getTodaysSales()));

        int lowStock = DB.getLowStockCount();
        lblLowStockVal.setText(String.valueOf(lowStock));

        if (lowStock > 0) {
            pnlLowStock.setBackground(new Color(255, 102, 102)); // Light Red
            lblLowStockVal.setForeground(Color.WHITE);
        } else {
            pnlLowStock.setBackground(new Color(240, 240, 240)); // Default gray/white
            lblLowStockVal.setForeground(Color.BLACK);
        }
    }
}
