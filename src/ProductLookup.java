import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProductLookup {

    private static final String API_URL = "https://world.openfoodfacts.org/api/v0/product/";

    /**
     * Fetches product name from Open Food Facts API using barcode.
     * 
     * @param barcode The product barcode
     * @return Product name (and brand) if found, or null
     */
    public static String fetchProductDetails(String barcode) {
        try {
            URL url = new URL(API_URL + barcode + ".json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "BillingSystem/1.0 (Java; Education)");

            if (conn.getResponseCode() != 200) {
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            br.close();

            String json = result.toString();

            // Simple string parsing to avoid external dependencies like Gson/Jackson
            // Look for "product_name" and "brands"
            // Note: This is fragile but sufficient for this specific API structure

            String status = extractJsonValue(json, "status_verbose");
            if (status != null && status.contains("product not found")) {
                return null;
            }

            String productName = extractJsonValue(json, "product_name");
            String brands = extractJsonValue(json, "brands");

            if (productName != null && !productName.isEmpty()) {
                if (brands != null && !brands.isEmpty()) {
                    return productName + " - " + brands;
                }
                return productName;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey);
        if (start == -1)
            return null;

        start += searchKey.length();

        // Find start of value (could be quote or number/boolean)
        while (start < json.length()
                && (json.charAt(start) == ' ' || json.charAt(start) == ':' || json.charAt(start) == '"')) {
            start++;
        }

        int end = start;
        while (end < json.length() && json.charAt(end) != '"') {
            // Handle escaped quotes if necessary, but keep simple for now
            if (json.charAt(end) == '\\' && end + 1 < json.length() && json.charAt(end + 1) == '"') {
                end += 2;
                continue;
            }
            end++;
        }

        // If it was a number or boolean, it wouldn't end with quote, so we scan for
        // comma or brace
        // But for product_name/brands it's always string, so scanning for closing quote
        // is safe
        // Wait, the prior loop skips opening quote. So we are inside the string value
        // now.

        // Revised extraction for string values:
        // Key found at index `start`.
        // If value starts with quote:

        int valueStart = json.indexOf("\"", json.indexOf(searchKey) + searchKey.length());
        if (valueStart == -1)
            return null; // Should confirm it's close to key

        int valueEnd = valueStart + 1;
        while (valueEnd < json.length()) {
            if (json.charAt(valueEnd) == '"' && json.charAt(valueEnd - 1) != '\\') {
                break;
            }
            valueEnd++;
        }

        if (valueEnd >= json.length())
            return null;

        return json.substring(valueStart + 1, valueEnd);
    }
}
