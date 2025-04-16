import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PartnerStoryWindow extends JFrame {
    private static Connection connection;
    private static Statement statement;

    private DefaultListModel<Product> listModel;

    public PartnerStoryWindow() {
        setTitle("Учёт партнёров");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        connection = Main.connection;
        statement = Main.statement;

        //main
        listModel = new DefaultListModel<>();
        JList<Product> list = new JList<>(listModel);
        JScrollPane pane = new JScrollPane(list);
        pane.setBackground(Style.gray);
        add(pane, BorderLayout.CENTER);

        //top
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JTextField nameTF = new JTextField();
        topPanel.add(nameTF);

        JButton searchBtn = new JButton("Искать");
        searchBtn.addActionListener(e -> loadProductsData(nameTF.getText()));
        topPanel.add(searchBtn);

        add(topPanel, BorderLayout.NORTH);

        //bottom
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        bottomPanel.setBackground(Style.white);

        JButton storyButton = new JButton("Главное окно");
        storyButton.addActionListener(e -> Main.changeWindow(false));
        bottomPanel.add(storyButton);

        JButton calculatorBtn = new JButton("Расчитать материалы");
        calculatorBtn.addActionListener(e -> openCalculator());
        bottomPanel.add(calculatorBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        setStyle();
    }

    private void loadProductsData(String partner) {
        listModel.clear();
        String query = "SELECT * FROM Partner_products " +
                "WHERE [Наименование партнера] = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, partner);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                String name, date;
                int count;

                name = set.getString(1);
                date = set.getString(4);
                count = set.getInt(3);
                Product product = new Product(name, date, count);

                listModel.addElement(product);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ощибка при получении данных", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void openCalculator() {
        CalculatorWindow calculatorWindow = new CalculatorWindow();
        calculatorWindow.setVisible(true);
    }

    private void setStyle() {
        setIconImage(new ImageIcon("src/Resources/Мастер пол.png").getImage());
        Style.applyButtonStyle(this);
        Style.applyTextFieldStyle(this);
        Style.applyLabelStyle(this);
        getContentPane().setBackground(Style.white);
    }
}
