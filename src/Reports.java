import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class Reports extends JPanel {
    private JTextField txtFromDate;
    private JTextField txtToDate;
    private JLabel lblTotalRevenueVal;
    private JLabel lblTxCountVal;
    private JLabel lblAvgSaleVal;
    private JTable salesTable;
    private DefaultTableModel salesModel;
    private JTable topProductsTable;
    private DefaultTableModel topProductsModel;

    /**
     * Create the panel.
     */
    public Reports() {
        setLayout(null);
        setBounds(100, 100, 840, 619);

        JLabel lblTitle = new JLabel("REPORTS & ANALYTICS");
        lblTitle.setBounds(300, 10, 250, 25);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblTitle);

        // Date Selection Panel
        JPanel pnlDate = new JPanel();
        pnlDate.setBorder(new LineBorder(Color.GRAY));
        pnlDate.setBounds(50, 45, 740, 50);
        pnlDate.setLayout(null);
        add(pnlDate);

        JLabel lblFrom = new JLabel("From (yyyy/MM/dd):");
        lblFrom.setBounds(20, 15, 120, 20);
        pnlDate.add(lblFrom);

        txtFromDate = new JTextField();
        txtFromDate.setBounds(140, 15, 100, 20);
        // Default to first day of current month
        txtFromDate.setText(LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        pnlDate.add(txtFromDate);
        txtFromDate.setColumns(10);

        JLabel lblTo = new JLabel("To (yyyy/MM/dd):");
        lblTo.setBounds(260, 15, 120, 20);
        pnlDate.add(lblTo);

        txtToDate = new JTextField();
        txtToDate.setBounds(370, 15, 100, 20);
        // Default to today
        txtToDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        pnlDate.add(txtToDate);
        JButton btnGenerate = new JButton("Generate Report");
        btnGenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
        btnGenerate.setBounds(450, 14, 150, 23);
        pnlDate.add(btnGenerate);

        JButton btnExport = new JButton("Export to CSV");
        btnExport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (salesModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "No data to export!");
                    return;
                }
                ArrayList<String[]> data = new ArrayList<>();
                for (int i = 0; i < salesModel.getRowCount(); i++) {
                    String[] row = new String[salesModel.getColumnCount()];
                    for (int j = 0; j < salesModel.getColumnCount(); j++) {
                        row[j] = salesModel.getValueAt(i, j).toString();
                    }
                    data.add(row);
                }
                String[] headers = new String[salesModel.getColumnCount()];
                for (int i = 0; i < salesModel.getColumnCount(); i++) {
                    headers[i] = salesModel.getColumnName(i);
                }

                // Use a FileChooser or just default
                javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
                fileChooser.setDialogTitle("Save Report");
                fileChooser.setSelectedFile(new java.io.File("sales_report.csv"));
                if (fileChooser.showSaveDialog(Reports.this) == javax.swing.JFileChooser.APPROVE_OPTION) {
                    String path = fileChooser.getSelectedFile().getAbsolutePath();
                    if (!path.endsWith(".csv"))
                        path += ".csv";
                    if (ExportToCSV.writeCSV(path, headers, data)) {
                        JOptionPane.showMessageDialog(null, "Export successful!");
                    }
                }
            }
        });
        btnExport.setBounds(610, 14, 120, 23);
        pnlDate.add(btnExport);

        // Summary Cards
        JPanel pnlSummary = new JPanel();
        pnlSummary.setBounds(50, 105, 740, 80);
        pnlSummary.setLayout(null);
        add(pnlSummary);

        // Card 1: Revenue
        JPanel pnlRev = createSummaryCard("Total Revenue", 0, 0);
        lblTotalRevenueVal = (JLabel) pnlRev.getComponent(1);
        pnlSummary.add(pnlRev);

        // Card 2: Transactions
        JPanel pnlTx = createSummaryCard("Transactions", 260, 0);
        lblTxCountVal = (JLabel) pnlTx.getComponent(1);
        pnlSummary.add(pnlTx);

        // Card 3: Avg Sale
        JPanel pnlAvg = createSummaryCard("Avg Sale Value", 520, 0);
        lblAvgSaleVal = (JLabel) pnlAvg.getComponent(1);
        pnlSummary.add(pnlAvg);

        // Tabbed Tables
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(50, 200, 740, 380);
        add(tabbedPane);

        // Tab 1: Sales History
        JPanel pnlSalesList = new JPanel();
        pnlSalesList.setLayout(null);

        salesModel = new DefaultTableModel();
        salesModel.addColumn("Date");
        salesModel.addColumn("Product ID");
        salesModel.addColumn("Product Name"); // Mapped from 'Company' or 'Name'? DB query shows cols: Date, ID,
                                              // Company, Qty, Pay, Name, Disc.
        // Let's verify DB.getSalesByDateRange mapping:
        // row[0]=Date, row[1]=ID, row[2]=Company, row[3]=Qty, row[4]=Pay,
        // row[5]=Name(Customer?), row[6]=Discount
        // Wait, 'Name' in sale table is usually Customer Name? Or Product Name?
        // Checking DB.addSaleToDB: INSERT INTO sale VALUES (..., name) -> name is
        // passed from Invoice.
        // In Invoice.java: DB.addSaleToDB(data, comp, n, discountRate); -> n is
        // customer name/number?
        // Let's check DB.java again to be sure.
        // row[5] is Name.

        salesModel.addColumn("Company");
        salesModel.addColumn("Qty");
        salesModel.addColumn("Amount");
        salesModel.addColumn("Customer");
        salesModel.addColumn("Discount");

        salesTable = new JTable(salesModel);
        JScrollPane scrollSales = new JScrollPane(salesTable);
        scrollSales.setBounds(0, 0, 735, 350);
        pnlSalesList.add(scrollSales);
        tabbedPane.addTab("Sales History", null, pnlSalesList, null);

        // Tab 2: Top Products
        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(null);

        topProductsModel = new DefaultTableModel();
        topProductsModel.addColumn("Product ID");
        topProductsModel.addColumn("Total Revenue");
        topProductsModel.addColumn("Units Sold");

        topProductsTable = new JTable(topProductsModel);
        JScrollPane scrollTop = new JScrollPane(topProductsTable);
        scrollTop.setBounds(0, 0, 735, 350);
        pnlTop.add(scrollTop);
        tabbedPane.addTab("Top 10 Products", null, pnlTop, null);

        // Initial Load
        generateReport();
    }

    private JPanel createSummaryCard(String title, int x, int y) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, 220, 80);
        panel.setBorder(new LineBorder(Color.GRAY));
        panel.setLayout(null);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setBounds(10, 10, 200, 20);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitle);

        JLabel lblVal = new JLabel("0");
        lblVal.setBounds(10, 35, 200, 30);
        lblVal.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblVal.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblVal);

        return panel;
    }

    private void generateReport() {
        String from = txtFromDate.getText().trim();
        String to = txtToDate.getText().trim();

        // 1. Update Summaries
        String[] summary = DB.getSalesSummary(from, to);
        lblTotalRevenueVal.setText(summary[0]);
        lblTxCountVal.setText(summary[1]);
        lblAvgSaleVal.setText(summary[2]);

        // 2. Update Sales Table
        salesModel.setRowCount(0);
        ArrayList<String[]> sales = DB.getSalesByDateRange(from, to);
        for (String[] row : sales) {
            // row: Date, ID, Company, Qty, Pay, Name, Disc
            salesModel.addRow(new Object[] { row[0], row[1], row[2], row[3], row[4], row[5], row[6] });
        }

        // 3. Update Top Products
        topProductsModel.setRowCount(0);
        ArrayList<String[]> top = DB.getTopProducts(10);
        for (String[] row : top) {
            topProductsModel.addRow(new Object[] { row[0], row[1], row[2] });
        }
    }
}
