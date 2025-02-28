import javax.swing.*;
import java.awt.*;

public class Alpha {
    //Вот это стиль
    public static Color mainColor = Color.decode("#FFFFFF");
    public static Color additionalColor = Color.decode("#F4E8D3");
    public static Color accentColor = Color.decode("#67BA80");
    public static Font Segoe = new Font("Segoe UI", Font.PLAIN, 14);
    public static Font SegoeBold = new Font("Segoe UI", Font.BOLD, 16);

    public static void applyButtonStyle(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                button.setFont(Segoe);
                button.setBackground(accentColor);
                button.setForeground(Color.WHITE);
            } else if (component instanceof Container) {
                applyButtonStyle((Container) component);
            }
        }
    }

    public static void applyLabelnStyle(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setFont(Segoe);
            } else if (component instanceof Container) {
                applyLabelnStyle((Container) component);
            }
        }
    }

    public static void applyJTextFiledStyle(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JTextField) {
                JTextField field = (JTextField) component;
                field.setFont(Segoe);
            } else if (component instanceof Container) {
                applyButtonStyle((Container) component);
            }
        }
    }


}
