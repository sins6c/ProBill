import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ResetAdmin {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/caddey", "root", "212369");
            Statement stmt = conn.createStatement();

            // Check if admin exists first
            java.sql.ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE Email='admin'");
            if (rs.next()) {
                // Update
                stmt.executeUpdate("UPDATE users SET Password='admin' WHERE Email='admin'");
                System.out.println("Admin password reset to 'admin'.");
            } else {
                // Insert
                stmt.executeUpdate("INSERT INTO users (Email, Password) VALUES ('admin', 'admin')");
                System.out.println("Admin user created with password 'admin'.");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
