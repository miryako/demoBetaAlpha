import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderMaker extends JFrame {
    JTextField countTF;
    JComboBox<Product> productBox;
    JComboBox<Material> materialBox;
    JLabel resultLabel;

    public OrderMaker() {
        setTitle("История");
        setIconImage(new ImageIcon("src/Resources/Мастер пол.png").getImage());
        setSize(800, 230);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Alpha.mainColor);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));

        countTF = new JTextField("1");
        mainPanel.add(countTF);

        productBox = new JComboBox<>();
        loadProducts();
        mainPanel.add(productBox);

        materialBox = new JComboBox<>();
        loadMaterial();
        mainPanel.add(materialBox);

        resultLabel = new JLabel();
        mainPanel.add(resultLabel);

        add(mainPanel, BorderLayout.CENTER);

        JButton resultBtn = new JButton("Расчитать");
        resultBtn.addActionListener(e -> {
            Product product = (Product) productBox.getSelectedItem();
            Material material = (Material) materialBox.getSelectedItem();
            resultLabel.setText("Необходимое количество материалов: " + amountOfMaterialRequired(product.id, material.id));
        });
        add(resultBtn, BorderLayout.SOUTH);
    }

    private void loadProducts() {
        String query = "SELECT * FROM Products";

        try {
            ResultSet set = Main.statement.executeQuery(query);
            while (set.next()) {
                Product product = new Product(set.getString(1),
                        set.getString(2),
                        set.getString(3),
                        set.getString(4),
                        set.getInt(5));
                productBox.addItem(product);
            }
        } catch (SQLException e) {
            System.out.println("OrderMaker (products): " + e.getMessage());
        }
    }

    private void loadMaterial() {
        String query = "SELECT * FROM Material_type";

        try {
            ResultSet set = Main.statement.executeQuery(query);
            while (set.next()) {
                Material material = new Material(set.getString(1),
                set.getString(2), set.getInt(3));
                materialBox.addItem(material);
            }
        } catch (SQLException e) {
            System.out.println("OrderMaker (material): " + e.getMessage());
        }
    }

    private int amountOfMaterialRequired(String productID, String materialID) {
        int count = Integer.parseInt(countTF.getText());
        if (productID.isEmpty() || materialID.isEmpty() || count <= 0) return -1;

        Product product = (Product) productBox.getSelectedItem();
        Material material = (Material) materialBox.getSelectedItem();

        int percentageOfDefects = material.percentageOfDefects;
        int productTypeCoefficient = getProductTypeCoefficient(product.type);

        int amountOfMaterial = (count * productTypeCoefficient) / (1 - percentageOfDefects / 100);

        return amountOfMaterial;
    }

    private int getProductTypeCoefficient(String type) {
        String query = "SELECT Product_Type_Coefficient FROM Product_type WHERE Product_type = ?";

        try {
            PreparedStatement preparedStatement = Main.connection.prepareStatement(query);
            preparedStatement.setString(1, type);

            try (ResultSet set = preparedStatement.executeQuery()) {
                if (set.next()) return set.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("OrderMaker (getProductTypeCoefficient): " + e.getMessage());
        }

        return 0;
    }
}
