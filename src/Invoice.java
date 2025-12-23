
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class Invoice extends JPanel {
	private JTextField name;
	private JTextField pID;
	private JTextField pQuan;
	private JTable items;
	private JTextField UnitPrice;
	JLabel lblName;
	JLabel error;
	static int invo = 1;
	DefaultTableModel dtm;
	Object data[];
	JComboBox<String> cType;
	ArrayList<String> comp = new ArrayList<String>();
	private JTextField dField;
	double total = 0;
	JLabel gtotal;

	/**
	 * Create the panel.
	 */
	public Invoice() {
		setLayout(null);

		JLabel lblCustomer = new JLabel("Customer");
		lblCustomer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCustomer.setBounds(88, 59, 97, 23);
		add(lblCustomer);

		cType = new JComboBox<String>();
		cType.setBounds(201, 62, 89, 20);
		add(cType);
		cType.addItem("Walk-in customer");
		cType.addItem("Company/customer name");
		cType.setSelectedIndex(1);
		cType.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO Auto-generated method stub
				if (cType.getSelectedIndex() == 1) {
					lblName.setVisible(true);
					name.setVisible(true);
				} else {
					lblName.setVisible(false);
					name.setVisible(false);
				}
			}
		});
		lblName = new JLabel("Name");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblName.setBounds(364, 59, 64, 23);
		add(lblName);

		name = new JTextField();
		name.setBounds(438, 62, 150, 20);
		add(name);
		name.setColumns(10);

		JLabel lblProductId = new JLabel("Product ID");
		lblProductId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblProductId.setBounds(88, 383, 80, 23);
		add(lblProductId);

		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblQuantity.setBounds(88, 174, 97, 23);
		add(lblQuantity);

		pID = new JTextField();
		pID.setBounds(201, 138, 89, 20);
		add(pID);
		pID.setColumns(10);

		pQuan = new JTextField();
		// Moved pID listener after btnAdd creation due to scope

		pQuan.setColumns(10);
		pQuan.setBounds(201, 177, 89, 20);
		add(pQuan);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (pQuan.getText().equals("") || pID.getText().equals("")) {
					error.setText("Enter required data");
					return;
				} else
					error.setText("");

				double up, fp;
				int q;
				String id, detail = "";

				q = Integer.parseInt(pQuan.getText().trim());
				id = pID.getText().trim();

				detail = DB.searchPDetail(id, q);
				String str[] = detail.split("%");
				detail = str[0];
				if (detail.equals("nill")) {
					error.setText("Invalid product id!");

					// Feature 11b: Online Lookup
					final String barcode = id;
					// Use a separate thread to avoid freezing UI during network call
					new Thread(new Runnable() {
						public void run() {
							String onlineProduct = ProductLookup.fetchProductDetails(barcode);
							if (onlineProduct != null) {
								int confirm = javax.swing.JOptionPane.showConfirmDialog(null,
										"Product found online:\n" + onlineProduct
												+ "\n\nDo you want to add this to your stock?",
										"Product Found Online", javax.swing.JOptionPane.YES_NO_OPTION);

								if (confirm == javax.swing.JOptionPane.YES_OPTION) {
									// Open addProduct panel, ideally pre-filled.
									// For now, simpler approach: Just switch to Add Product panel
									// and maybe we can set static fields there or pass data.
									// Since addProduct is a JPanel in AdminPanel's list, accessing it is tricky
									// from here
									// without a reference to AdminPanel or making fields static.
									// Strategy: Show a small Quick Add dialog right here.

									String priceStr = javax.swing.JOptionPane.showInputDialog(null,
											"Enter Selling Price for " + onlineProduct + ":");
									if (priceStr == null)
										return;
									String qtyStr = javax.swing.JOptionPane.showInputDialog(null,
											"Enter Initial Quantity:");
									if (qtyStr == null)
										return;

									try {
										double price = Double.parseDouble(priceStr);
										int qty = Integer.parseInt(qtyStr);
										// Default company "General" and min limit 10
										DB.addProductToDB(barcode, onlineProduct, "General", qty, price, 10);

										// Now auto-add to the current invoice cart
										// We need to re-trigger the add button logic safely
										javax.swing.SwingUtilities.invokeLater(new Runnable() {
											public void run() {
												pID.setText(barcode);
												pQuan.setText("1"); // Default to 1 for current sale
												btnAdd.doClick();
											}
										});

									} catch (NumberFormatException ex) {
										javax.swing.JOptionPane.showMessageDialog(null,
												"Invalid Price or Quantity entered.");
									}
								}
							}
						}
					}).start();

					return;
				} else if (detail.equals("item is out of stock")) {
					error.setText(detail);
					return;
				} else {
					error.setText("");
					comp.add(str[1]);
					// Parse DB price
					double dbPrice = 0.0;
					if (str.length >= 3) {
						try {
							dbPrice = Double.parseDouble(str[2]);
						} catch (Exception ex) {
						}
					}

					if (UnitPrice.getText().trim().equals("")) {
						up = dbPrice;
					} else {
						try {
							up = Double.parseDouble(UnitPrice.getText().trim());
						} catch (NumberFormatException e) {
							up = dbPrice; // Fallback or strict error? Fallback seems safer for now
						}
					}
				}
				fp = (up * q);
				UnitPrice.setText("");
				pQuan.setText("");
				pID.setText("");

				dtm.addRow(new Object[] { id, detail, up, q, fp });
				total += fp;
				gtotal.setText(total + "");

			}
		});
		btnAdd.setBounds(201, 265, 89, 23);
		add(btnAdd);

		// Feature 11: Barcode Scanner Support (Enter key acts as Add)
		pID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAdd.doClick();
				pID.requestFocusInWindow();
			}
		});

		String[] header = { "Product ID", "Item Details", "Unit Price", "Quantity", "Final Price" };
		dtm = new DefaultTableModel(header, 0);
		items = new JTable(dtm);
		items.setBounds(361, 135, 316, 298);
		// add(items);
		JScrollPane s = new JScrollPane(items);
		s.setEnabled(false);
		s.setBounds(361, 135, 392, 265);
		add(s);

		UnitPrice = new JTextField();
		UnitPrice.setColumns(10);
		UnitPrice.setBounds(201, 220, 89, 20);
		add(UnitPrice);

		JLabel lblUnitPrice = new JLabel("Unit Price");
		lblUnitPrice.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblUnitPrice.setBounds(88, 217, 97, 23);
		add(lblUnitPrice);

		JButton btnPrint = new JButton("PRINT");
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int x = 0;
				String n = "";
				if (cType.getSelectedIndex() == 1)
					n = name.getText().trim();
				data = new Object[dtm.getColumnCount() * dtm.getRowCount()];
				for (int row = 0; row < dtm.getRowCount(); row++) {
					for (int col = 0; col < dtm.getColumnCount(); col++) {
						data[x] = items.getValueAt(row, col);
						x++;
					}
				}
				pdfGenerator.makePdf(data, total, invo, discountRate);
				invo++;
				DB.addSaleToDB(data, comp, n, discountRate);
			}
		});
		btnPrint.setBounds(664, 411, 89, 52);
		btnPrint.setBounds(664, 411, 89, 52);
		add(btnPrint);

		// Feature 14: Email Invoice
		JButton btnEmail = new JButton("EMAIL");
		btnEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (dtm.getRowCount() == 0) {
					javax.swing.JOptionPane.showMessageDialog(null, "Cart is empty!");
					return;
				}

				StringBuilder body = new StringBuilder();
				body.append("Invoice Details:\n\n");
				body.append(String.format("%-15s %-30s %-10s %-10s %-10s\n", "ID", "Detail", "Unit", "Qty", "Total"));
				body.append("--------------------------------------------------------------------------\n");

				for (int row = 0; row < dtm.getRowCount(); row++) {
					body.append(String.format("%-15s %-30s %-10s %-10s %-10s\n",
							dtm.getValueAt(row, 0),
							dtm.getValueAt(row, 1),
							dtm.getValueAt(row, 2),
							dtm.getValueAt(row, 3),
							dtm.getValueAt(row, 4)));
				}

				body.append("\nTotal: " + total);
				if (discountRate > 0) {
					body.append("\nDiscount: " + discountRate + "%");
					body.append("\nNet Total: " + gtotal.getText());
				}
				body.append("\n\nThank you for shopping with us!");

				String toEmail = "";
				// Check if customer name is entered, maybe we can fetch their email?
				// For now, simple prompt
				// String nameStr = name.getText().trim();
				toEmail = javax.swing.JOptionPane.showInputDialog(null, "Enter Customer Email:", "");
				if (toEmail == null)
					return;

				EmailSender.openDraft(toEmail, "Invoice from Thangaraja Malligai", body.toString());
			}
		});
		btnEmail.setBounds(565, 411, 89, 52);
		add(btnEmail);

		error = new JLabel("");
		error.setForeground(Color.RED);
		error.setBounds(92, 319, 218, 14);
		add(error);

		JLabel lblDeleteProduct = new JLabel("DELETE PRODUCT");
		lblDeleteProduct.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDeleteProduct.setBounds(88, 344, 132, 14);
		add(lblDeleteProduct);

		JLabel label = new JLabel("Product ID");
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label.setBounds(88, 135, 80, 23);
		add(label);

		dField = new JTextField();
		dField.setColumns(10);
		dField.setBounds(201, 386, 89, 20);
		add(dField);

		JButton delbutton = new JButton("Delete");
		delbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String df = dField.getText().trim();
				for (int row = 0; row < dtm.getRowCount(); row++) {
					if (items.getValueAt(row, 0).equals(df)) {
						int q = (Integer) items.getValueAt(row, 3);
						String i = (String) items.getValueAt(row, 0);
						DB.updateProduct(i, q);
						total -= (Double) items.getValueAt(row, 4);
						dtm.removeRow(row);
						gtotal.setText(total + "");
						dField.setText("");
						break;
					}
				}
			}
		});
		delbutton.setBounds(201, 440, 89, 23);
		add(delbutton);

		JLabel lblGrandTotal = new JLabel("Grand total");
		lblGrandTotal.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblGrandTotal.setBounds(364, 449, 89, 14);
		add(lblGrandTotal);

		gtotal = new JLabel("");
		gtotal.setFont(new Font("Tahoma", Font.BOLD, 15));
		gtotal.setBounds(470, 449, 132, 14);
		add(gtotal);

		// dtm.addRow(new Object[]{"Product ID","Item Details","Unit
		// Price","Quantity","Final Price"});

		// Coupon UI
		JLabel lblCoupon = new JLabel("Coupon Code");
		lblCoupon.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCoupon.setBounds(88, 480, 100, 23);
		add(lblCoupon);

		couponField = new JTextField();
		couponField.setBounds(201, 480, 89, 20);
		add(couponField);

		JButton btnApplyCoupon = new JButton("Apply");
		btnApplyCoupon.setBounds(300, 480, 70, 23);
		add(btnApplyCoupon);

		lblDiscount = new JLabel("Discount: 0%");
		lblDiscount.setBounds(88, 510, 200, 20);
		add(lblDiscount);

		btnApplyCoupon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String code = couponField.getText().trim();
				int rate = DB.getDiscount(code);
				if (rate > 0) {
					discountRate = rate;
					lblDiscount.setText("Discount: " + rate + "% (" + code + ")");
					updateTotalLabel();
				} else {
					lblDiscount.setText("Invalid Coupon");
					discountRate = 0;
					updateTotalLabel();
				}
			}
		});

	}

	private JTextField couponField;
	private JLabel lblDiscount;
	double discountRate = 0;

	private void updateTotalLabel() {
		double netTotal = total * (1.0 - (discountRate / 100.0));
		gtotal.setText(String.format("%.2f", netTotal));
	}
}
