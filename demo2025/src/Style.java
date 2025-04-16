import javax.swing.*;
import java.awt.*;

public class Style {
    public static Color white = Color.decode("#FFFFFF");
    public static Color gray = Color.decode("#F4E8D3");
    public static Color green = Color.decode("#67BA80");

    public static Font segoe = new Font("Segoe UI", Font.PLAIN, 18);

    public static void applyButtonStyle(Container container) {
        for (Component component: container.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                button.setBackground(green);
                button.setForeground(white);
                button.setFont(segoe);
            }
            else if (component instanceof Container) {
                applyButtonStyle((Container) component);
            }
        }
    }

    public static void applyLabelStyle(Container container) {
        for (Component component: container.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setFont(segoe);
            }
            else if (component instanceof Container) {
                applyLabelStyle((Container) component);
            }
        }
    }

    public static void applyTextFieldStyle(Container container) {
        for (Component component: container.getComponents()) {
            if (component instanceof JTextField) {
                JTextField field = (JTextField) component;
                field.setFont(segoe);
            }
            else if (component instanceof Container) {
                applyTextFieldStyle((Container) component);
            }
        }
    }
}
