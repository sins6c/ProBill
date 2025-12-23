import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JMenu;
import java.awt.Toolkit;
import java.io.File;

public class AdminPanel extends JFrame implements ActionListener {
	JMenuItem itmAddProduct;
	JMenu mnProduct;
	JMenuItem itmUpdateProduct;
	JMenuItem itmDeleteProduct;
	JMenu mnCashier;
	JMenuItem itmDeleteCashier;
	JMenuItem itmAddCashier;
	JMenu mnStock;
	JMenuItem itmShowStock;
	JMenu mnExport;
	ArrayList<JPanel> panels = new ArrayList<JPanel>();
	int cPanel = 0;
	private JMenu mnSearch;
	private JMenuItem mntmSearchProduct;
	private JMenuItem mntmSearchCashier;
	private JMenu mnSale;
	private JMenuItem mntmPrintSale;

	/**
	 * Create the frame.
	 */
	public AdminPanel() {
		setIconImage(
				Toolkit.getDefaultToolkit().getImage("F:\\Working Directory\\fianl project with sql\\Bill\\logo.png"));
		setTitle("Admin Panel");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 840, 619);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnProduct = new JMenu("Product");
		menuBar.add(mnProduct);

		itmAddProduct = new JMenuItem("Add Product");
		mnProduct.add(itmAddProduct);
		itmAddProduct.addActionListener(this);

		itmUpdateProduct = new JMenuItem("Update Product");
		mnProduct.add(itmUpdateProduct);
		itmUpdateProduct.addActionListener(this);

		itmDeleteProduct = new JMenuItem("Delete Product");
		mnProduct.add(itmDeleteProduct);
		itmDeleteProduct.addActionListener(this);

		JMenu mnCustomer = new JMenu("Customer");
		menuBar.add(mnCustomer);

		JMenuItem itmManageCustomers = new JMenuItem("Manage Customers");
		mnCustomer.add(itmManageCustomers);
		itmManageCustomers.addActionListener(this);

		mnCashier = new JMenu("Cashier");
		menuBar.add(mnCashier);

		itmAddCashier = new JMenuItem("Add Cashier");
		mnCashier.add(itmAddCashier);
		itmAddCashier.addActionListener(this);

		JMenuItem itmUpdateCashier = new JMenuItem("Update Cashier");
		mnCashier.add(itmUpdateCashier);
		itmUpdateCashier.addActionListener(this);

		itmDeleteCashier = new JMenuItem("Delete Cashier");
		mnCashier.add(itmDeleteCashier);
		itmDeleteCashier.addActionListener(this);

		mnStock = new JMenu("Stock");
		menuBar.add(mnStock);

		itmShowStock = new JMenuItem("Show Stock");
		mnStock.add(itmShowStock);
		itmShowStock.addActionListener(this);

		mnSearch = new JMenu("Search");
		menuBar.add(mnSearch);

		mntmSearchProduct = new JMenuItem("Search Product");
		mnSearch.add(mntmSearchProduct);
		mntmSearchProduct.addActionListener(this);

		mntmSearchCashier = new JMenuItem("Search Cashier");
		mnSearch.add(mntmSearchCashier);

		mnSale = new JMenu("Sale");
		menuBar.add(mnSale);

		mntmPrintSale = new JMenuItem("Print Sale");
		mnSale.add(mntmPrintSale);
		mntmPrintSale.addActionListener(this);

		// Feature 9: Reports Menu
		JMenu mnReports = new JMenu("Reports");
		menuBar.add(mnReports);

		JMenuItem mntmViewReports = new JMenuItem("View Analytics");
		mnReports.add(mntmViewReports);
		mntmViewReports.addActionListener(this);

		JMenu mnExportData = new JMenu("Export");
		menuBar.add(mnExportData);

		JMenuItem mntmExportSales = new JMenuItem("Export Sales Data");
		mnExportData.add(mntmExportSales);
		mntmExportSales.addActionListener(this);

		JMenuItem mntmExportStock = new JMenuItem("Export Stock Data");
		mnExportData.add(mntmExportStock);
		mntmExportStock.addActionListener(this);

		mnExport = new JMenu("Account");
		menuBar.add(mnExport);

		JMenuItem dashboard = new JMenuItem("Dashboard");
		mnExport.add(dashboard);
		dashboard.addActionListener(this);

		JMenuItem logout = new JMenuItem("Logout");
		mnExport.add(logout);
		logout.addActionListener(this);

