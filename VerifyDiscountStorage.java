
import java.sql.*;
import java.util.ArrayList;

public class VerifyDiscountStorage {
    public static void main(String[] args) {
        System.out.println("Starting Database Storage Verification...");

        // Mock data
        Object[] data = {
                "TEST_P001", "Test Product 1", "100.0", "2", "200.0", // 5 elements per row
        };
        ArrayList<String> companies = new ArrayList<>();
        companies.add("Test Company");
        String customerName = "Test Customer";
        double discountRate = 15.5;

        try {
            // 1. Insert record
            System.out.println("Inserting test sale record...");
            // We need to make sure we are calling the NEW method signature.
            // If compilation used old cached class files, it might fail.
            DB.addSaleToDB(data, companies, customerName, discountRate);

            // 2. Verify
            Connection conn = DB.DBConnection();
            Statement stmt = conn.createStatement();

            String query = "SELECT * FROM sale WHERE ProductID = 'TEST_P001' AND Name = 'Test Customer' ORDER BY Date DESC";
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                // Check if column exists
                try {
                    double storedDiscount = rs.getDouble("Discount");
                    System.out.println("Retrieved Discount from DB: " + storedDiscount);

                    if (Math.abs(storedDiscount - discountRate) < 0.001) {
                        System.out.println("SUCCESS: Discount stored correctly.");
                    } else {
                        System.out.println(
                                "FAILURE: Discount mismatch. Expected " + discountRate + ", got " + storedDiscount);
                    }
                } catch (SQLException e) {
                    System.out.println("FAILURE: accessing Discount column: " + e.getMessage());
                }

                // Cleanup
                stmt.executeUpdate("DELETE FROM sale WHERE ProductID = 'TEST_P001' AND Name = 'Test Customer'");
                System.out.println("Test record cleaned up.");
            } else {
                System.out.println("FAILURE: Could not find the inserted record.");
            }
            conn.close();

        } catch (Exception e) {
            e.printStackTrace(); // Print full stack trace
            System.out.println("FAILURE: Exception occurred: " + e);
        }
    }
}
