import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    static PartnerMenu partnerMenu;
    static HistoryMenu historyMenu;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    public Main() {
        connectingToDB();

        LoginMenu loginMenu = new LoginMenu();
        partnerMenu = new PartnerMenu();
        historyMenu = new HistoryMenu();

        //loginMenu.setVisible(true);
        openMainMenu();
    }


    //подключение к SQL
    private String url = "jdbc:sqlite:src/Resources/Demo.db";
    public static Connection connection;
    public static Statement statement;

    private void connectingToDB() {
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            System.out.println("Успешно!");
        } catch (SQLException e) {
            System.out.println("Main: " + e.getMessage());
        }
    }

    public static void openMainMenu() {
        historyMenu.setVisible(false);
        partnerMenu.setVisible(true);
    }

    public static  void openHistory() {
        historyMenu.setVisible(true);
        partnerMenu.setVisible(false);
    }
}