		JMenuItem toggleTheme = new JMenuItem("Dark Mode Toggle");
		mnExport.add(toggleTheme);
		toggleTheme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Theme.toggle();
				Theme.applyTheme(AdminPanel.this.getContentPane());
				if (cPanel >= 0 && cPanel < panels.size()) {
					Theme.applyTheme(panels.get(cPanel));
				}
				for (JPanel p : panels) {
					Theme.applyTheme(p);
				}
				AdminPanel.this.repaint();
				AdminPanel.this.revalidate();
			}
		});

		mntmSearchCashier.addActionListener(this);

		getContentPane().setLayout(new BorderLayout(0, 0));

		panels.add(new addProduct());
		panels.add(new updateProduct());
		panels.add(new deleteProduct());
		panels.add(new addCashier());
		panels.add(new deleteCashier());
		panels.add(new showStock());
		panels.add(new searchProduct());
		panels.add(new searchCashier());
		panels.add(new Sale());
		panels.add(new updateCashier());
		panels.add(new Dashboard());
		panels.add(new Reports());
		panels.add(new CustomerPanel()); // Index 12
		getContentPane().add(panels.get(10)); // Default to Dashboard
		cPanel = 10;
		this.setTitle("Dashboard");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Selected: " + e.getActionCommand());
		if (e.getActionCommand().equals("Add Product")) {
			System.out.println(panels.get(cPanel));
			this.remove(panels.get(cPanel));
			this.revalidate();
			this.repaint();
			getContentPane().add(panels.get(0));
			this.setVisible(true);
			cPanel = 0;
			this.setTitle("Add Product");
		} else if (e.getActionCommand().equals("Update Product")) {
			this.remove(panels.get(cPanel));
			this.revalidate();
			this.repaint();
			getContentPane().add(panels.get(1));
			this.setVisible(true);
			cPanel = 1;
			this.setTitle("Update Product");
		} else if (e.getActionCommand().equals("Delete Product")) {
			this.remove(panels.get(cPanel));
			this.revalidate();
			this.repaint();
			getContentPane().add(panels.get(2));
			this.setVisible(true);
			cPanel = 2;
			this.setTitle("Delete Product");
		} else if (e.getActionCommand().equals("Add Cashier")) {
			this.remove(panels.get(cPanel));
			this.revalidate();
			this.repaint();
			getContentPane().add(panels.get(3));
			this.setVisible(true);
			cPanel = 3;
			this.setTitle("Add Cashier");
		} else if (e.getActionCommand().equals("Delete Cashier")) {
			this.remove(panels.get(cPanel));
			this.revalidate();
			this.repaint();
			getContentPane().add(panels.get(4));
			this.setVisible(true);
			cPanel = 4;
			this.setTitle("Delete Cashier");
		} else if (e.getActionCommand().equals("Update Cashier")) {
			this.remove(panels.get(cPanel));
			this.revalidate();
			this.repaint();
			getContentPane().add(panels.get(9)); // Index 9 for updateCashier
			this.setVisible(true);
			cPanel = 9;
			this.setTitle("Update Cashier");
		} else if (e.getActionCommand().equals("Show Stock")) {
			this.remove(panels.get(cPanel));
			getContentPane().add(panels.get(5));
			this.revalidate();
			this.repaint();
			this.setVisible(true);
			cPanel = 5;
			this.setTitle("Show Stock");
		} else if (e.getActionCommand().equals("Search Product")) {
			this.remove(panels.get(cPanel));
			getContentPane().add(panels.get(6));
			this.revalidate();
			this.repaint();
			this.setVisible(true);
			cPanel = 6;
			this.setTitle("Search Product");
		} else if (e.getActionCommand().equals("Search Cashier")) {
			this.remove(panels.get(cPanel));
			getContentPane().add(panels.get(7));
			this.revalidate();
			this.repaint();
			this.setVisible(true);
			cPanel = 7;
			this.setTitle("Search Cashier");
		} else if (e.getActionCommand().equals("Print Sale")) {
			this.remove(panels.get(cPanel));
			getContentPane().add(panels.get(8));
			this.revalidate();
			this.repaint();
			this.setVisible(true);
			cPanel = 8;
			this.setTitle("Print Sale");
		} else if (e.getActionCommand().equals("Dashboard")) {
			this.remove(panels.get(cPanel));
			getContentPane().add(panels.get(10));
			this.revalidate();
			this.repaint();
			this.setVisible(true);
			cPanel = 10;
			this.setTitle("Dashboard");
			((Dashboard) panels.get(10)).updateStats();
		} else if (e.getActionCommand().equals("Logout")) {
			this.dispose();
		} else if (e.getActionCommand().equals("Export Sales Data")) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Save Sales Data");
			fileChooser.setSelectedFile(new File("sales_data.csv"));
			int result = fileChooser.showSaveDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				String filepath = fileChooser.getSelectedFile().getAbsolutePath();
				if (!filepath.endsWith(".csv")) {
					filepath += ".csv";
				}
				ExportToCSV.exportSales(filepath);
			}
		} else if (e.getActionCommand().equals("Export Stock Data")) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Save Stock Data");
			fileChooser.setSelectedFile(new File("stock_data.csv"));
			int result = fileChooser.showSaveDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				String filepath = fileChooser.getSelectedFile().getAbsolutePath();
				if (!filepath.endsWith(".csv")) {
					filepath += ".csv";
				}
				ExportToCSV.exportStock(filepath);
			}
		} else if (e.getActionCommand().equals("View Analytics")) {
			this.remove(panels.get(cPanel));
			this.revalidate();
			this.repaint();
			getContentPane().add(panels.get(11)); // Reports is index 11
			cPanel = 11;
			this.setTitle("Reports & Analytics");
		} else if (e.getActionCommand().equals("Manage Customers")) {
			this.remove(panels.get(cPanel));
			getContentPane().add(panels.get(12));
			this.revalidate();
			this.repaint();
			this.setVisible(true);
			cPanel = 12;
			this.setTitle("Manage Customers");
			((CustomerPanel) panels.get(12)).refreshTable();
		}
	}
}
