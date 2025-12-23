
import java.util.ArrayList;

public class TestPdfGeneration {
    public static void main(String[] args) {
        System.out.println("Starting PDF Generation Test...");
        try {
            // Mock data: ProductID, Detail, UnitPrice, Quantity, TotalPrice
            Object[] data = {
                    "P001", "Test Product 1", "100.0", "2", "200.0",
                    "P002", "Test Product 2", "50.0", "1", "50.0"
            };
            double total = 250.0;
            int invoiceId = 999;
            double discountRate = 10.0; // 10% discount

            System.out.println("Generating PDF with Total: " + total + ", Discount: " + discountRate + "%");

            // Call the modified pdfGenerator
            pdfGenerator.makePdf(data, total, invoiceId, discountRate);

            System.out.println("PDF generation completed. Check _invoice_.pdf");

            // Basic file check (since we can't easily parse PDF in this env without more
            // libs)
            java.io.File f = new java.io.File("_invoice_.pdf");
            if (f.exists() && f.length() > 0) {
                System.out.println("SUCCESS: _invoice_.pdf created. Size: " + f.length() + " bytes.");
            } else {
                System.out.println("FAILURE: _invoice_.pdf not found or empty.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FAILURE: Exception occurred.");
        }
    }
}
