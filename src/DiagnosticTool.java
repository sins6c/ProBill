import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DiagnosticTool {

    // Potentials to check
    private static final String[] PASSWORDS = { "", "root", "mysql", "1234", "admin", "password" };

    public static void main(String[] args) {
        String workingPass = null;
        Connection conn = null;

        System.out.println("--- Starting Database Diagnostics ---");

        // 1. Attempt to find working credential
        for (String pass : PASSWORDS) {
            try {
                // Connect without DB first to just check auth
                System.out.print("Trying user='root' pass='" + pass + "' ... ");
                conn = DriverManager.getConnection("jdbc:mysql://localhost/", "root", pass);
                System.out.println("SUCCESS!");
                workingPass = pass;
                break;
            } catch (SQLException e) {
                System.out.println("Failed (" + e.getMessage() + ")");
            }
        }

        if (conn == null) {
            System.err.println("FATAL: Could not connect to MySQL with common passwords.");
            return;
        }

        try {
            Statement stmt = conn.createStatement();

            // 2. Check if 'caddey' database exists
            System.out.println("Checking for database 'caddey'...");
            ResultSet rs = stmt.executeQuery("SHOW DATABASES LIKE 'caddey'");
            if (!rs.next()) {
                System.out.println("Database 'caddey' NOT FOUND. Creating it...");
                stmt.executeUpdate("CREATE DATABASE caddey");
                System.out.println("Database 'caddey' created.");

                // Initialize tables?
                // We'll select it and let the user run the app setup, or we can minimally
                // bootstrap the users table.
                stmt.execute("USE caddey");
                createBootstrapDetails(stmt);
            } else {
                System.out.println("Database 'caddey' found.");
                stmt.execute("USE caddey");
            }

            // 3. Reset/Create Admin
            System.out.println("Ensuring Admin user exists...");
            // Ensure users table exists
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (Email VARCHAR(255), Password VARCHAR(255))");

            rs = stmt.executeQuery("SELECT * FROM users WHERE Email='admin'");
            if (rs.next()) {
                stmt.executeUpdate("UPDATE users SET Password='admin' WHERE Email='admin'");
                System.out.println("Updated existing admin password to 'admin'.");
            } else {
                stmt.executeUpdate("INSERT INTO users (Email, Password) VALUES ('admin', 'admin')");
                System.out.println("Inserted new admin user with password 'admin'.");
            }

            System.out.println("\n--- DIAGNOSTICS COMPLETE ---");
            if (!workingPass.equals("")) {
                System.out.println("IMPORTANT: You must update DB.java with the password: '" + workingPass + "'");
            } else {
                System.out.println("DB.java configuration (empty password) appears correct.");
            }

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createBootstrapDetails(Statement stmt) throws SQLException {
        // Create basic tables just to allow login
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (Email VARCHAR(255), Password VARCHAR(255))");
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS stock (ProductID VARCHAR(255), Detail VARCHAR(255), Company VARCHAR(255), Quantity INT, Price DOUBLE, MinLimit INT)");
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS sale (ProductID VARCHAR(255), Company VARCHAR(255), Date VARCHAR(50), Quantity INT, Payment DOUBLE, Name VARCHAR(255), Discount DOUBLE)");
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS customers (CustomerID INT AUTO_INCREMENT PRIMARY KEY, Name VARCHAR(255), Phone VARCHAR(20), Email VARCHAR(255), LoyaltyPoints INT DEFAULT 0)");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS coupons (Code VARCHAR(50), Rate INT)");

        System.out.println("Bootstrapped tables: users, stock, sale, customers, coupons.");
    }
}
