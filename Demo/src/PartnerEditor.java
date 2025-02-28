import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PartnerEditor extends JFrame {

    JTextField idLab, nameLab, directorLab, mailLab, phoneLab, ratingLab;
    JComboBox<String> typeBox;
    JLabel percentLab;
    boolean newElement = false;

    public PartnerEditor() {
        setTitle("Партнёры");
        setIconImage(new ImageIcon("src/Resources/Мастер пол.png").getImage());
        setSize(600, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Alpha.mainColor);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(new JLabel("Идентификатор"));
        idLab = new JTextField();
        mainPanel.add(idLab);

        mainPanel.add(new JLabel("Название"));
        nameLab = new JTextField();
        mainPanel.add(nameLab);

        mainPanel.add(new JLabel("Тип"));
        typeBox = new JComboBox<>(new String[]{"ЗАО", "ООО", "ПАО", "ОАО"});
        mainPanel.add(typeBox);

        mainPanel.add(new JLabel("Директор"));
        directorLab = new JTextField();
        mainPanel.add(directorLab);

        mainPanel.add(new JLabel("Номер телефона"));
        phoneLab = new JTextField();
        mainPanel.add(phoneLab);

        mainPanel.add(new JLabel("Почта"));
        mailLab = new JTextField();
        mainPanel.add(mailLab);

        mainPanel.add(new JLabel("Рейтинг"));
        ratingLab = new JTextField();
        mainPanel.add(ratingLab);

        percentLab = new JLabel();
        mainPanel.add(percentLab);

        add(mainPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setBackground(Alpha.additionalColor);

        JButton saveBtn = new JButton("Сохранить");
        saveBtn.addActionListener(e -> action());
        bottomPanel.add(saveBtn);

        JButton deleteBtn = new JButton("Удалить");
        deleteBtn.addActionListener(e -> deleteDate());
        bottomPanel.add(deleteBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        //style
        Alpha.applyButtonStyle(this);
        Alpha.applyLabelnStyle(this);
        Alpha.applyJTextFiledStyle(this);
    }

    public void loadDate(Partner partner) {
        if (partner == null) {
            newElement = true;
            return;
        }

        idLab.setText(partner.id);
        nameLab.setText(partner.name);
        typeBox.setSelectedItem(partner.type);
        directorLab.setText(partner.director);
        phoneLab.setText(partner.phone);
        mailLab.setText(partner.mail);
        ratingLab.setText(partner.rating);
        percentLab.setText("Скидка: " + partner.percent + "%");

    }

    private void action() {
        if (newElement) addDate();
        else saveDate();
    }

    private void addDate() {
        String query = "INSERT INTO Partners (Partner_type, Name_of_the_partner, Director, Partners_phone_number, Rating)" +
                "VALUES (?, ?, ?, ?, ?)";

        String Partner_type = typeBox.getSelectedItem().toString(), Name_of_the_partner = nameLab.getText(), Director = directorLab.getText(),
                Partners_phone_number = phoneLab.getText(), Partners_email = mailLab.getText(), Rating = ratingLab.getText();

        if (Partner_type.isEmpty() || Name_of_the_partner.isEmpty() || Director.isEmpty() || Partners_phone_number.isEmpty() ||
                Partners_email.isEmpty() || Rating.isEmpty()) {
            System.out.println("Заполните все поля");
            return;
        }

        if (Integer.valueOf(ratingLab.getText()) < 0) return;

        try (PreparedStatement statement = Main.connection.prepareStatement(query)) {
            statement.setString(1, Partner_type);
            statement.setString(2, Name_of_the_partner);
            statement.setString(3, Director);
            statement.setString(4, Partners_phone_number);
            statement.setString(5, Rating);

            statement.executeUpdate();
            System.out.println("Успешно");
            PartnerMenu.loadData();
            setVisible(false);

        } catch (SQLException e) {
            System.out.println("Editor (add): " + e.getMessage());
        }
    }

    private void saveDate() {
        String query = "UPDATE Partners " +
                "SET Partner_type = ?, Name_of_the_partner = ?, Director = ?, Partners_phone_number = ?, Partners_email =?, Rating = ? " +
                "WHERE id = " + idLab.getText();

        String Partner_type = typeBox.getSelectedItem().toString(), Name_of_the_partner = nameLab.getText(), Director = directorLab.getText(),
                Partners_phone_number = phoneLab.getText(), Partners_email = mailLab.getText(), Rating = ratingLab.getText();

        if (Partner_type.isEmpty() || Name_of_the_partner.isEmpty() || Director.isEmpty() || Partners_phone_number.isEmpty() ||
                Partners_email.isEmpty() || Rating.isEmpty()) {
            System.out.println("Заполните все поля");
            return;
        }

        if (Integer.valueOf(ratingLab.getText()) < 0) return;

        try (PreparedStatement statement = Main.connection.prepareStatement(query)) {
            statement.setString(1, Partner_type);
            statement.setString(2, Name_of_the_partner);
            statement.setString(3, Director);
            statement.setString(4, Partners_phone_number);
            statement.setString(4, Partners_email);
            statement.setString(5, Rating);

            statement.executeUpdate();
            System.out.println("Успешно");
            PartnerMenu.loadData();
            setVisible(false);

        } catch (SQLException e) {
            System.out.println("Editor (update): " + e.getMessage());
        }
    }

    private void deleteDate() {
        String id = idLab.getText();
        if (id.isEmpty()) return;

        String query = "DELETE FROM Partners WHERE id = " + id;

        try {
            Main.statement.executeUpdate(query);
            System.out.println("Успешно");
            PartnerMenu.loadData();
            setVisible(false);
        }
        catch (SQLException e) {System.out.println("delete: " + e.getMessage());}
    }
}
