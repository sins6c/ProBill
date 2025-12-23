import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;

import javax.swing.JOptionPane;

public class EmailSender {

    public static void openDraft(String to, String subject, String body) {
        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
            JOptionPane.showMessageDialog(null, "Email is not supported on this system usage.");
            return;
        }

        try {
            // Encode subject and body parameters
            String encodedSubject = URLEncoder.encode(subject, "UTF-8").replace("+", "%20");
            String encodedBody = URLEncoder.encode(body, "UTF-8").replace("+", "%20");

            // Construct mailto URI
            // RFC 6068: mailto:user@example.com?subject=...&body=...
            String uriStr = String.format("mailto:%s?subject=%s&body=%s",
                    to, encodedSubject, encodedBody);

            Desktop.getDesktop().mail(new URI(uriStr));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error opening email client: " + e.getMessage());
        }
    }
}
