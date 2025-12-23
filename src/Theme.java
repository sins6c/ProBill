import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.text.JTextComponent;

public class Theme {
    public static boolean isDarkMode = false;

    // Light Theme Colors
    public static final Color BG_LIGHT = new Color(240, 240, 240);
    public static final Color FG_LIGHT = Color.BLACK;
    public static final Color INPUT_BG_LIGHT = Color.WHITE;
    public static final Color INPUT_FG_LIGHT = Color.BLACK;

    // Dark Theme Colors
    public static final Color BG_DARK = new Color(30, 30, 30);
    public static final Color FG_DARK = new Color(220, 220, 220);
    public static final Color INPUT_BG_DARK = new Color(50, 50, 50);
    public static final Color INPUT_FG_DARK = Color.WHITE;

    public static void toggle() {
        isDarkMode = !isDarkMode;
    }

    public static void applyTheme(Container container) {
        Color bg = isDarkMode ? BG_DARK : BG_LIGHT;
        Color fg = isDarkMode ? FG_DARK : FG_LIGHT;
        Color inputBg = isDarkMode ? INPUT_BG_DARK : INPUT_BG_LIGHT;
        Color inputFg = isDarkMode ? INPUT_FG_DARK : INPUT_FG_LIGHT;

        if (container instanceof JPanel) {
            container.setBackground(bg);
        }

        for (Component c : container.getComponents()) {
            if (c instanceof JLabel) {
                c.setForeground(fg);
            } else if (c instanceof JTextComponent) { // JTextField, JTextArea
                c.setBackground(inputBg);
                c.setForeground(inputFg);
                ((JTextComponent) c).setCaretColor(fg);
            } else if (c instanceof JButton) {
                // Keep default button look or customize slightly
                // c.setBackground(isDarkMode ? Color.DARK_GRAY : new
                // JButton().getBackground());
                c.setForeground(isDarkMode ? Color.WHITE : Color.BLACK);
                if (isDarkMode)
                    c.setBackground(Color.DARK_GRAY);
                else
                    c.setBackground(new JButton().getBackground());
            } else if (c instanceof JComboBox) {
                c.setBackground(inputBg);
                c.setForeground(inputFg);
            } else if (c instanceof JScrollPane) {
                c.setBackground(bg);
                // Apply to viewport view
                JViewport viewport = ((JScrollPane) c).getViewport();
                viewport.setBackground(bg);
                if (viewport.getView() instanceof Component) {
                    applyTheme((Container) viewport.getView()); // Recursive for view
                }
            } else if (c instanceof JTable) {
                c.setBackground(inputBg);
                c.setForeground(inputFg);
                ((JTable) c).getTableHeader().setBackground(isDarkMode ? Color.DARK_GRAY : Color.LIGHT_GRAY);
                ((JTable) c).getTableHeader().setForeground(isDarkMode ? Color.WHITE : Color.BLACK);
            } else if (c instanceof Container) {
                applyTheme((Container) c);
            }
        }
    }
}
