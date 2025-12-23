public class TestLowStock {
    public static void main(String[] args) {
        System.out.println("Testing Feature 10: Low Stock Alerts...");

        // 1. Test Backend Method
        int count = DB.getLowStockCount();
        System.out.println("Current Low Stock Count: " + count);

        if (count >= 0) {
            System.out.println("PASS: DB.getLowStockCount() returned valid integer.");
        } else {
            System.out.println("FAIL: Invalid count returned.");
        }

        // 2. Logic Verification (manual interpretation)
        System.out.println("Note: If count > 0, Dashboard card should be RED.");
        System.out.println("Note: If count == 0, Dashboard card should be DEFAULT.");
    }
}
