import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginMenu extends JFrame {
    public LoginMenu() {
        setTitle("Вход в систему");
        setIconImage(new ImageIcon("src/Resources/Мастер пол.png").getImage());
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Alpha.mainColor);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60, 10, 60, 10));
        mainPanel.setBackground(Alpha.mainColor);

        mainPanel.add(new JLabel("Почта"));
        JTextField mailTF = new JTextField();
        mainPanel.add(mailTF);

        mainPanel.add(new JLabel("Пароль"));
        JTextField passwordTF = new JTextField();
        mainPanel.add(passwordTF);

        JButton checkBtn = new JButton("Вход");
        checkBtn.addActionListener(e -> checkInfo(mailTF.getText(), passwordTF.getText()));
        mainPanel.add(checkBtn);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void checkInfo(String mail, String password) {
        if (mail.isEmpty() || password.isEmpty()) {
            System.out.println("error");
            return;
        }

        String query = "SELECT * FROM STAFF WHERE Mail = ? AND Password = ?";

        try (PreparedStatement statement = Main.connection.prepareStatement(query)) {
            statement.setString(1, mail);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) Main.openMainMenu();
            else System.out.println("Неправильные данные");

        } catch (SQLException e) {
            System.out.println("Login: " + e.getMessage());
        }
    }
}
