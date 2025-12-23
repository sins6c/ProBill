import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class FixStockTable {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost/caddey";
        String user = "root";
        String password = "212369";

        try (Connection conn = DriverManager.getConnection(url, user, password);
                Statement stmt = conn.createStatement()) {

            System.out.println("Connected to database.");

            // Add Price column if missing
            try {
                stmt.executeUpdate("ALTER TABLE stock ADD COLUMN Price DOUBLE DEFAULT 0.0 AFTER Quantity");
                System.out.println("Modified 'stock' table: Added 'Price' column.");
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate column")) {
                    System.out.println("'Price' column already exists.");
                } else {
                    System.err.println("Error adding Price: " + e.getMessage());
                }
            }

            // Add MinLimit column if missing
            try {
                stmt.executeUpdate("ALTER TABLE stock ADD COLUMN MinLimit INT DEFAULT 10");
                System.out.println("Modified 'stock' table: Added 'MinLimit' column.");
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate column")) {
                    System.out.println("'MinLimit' column already exists.");
                } else {
                    System.err.println("Error adding MinLimit: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
