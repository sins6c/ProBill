import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ExportToCSV {

    /**
     * Generic method to write data to CSV file
     * 
     * @param filename Path to the CSV file
     * @param headers  Column headers
     * @param data     Rows of data (each String[] is a row)
     * @return true if successful, false otherwise
     */
    public static boolean writeCSV(String filename, String[] headers, ArrayList<String[]> data) {
        try (FileWriter writer = new FileWriter(filename)) {
            // Write headers
            writer.append(String.join(",", headers));
            writer.append("\n");

            // Write data rows
            for (String[] row : data) {
                // Escape commas and quotes in fields
                String[] escapedRow = new String[row.length];
                for (int i = 0; i < row.length; i++) {
                    String field = row[i];
                    if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
                        field = "\"" + field.replace("\"", "\"\"") + "\"";
                    }
                    escapedRow[i] = field;
                }
                writer.append(String.join(",", escapedRow));
                writer.append("\n");
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error writing CSV file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Export all sales data to CSV
     */
    public static boolean exportSales(String filename) {
        String[] headers = { "ProductID", "Company", "Date", "Quantity", "Payment", "Customer", "Discount" };
        ArrayList<String[]> data = DB.getSalesDataForExport();

        if (data == null || data.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sales data to export.");
            return false;
        }

        boolean success = writeCSV(filename, headers, data);
        if (success) {
            JOptionPane.showMessageDialog(null, "Sales data exported successfully to:\n" + filename);
        }
        return success;
    }

    /**
     * Export all stock data to CSV
     */
    public static boolean exportStock(String filename) {
        String[] headers = { "ProductID", "Detail", "Company", "Quantity", "Price", "MinLimit" };
        ArrayList<String[]> data = DB.getStockDataForExport();

        if (data == null || data.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No stock data to export.");
            return false;
        }

        boolean success = writeCSV(filename, headers, data);
        if (success) {
            JOptionPane.showMessageDialog(null, "Stock data exported successfully to:\n" + filename);
        }
        return success;
    }
}
