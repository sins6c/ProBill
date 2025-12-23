import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CustomerTableSetup {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/caddey", "root", "");
            Statement stmt = conn.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS customers (" +
                    "CustomerID INT NOT NULL AUTO_INCREMENT, " +
                    "Name VARCHAR(255), " +
                    "Phone VARCHAR(20) UNIQUE, " +
                    "Email VARCHAR(255), " +
                    "LoyaltyPoints INT DEFAULT 0, " +
                    "PRIMARY KEY (CustomerID))";

            stmt.executeUpdate(sql);
            System.out.println("Table 'customers' created successfully.");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
