import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

public class DB {

	public static Connection DBConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			try {
				conn = DriverManager.getConnection("jdbc:mysql://localhost/caddey", "root", "212369");
			} catch (SQLException e) {
				// If basic connection fails, try prompting for password
				javax.swing.JPasswordField pf = new javax.swing.JPasswordField();
				int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter MySQL Root Password",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (okCxl == JOptionPane.OK_OPTION) {
					String pass = new String(pf.getPassword());
					conn = DriverManager.getConnection("jdbc:mysql://localhost/caddey", "root", pass);
				} else {
					throw e;
				}
			}
			System.out.print("Database is connected !");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Do not connect to DB - Error:" + e);

		}
		return conn;
	}

	public static void addProductToDB(String id, String detail, String comp, int quan, double price, int minLimit) {
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			statement.executeUpdate("INSERT INTO stock VALUES ('" + id + "','" + detail + "','" + comp + "'," + quan
					+ "," + price + "," + minLimit + ");");
			JOptionPane.showMessageDialog(null, "Product added to database");
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	public static void updateProductToDB(String id, String detail, String comp, int quan, double price, int minLimit) {
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			int status = statement.executeUpdate("UPDATE stock set Detail = '" + detail + "', Company = '" + comp
					+ "', Quantity = " + quan + ", Price = " + price + ", MinLimit = " + minLimit
					+ " WHERE ProductID = '" + id + "';");
			if (status == 1)
				JOptionPane.showMessageDialog(null, "Product updted");
			else
				JOptionPane.showMessageDialog(null, "ProductID not found!");
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}

	}

	public static void deleteProductToDB(String id) {
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			int status = statement.executeUpdate("DELETE from stock WHERE ProductID = '" + id + "';");
			if (status == 1)
				JOptionPane.showMessageDialog(null, "Product deleted");
			else
				JOptionPane.showMessageDialog(null, "ProductID not found!");
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}

	}

	public static void searchProduct(String id) {
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("Select * from stock WHERE ProductID = '" + id + "';");
			if (!rs.next())
				JOptionPane.showMessageDialog(null, "No product found with this id!");
			else
				JOptionPane.showMessageDialog(null, "ProductID: " + id + "\nQuantity: " + rs.getString("Quantity")
						+ "\nPrice: " + rs.getDouble("Price"));

			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	public static void searchCashier(String email) {
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("Select * from users WHERE Email = '" + email + "';");
			if (!rs.next())
				JOptionPane.showMessageDialog(null, "No cashier found with this email!");
			else
				JOptionPane.showMessageDialog(null, "Email: " + email + "\nPassword: " + rs.getString("Password"));

			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	public static boolean varifyLogin(String email, String pass) {
		boolean login = false;
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement
					.executeQuery("Select * from users WHERE Email = '" + email + "' and Password = '" + pass + "';");
			if (!rs.next())
				login = false;
			else
				login = true;

			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		return login;
	}

	public static void addCashier(String user, String pass) {
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			statement.executeUpdate("INSERT INTO users VALUES ('" + user + "','" + pass + "');");
			JOptionPane.showMessageDialog(null, "Cashier added to database");
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	public static void deleteCashier(String user, String pass) {
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			int status = statement
					.executeUpdate("DELETE from users WHERE Email = '" + user + "' AND Password = '" + pass + "';");
			if (status == 1)
				JOptionPane.showMessageDialog(null, "Cashier deleted");
			else
				JOptionPane.showMessageDialog(null, "Cashier not found!");
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	public static void updateCashier(String user, String pass) {
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			int status = statement
					.executeUpdate("UPDATE users set Password = '" + pass + "' WHERE Email = '" + user + "';");
			if (status == 1)
				JOptionPane.showMessageDialog(null, "Cashier updated");
			else
				JOptionPane.showMessageDialog(null, "Cashier not found!");
			conn.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	public static String searchPDetail(String id, int q) {
		Connection conn = DBConnection();
		String rt = "";
		try {
			int quan;
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("Select * from stock WHERE ProductID = '" + id + "';");
			if (!rs.next())
				rt = "nill";
			else {
				quan = Integer.parseInt(rs.getString("Quantity")) - q;
				if (quan < 0)
					rt = "item is out of stock";
				else {
					rt = rs.getString("Detail") + "%" + rs.getString("Company") + "%" + rs.getDouble("Price");
					statement.executeUpdate("UPDATE stock set Quantity = " + quan + " WHERE ProductID = '" + id + "';");
				}

			}

			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		return rt;
	}

	public static void addSaleToDB(Object data[], ArrayList<String> comp, String name, double discountRate) {
		Connection conn = DBConnection();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		String d = dateFormat.format(date);
		try {
			Statement statement = conn.createStatement();
			for (int x = 0; x < data.length; x = x + 5) {
				statement.executeUpdate("INSERT INTO sale VALUES ('" + data[x] + "','" + comp.get(0) + "','" + d + "','"
						+ data[x + 3] + "'," + data[x + 4] + ",'" + name + "'," + discountRate + ");");
				comp.remove(0);
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	public static ArrayList<String> getSale(String date, String comp) {
		String q;
		ArrayList<String> r = new ArrayList<String>();

		if (comp.equals("All"))
			q = "Select * from sale WHERE Date = '" + date + "';";
		else
			q = "Select * from sale WHERE Date = '" + date + "' AND Company = '" + comp + "';";
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(q);
			while (rs.next()) {
				r.add(rs.getString("Date"));
				r.add(rs.getString("ProductID"));
				r.add(rs.getString("Company"));
				r.add(rs.getString("Payment"));
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		return r;
	}

	public static ArrayList<String> showStock(String comp) {
		String q;
		ArrayList<String> r = new ArrayList<String>();
		if (comp.equals("All"))
			q = "Select * from stock;";
		else
			q = "Select * from stock WHERE Company = '" + comp + "';";
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(q);
			while (rs.next()) {
				r.add(rs.getString("ProductID"));
				r.add(rs.getString("Detail"));
				r.add(rs.getString("Company"));
				r.add(rs.getString("Quantity"));
				try {
					r.add(String.valueOf(rs.getDouble("Price")));
				} catch (SQLException ex) {
					r.add("0.0"); // Fallback if column missing or null, though we added it
				}
				try {
					r.add(String.valueOf(rs.getInt("MinLimit")));
				} catch (SQLException ex) {
					r.add("0"); // Fallback
				}
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		return r;
	}

	public static String getPDetail(String id, int q) {
		Connection conn = DBConnection();
		String rt = "";
		try {
			int quan;
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("Select * from stock WHERE ProductID = '" + id + "';");
			if (!rs.next())
				rt = "nill";
			else {
				quan = Integer.parseInt(rs.getString("Quantity")) - q;
				if (quan < 0)
					rt = "item is out of stock";
				else {
					rt = rs.getString("Detail") + "%" + rs.getString("Company") + "%" + rs.getDouble("Price");
					statement.executeUpdate("UPDATE stock set Quantity = " + quan + " WHERE ProductID = '" + id + "';");
				}

			}

			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		return rt;
	}

	public static ArrayList<String> searchP(String id) {
		Connection conn = DBConnection();
		ArrayList<String> data = new ArrayList<String>();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("Select * from stock WHERE ProductID = '" + id + "';");
			if (rs.next()) {
				data.add(rs.getString("Detail"));
				data.add(rs.getString("Company"));
				data.add(rs.getString("Quantity"));
				data.add(String.valueOf(rs.getDouble("Price")));
				try {
					data.add(String.valueOf(rs.getInt("MinLimit")));
				} catch (SQLException e) {
					data.add("0");
				}
			}

			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		return data;
	}

	public static void updateProduct(String id, int quan) {
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("Select * from stock WHERE ProductID = '" + id + "';");
			int q = 0;
			if (rs.next()) {
				q = Integer.parseInt(rs.getString("Quantity")) + quan;
				statement.executeUpdate("UPDATE stock set Quantity = " + q + " WHERE ProductID = '" + id + "';");

			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}

	}

	public static int getTotalProducts() {
		int count = 0;
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM stock");
			if (rs.next()) {
				count = rs.getInt(1);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public static double getTotalStockValue() {
		double value = 0;
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT SUM(Quantity * Price) FROM stock");
			if (rs.next()) {
				value = rs.getDouble(1);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static double getTodaysSales() {
		double sales = 0;
		Connection conn = DBConnection();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		String d = dateFormat.format(date);
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT SUM(Payment) FROM sale WHERE Date = '" + d + "'");
			if (rs.next()) {
				sales = rs.getDouble(1);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sales;
	}

	public static int getDiscount(String code) {
		int rate = 0;
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT Rate FROM coupons WHERE Code = '" + code + "'");
			if (rs.next()) {
				rate = rs.getInt("Rate");
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rate;
	}

	public static ArrayList<String[]> getSalesDataForExport() {
		ArrayList<String[]> data = new ArrayList<String[]>();
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM sale");
			while (rs.next()) {
				String[] row = new String[7];
				row[0] = rs.getString("ProductID");
				row[1] = rs.getString("Company");
				row[2] = rs.getString("Date");
				row[3] = rs.getString("Quantity");
				row[4] = String.valueOf(rs.getDouble("Payment"));
				row[5] = rs.getString("Name");
				try {
					row[6] = String.valueOf(rs.getDouble("Discount"));
				} catch (SQLException e) {
					row[6] = "0.0"; // Fallback if column doesn't exist
				}
				data.add(row);
			}
			conn.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		return data;
	}

	public static ArrayList<String[]> getStockDataForExport() {
		ArrayList<String[]> data = new ArrayList<String[]>();
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM stock");
			while (rs.next()) {
				String[] row = new String[6];
				row[0] = rs.getString("ProductID");
				row[1] = rs.getString("Detail");
				row[2] = rs.getString("Company");
				row[3] = rs.getString("Quantity");
				row[4] = String.valueOf(rs.getDouble("Price"));
				row[5] = String.valueOf(rs.getInt("MinLimit"));
				data.add(row);
			}
			conn.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		return data;
	}

	// Feature 9: Reports & Analytics Methods

	public static ArrayList<String[]> getSalesByDateRange(String fromDate, String toDate) {
		ArrayList<String[]> data = new ArrayList<String[]>();
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			// Note: Date stored as String yyyy/MM/dd, so lexicographical comparison works
			String query = "SELECT * FROM sale WHERE Date >= '" + fromDate + "' AND Date <= '" + toDate
					+ "' ORDER BY Date";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				String[] row = new String[7];
				row[0] = rs.getString("Date");
				row[1] = rs.getString("ProductID");
				row[2] = rs.getString("Company");
				row[3] = rs.getString("Quantity");
				row[4] = String.valueOf(rs.getDouble("Payment"));
				row[5] = rs.getString("Name");
				try {
					row[6] = String.valueOf(rs.getDouble("Discount"));
				} catch (SQLException e) {
					row[6] = "0.0";
				}
				data.add(row);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static ArrayList<String[]> getTopProducts(int limit) {
		ArrayList<String[]> data = new ArrayList<String[]>();
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			String query = "SELECT ProductID, SUM(Payment) as TotalRevenue, SUM(Quantity) as TotalUnits " +
					"FROM sale GROUP BY ProductID ORDER BY TotalRevenue DESC LIMIT " + limit;
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				String[] row = new String[3];
				row[0] = rs.getString("ProductID");
				row[1] = String.valueOf(rs.getDouble("TotalRevenue"));
				row[2] = String.valueOf(rs.getInt("TotalUnits"));
				data.add(row);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static String[] getSalesSummary(String fromDate, String toDate) {
		String[] summary = { "0.0", "0", "0.0" }; // Total Revenue, Tx Count, Avg Sale
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			String query = "SELECT SUM(Payment), COUNT(*), AVG(Payment) FROM sale WHERE Date >= '" + fromDate
					+ "' AND Date <= '" + toDate + "'";
			ResultSet rs = statement.executeQuery(query);
			if (rs.next()) {
				double total = rs.getDouble(1);
				int count = rs.getInt(2);
				double avg = rs.getDouble(3);
				summary[0] = String.format("%.2f", total);
				summary[1] = String.valueOf(count);
				summary[2] = String.format("%.2f", avg);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return summary;
	}

	public static ArrayList<String[]> getDailySales(String fromDate, String toDate) {
		ArrayList<String[]> data = new ArrayList<String[]>();
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			String query = "SELECT Date, SUM(Payment) FROM sale WHERE Date >= '" + fromDate + "' AND Date <= '" + toDate
					+ "' GROUP BY Date ORDER BY Date";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				String[] row = new String[2];
				row[0] = rs.getString("Date");
				row[1] = String.valueOf(rs.getDouble(1));
				data.add(row);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static int getLowStockCount() {
		int count = 0;
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM stock WHERE Quantity < MinLimit");
			if (rs.next()) {
				count = rs.getInt(1);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	// Feature 8: Customer Management Methods

	public static void addCustomer(String name, String phone, String email) {
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			statement.executeUpdate("INSERT INTO customers (Name, Phone, Email) VALUES ('" + name + "','" + phone
					+ "','" + email + "')");
			JOptionPane.showMessageDialog(null, "Customer added successfully");
			conn.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error adding customer: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void updateCustomer(String id, String name, String phone, String email) {
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			int status = statement.executeUpdate("UPDATE customers SET Name = '" + name + "', Phone = '" + phone
					+ "', Email = '" + email + "' WHERE CustomerID = " + id);
			if (status == 1)
				JOptionPane.showMessageDialog(null, "Customer updated successfully");
			else
				JOptionPane.showMessageDialog(null, "Customer not found!");
			conn.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error updating customer: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void deleteCustomer(String id) {
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			int status = statement.executeUpdate("DELETE FROM customers WHERE CustomerID = " + id);
			if (status == 1)
				JOptionPane.showMessageDialog(null, "Customer deleted successfully");
			else
				JOptionPane.showMessageDialog(null, "Customer not found!");
			conn.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error deleting customer: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static ArrayList<String[]> getCustomers() {
		ArrayList<String[]> data = new ArrayList<String[]>();
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM customers");
			while (rs.next()) {
				String[] row = new String[5];
				row[0] = String.valueOf(rs.getInt("CustomerID"));
				row[1] = rs.getString("Name");
				row[2] = rs.getString("Phone");
				row[3] = rs.getString("Email");
				row[4] = String.valueOf(rs.getInt("LoyaltyPoints"));
				data.add(row);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static boolean checkCustomerPhone(String phone) {
		boolean exists = false;
		Connection conn = DBConnection();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM customers WHERE Phone = '" + phone + "'");
			if (rs.next()) {
				exists = true;
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exists;
	}

	public static void main(String args[]) {

	}
}
