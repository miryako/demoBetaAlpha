import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PartnerEditorWindow extends JFrame {
    JTextField nameTf, ratingTF, addressTF, directorTF, phoneNumberTF, mailTF, innTF;
    JComboBox<String> typeBox;

    private boolean isNewElement = false;

    private Connection connection;
    private Statement statement;

    public PartnerEditorWindow() {
        setTitle("Редактор партнёров");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        connection = Main.connection;
        statement = Main.statement;

        //main
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Style.white);

        mainPanel.add(new JLabel("Тип партнера"));
        typeBox = new JComboBox<>(new String[]{"ЗАО", "ООО", "ПАО", "ОАО"});
        mainPanel.add(typeBox);

        mainPanel.add(new JLabel("Наименование партнера"));
        nameTf = new JTextField();
        mainPanel.add(nameTf);

        mainPanel.add(new JLabel("Рейтинг (от 1 до 10)"));
        ratingTF = new JTextField();
        mainPanel.add(ratingTF);

        mainPanel.add(new JLabel("Юридический адрес партнера"));
        addressTF = new JTextField();
        mainPanel.add(addressTF);

        mainPanel.add(new JLabel("Директор"));
        directorTF = new JTextField();
        mainPanel.add(directorTF);

        mainPanel.add(new JLabel("Телефон партнера"));
        phoneNumberTF = new JTextField();
        mainPanel.add(phoneNumberTF);

        mainPanel.add(new JLabel("Электронная почта партнера"));
        mailTF = new JTextField();
        mainPanel.add(mailTF);

        mainPanel.add(new JLabel("ИНН"));
        innTF = new JTextField();
        mainPanel.add(innTF);

        add(mainPanel, BorderLayout.CENTER);

        //bottom
        JButton saveBtn = new JButton("Сохранить");
        saveBtn.addActionListener(e -> fieldsCheck());
        add(saveBtn, BorderLayout.SOUTH);

        setStyle();
    }

    public void loadPartnerData(String name) {
        String query = "SELECT * FROM Partners " +
                "WHERE [Наименование партнера] = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                nameTf.setText(set.getString(2));
                ratingTF.setText(String.valueOf(set.getInt(8)));
                addressTF.setText(set.getString(6));
                directorTF.setText(set.getString(3));
                phoneNumberTF.setText(set.getString(5));
                mailTF.setText(set.getString(4));
                typeBox.setSelectedItem(set.getString(1));
                innTF.setText(set.getString(7));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка при получении данных", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fieldsCheck() {
        String[] fields = new String[]{
                typeBox.getSelectedItem().toString(),
                nameTf.getText(),
                directorTF.getText(),
                mailTF.getText(),
                phoneNumberTF.getText(),
                addressTF.getText(),
                innTF.getText(),
                ratingTF.getText()
        };

        for (String text: fields) {
            if (text == null) {
                System.out.println("Заполните все поля");
                return;
            }
        }

        try {
            int rating = Integer.parseInt(fields[7]);
            if (rating < 0 || rating > 10) {
                JOptionPane.showMessageDialog(this, "Неправильный рейтинг. Введите значение от 0 до 10", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ошибка: Рейтинг должен быть целым числом", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (isNewElement) createPartner(fields);
        else updatePartner(fields);
    }

    private void updatePartner(String[] fields) {
        String query = "UPDATE Partners " +
                "SET [Тип партнера] = ?, " +
                "    [Наименование партнера] = ?, " +
                "    [Директор] = ?, " +
                "    [Электронная почта партнера] = ?, " +
                "    [Телефон партнера] = ?, " +
                "    [Юридический адрес партнера] = ?, " +
                "    [ИНН] = ?, " +
                "    [Рейтинг] = ? " +
                "WHERE [Наименование партнера] = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < fields.length; i++) {
                if (i == 7) statement.setInt(i + 1, Integer.parseInt(fields[i]));
                statement.setString(i + 1, fields[i]);
            }
            statement.setString(9, fields[1]);
            statement.executeUpdate();
            closeWindow();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void createPartner(String[] fields) {
        String query = "INSERT INTO Partners " +
                "(" +
                "[Тип партнера], " +
                "[Наименование партнера], " +
                "[Директор], " +
                "[Электронная почта партнера], " +
                "[Телефон партнера], " +
                "[Юридический адрес партнера], " +
                "[ИНН], " +
                "[Рейтинг]) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < fields.length; i++) {
                if (i == 7) statement.setInt(i + 1, Integer.parseInt(fields[i]));
                statement.setString(i + 1, fields[i]);
            }
            statement.executeUpdate();
            closeWindow();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void closeWindow() {
        System.out.println("Успешно");
        PartnersListWindow.loadData();
        dispose();
    }

    private void setStyle() {
        setIconImage(new ImageIcon("src/Resources/Мастер пол.png").getImage());
        Style.applyButtonStyle(this);
        Style.applyTextFieldStyle(this);
        Style.applyLabelStyle(this);
        getContentPane().setBackground(Style.white);
    }
}
