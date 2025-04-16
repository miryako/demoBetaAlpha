import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    private String path = "jdbc:sqlite:src/Resources/demo25.db";
    public static Statement statement;
    public static Connection connection;

    private static PartnersListWindow partnersListWindow;
    private static PartnerStoryWindow partnerStoryWindow;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    public Main() {
        connectToDB();

        partnersListWindow = new PartnersListWindow();
        partnersListWindow.setVisible(true);

        partnerStoryWindow = new PartnerStoryWindow();
    }

    private void connectToDB() {
        try {
            connection = DriverManager.getConnection(path);
            statement = connection.createStatement();
            System.out.println("Yra");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void changeWindow(boolean state) {
        partnerStoryWindow.setVisible(state);
        partnersListWindow.setVisible(!state);
    }
}