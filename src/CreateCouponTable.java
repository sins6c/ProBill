import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.DriverManager;

public class CreateCouponTable {
    public static void main(String[] args) {
        Connection conn = DB.DBConnection();
        try {
            Statement stmt = conn.createStatement();

            // Create coupons table
            String createTable = "CREATE TABLE IF NOT EXISTS coupons " +
                    "(Code VARCHAR(50) PRIMARY KEY, " +
                    " Rate INT NOT NULL)";
            stmt.executeUpdate(createTable);
            System.out.println("Table 'coupons' created or already exists.");

            // Insert default coupons (ignore if exists)
            try {
                stmt.executeUpdate("INSERT INTO coupons VALUES ('WELCOME10', 10)");
            } catch (Exception e) {
                /* Expected if exists */ }
            try {
                stmt.executeUpdate("INSERT INTO coupons VALUES ('SALE20', 20)");
            } catch (Exception e) {
                /* Expected if exists */ }

            System.out.println("Default coupons inserted (WELCOME10, SALE20).");

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
