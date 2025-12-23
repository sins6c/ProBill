import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class SchemaMigration {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/caddey", "root", "");
            if (conn != null) {
                System.out.println("Connected to database.");
                Statement stmt = conn.createStatement();
                String sql = "ALTER TABLE sale ADD COLUMN Discount DOUBLE DEFAULT 0.0";
                try {
                    stmt.executeUpdate(sql);
                    System.out.println("Schema updated successfully: Added Discount column to sale table.");
                } catch (SQLException e) {
                    if (e.getMessage().contains("Duplicate column")) {
                        System.out.println("Column 'Discount' already exists. Skipping.");
                    } else {
                        throw e;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error updating schema: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
