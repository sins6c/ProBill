
import java.sql.*;

public class CheckSchema {
    public static void main(String[] args) {
        System.out.println("Checking Schema...");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/caddey", "root", "");
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getColumns(null, null, "sale", "Discount");
            if (rs.next()) {
                System.out.println("SUCCESS: Column 'Discount' found in table 'sale'.");
            } else {
                System.out.println("FAILURE: Column 'Discount' NOT found in table 'sale'.");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
