import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TestReports {
    public static void main(String[] args) {
        System.out.println("Testing Reports & Analytics Backend...");

        // Use current date as pivot
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String startOfMonth = LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        System.out.println("Date Range: " + startOfMonth + " to " + today);

        // 1. Test Sales Summary
        System.out.println("\n[Test 1] Sales Summary");
        String[] summary = DB.getSalesSummary(startOfMonth, today);
        System.out.println("Total Revenue: " + summary[0]);
        System.out.println("Tx Count: " + summary[1]);
        System.out.println("Avg Sale: " + summary[2]);
        if (summary != null && summary.length == 3) {
            System.out.println("PASS: Summary returned valid structure.");
        } else {
            System.out.println("FAIL: Summary is null or invalid.");
        }

        // 2. Test Sales List
        System.out.println("\n[Test 2] Sales List");
        ArrayList<String[]> sales = DB.getSalesByDateRange(startOfMonth, today);
        System.out.println("Sales found: " + sales.size());
        if (sales.size() > 0) {
            System.out.println("First sale date: " + sales.get(0)[0]);
            System.out.println("PASS: Sales list retrieval successful.");
        } else {
            System.out.println("WARN: No sales found (expected if DB is empty).");
        }

        // 3. Test Top Products
        System.out.println("\n[Test 3] Top Products");
        ArrayList<String[]> top = DB.getTopProducts(5);
        System.out.println("Top products found: " + top.size());
        for (String[] p : top) {
            System.out.println("Product: " + p[0] + ", Rev: " + p[1]);
        }
        if (top != null) {
            System.out.println("PASS: Top products query executed.");
        } else {
            System.out.println("FAIL: Top products list is null.");
        }

        System.out.println("\nBackend Verification Completed.");
    }
}
