import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class showStock extends JPanel {
	private JTable stockTable;
	JComboBox<String> comp;
	DefaultTableModel model;

	/**
	 * Create the panel.
	 */
	public showStock() {
		setLayout(null);
		setBounds(100, 100, 840, 619);
		JLabel lblStock = new JLabel("AVAILABLE STOCK");
		lblStock.setBounds(328, 26, 182, 21);
		lblStock.setFont(new Font("Tahoma", Font.PLAIN, 17));
		add(lblStock);

		model = new DefaultTableModel();
		stockTable = new JTable(model) {
			@Override
			public java.awt.Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row,
					int column) {
				java.awt.Component c = super.prepareRenderer(renderer, row, column);
				if (!isRowSelected(row)) {
					try {
						int quantity = Integer.parseInt(getModel().getValueAt(row, 3).toString());
						int minLimit = Integer.parseInt(getModel().getValueAt(row, 5).toString());
						if (quantity < minLimit) {
							c.setBackground(Color.RED);
						} else {
							c.setBackground(Color.WHITE);
						}
					} catch (Exception e) {
						c.setBackground(Color.WHITE);
					}
				}
				return c;
			}
		};
		stockTable.setBounds(98, 112, 645, 397);
		add(stockTable);
		model.addColumn("Product ID");
		model.addColumn("Product Detail");
		model.addColumn("Company");
		model.addColumn("Quantity");
		model.addColumn("Price");
		model.addColumn("Min Limit");
		JScrollPane scroll = new JScrollPane(stockTable);
		scroll.setBounds(98, 112, 645, 397);
		add(scroll);

		comp = new JComboBox<String>();
		comp.setBackground(Color.WHITE);
		comp.setBounds(583, 81, 160, 20);
		add(comp);
		comp.addItem("All");
		comp.addItem("General");
		comp.addItem("Mats & Rugs");
		comp.addItem("N/S & Electric");
		comp.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO Auto-generated method stub
				updateTable();
			}
		});

		JLabel lblCompany = new JLabel("Company");
		lblCompany.setBounds(582, 68, 161, 14);
		add(lblCompany);

		JButton btnExportToExcel = new JButton("Export to Excel");
		btnExportToExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (model.getRowCount() == 0) {
					JOptionPane.showMessageDialog(null, "No data to export!");
					return;
				}
				ArrayList<String[]> data = new ArrayList<>();
				for (int i = 0; i < model.getRowCount(); i++) {
					String[] row = new String[model.getColumnCount()];
					for (int j = 0; j < model.getColumnCount(); j++) {
						row[j] = model.getValueAt(i, j).toString();
					}
					data.add(row);
				}
				String[] headers = new String[model.getColumnCount()];
				for (int i = 0; i < model.getColumnCount(); i++) {
					headers[i] = model.getColumnName(i);
				}

				javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
				fileChooser.setDialogTitle("Save Stock Data");
				fileChooser.setSelectedFile(new File("stock_data.csv"));
				if (fileChooser.showSaveDialog(showStock.this) == javax.swing.JFileChooser.APPROVE_OPTION) {
					String path = fileChooser.getSelectedFile().getAbsolutePath();
					if (!path.endsWith(".csv"))
						path += ".csv";
					if (ExportToCSV.writeCSV(path, headers, data)) {
						JOptionPane.showMessageDialog(null, "Export successful!");
					}
				}
			}
		});
		btnExportToExcel.setBounds(605, 525, 138, 23);
		add(btnExportToExcel);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateTable();
			}
		});
		btnRefresh.setBounds(457, 525, 138, 23);
		add(btnRefresh);
		updateTable();

	}

	public void updateTable() {
		model.setRowCount(0);
		ArrayList<String> stock = new ArrayList<String>();
		stock = DB.showStock(comp.getSelectedItem().toString());
		for (int x = 0; x < stock.size(); x += 6) {
			model.addRow(new Object[] { stock.get(x), stock.get(x + 1), stock.get(x + 2), stock.get(x + 3),
					stock.get(x + 4), stock.get(x + 5) });
		}
	}

}
