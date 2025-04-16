import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CalculatorWindow extends JFrame {
    private static Connection connection;
    private static Statement statement;

    JTextField matTF, proTf, countTF, param1TF, param2TF;

    public CalculatorWindow() {
        setTitle("Расчёт кол-ва материалов");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        connection = Main.connection;
        statement = Main.statement;

        //main
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(new JLabel("Материал (ID)"));
        matTF = new JTextField();
        mainPanel.add(matTF);

        mainPanel.add(new JLabel("Продукт (ID)"));
        proTf = new JTextField();
        mainPanel.add(proTf);

        mainPanel.add(new JLabel("Количество"));
        countTF = new JTextField();
        mainPanel.add(countTF);

        mainPanel.add(new JLabel("Параметр 1"));
        param1TF = new JTextField();
        mainPanel.add(param1TF);

        mainPanel.add(new JLabel("Параметр 2"));
        param2TF = new JTextField();
        mainPanel.add(param2TF);

        add(mainPanel, BorderLayout.CENTER);

        //bottom
        JButton countBtn = new JButton("Расчитать");
        countBtn.addActionListener(e -> checkInputData());
        add(countBtn, BorderLayout.SOUTH);

        setStyle();
    }

    private void checkInputData() {
        String matId, proId;
        int count;
        double param1, param2;

        try {
            matId = matTF.getText().trim();
            proId = proTf.getText().trim();
            if (matId.isEmpty() || proId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Заполните все ID", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            count = Integer.parseInt(countTF.getText().trim());
            param1 = Double.parseDouble(param1TF.getText().trim());
            param2 = Double.parseDouble(param2TF.getText().trim());

            if (count < 1 || param1 <= 0 || param2 <= 0) {
                JOptionPane.showMessageDialog(this, "Числа должны быть больше 0", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int result = calculation(matId, proId, count, param1, param2);
            JOptionPane.showMessageDialog(this, result, "Необходимое кол-во с у чётом брака", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "-1", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int calculation(String matId, String proId, int count, double param1, double param2) {
        Double productCoefficient = getProductCoefficient(proId);
        Double materialPercent = getMaterialPercent(matId);
        if (productCoefficient == -1 || materialPercent == -1) return -1;

        double materialPerUnit = param1 * param2 * productCoefficient;
        double totalMaterial = materialPerUnit * count;
        double totalWithDefect = totalMaterial * (1 + materialPercent / 100);

        return (int) Math.ceil(totalWithDefect);
    }

    private double getProductCoefficient(String proId) {
        String query = "SELECT * FROM Product_type " +
                "WHERE Id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, proId);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return set.getDouble(2);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ощибка при получении данных", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

        return -1;
    }

    private double getMaterialPercent(String matId) {
        String query = "SELECT * FROM Material_type " +
                "WHERE Id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, matId);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return set.getDouble(2);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ощибка при получении данных", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

        return -1;
    }

    private void setStyle() {
        setIconImage(new ImageIcon("src/Resources/Мастер пол.png").getImage());
        Style.applyButtonStyle(this);
        Style.applyTextFieldStyle(this);
        Style.applyLabelStyle(this);
        getContentPane().setBackground(Style.white);
    }
}
