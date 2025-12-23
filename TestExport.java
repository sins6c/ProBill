import java.io.File;

public class TestExport {
    public static void main(String[] args) {
        System.out.println("Testing Export Functionality...");

        // Test Sales Export
        System.out.println("\\n1. Testing Sales Export...");
        boolean salesSuccess = ExportToCSV.exportSales("test_sales.csv");
        File salesFile = new File("test_sales.csv");
        if (salesFile.exists() && salesFile.length() > 0) {
            System.out.println("SUCCESS: Sales CSV created. Size: " + salesFile.length() + " bytes");
        } else {
            System.out.println("FAILURE: Sales CSV not created or empty");
        }

        // Test Stock Export
        System.out.println("\\n2. Testing Stock Export...");
        boolean stockSuccess = ExportToCSV.exportStock("test_stock.csv");
        File stockFile = new File("test_stock.csv");
        if (stockFile.exists() && stockFile.length() > 0) {
            System.out.println("SUCCESS: Stock CSV created. Size: " + stockFile.length() + " bytes");
        } else {
            System.out.println("FAILURE: Stock CSV not created or empty");
        }

        System.out.println("\\nTest completed!");
    }
}
