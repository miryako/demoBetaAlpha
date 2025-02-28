import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class HistoryMenu extends JFrame {
    private DefaultListModel<PartnerProduct> listModel;
    private JComboBox<String> partnerBox;

    public HistoryMenu() {
        setTitle("История");
        setIconImage(new ImageIcon("src/Resources/Мастер пол.png").getImage());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Alpha.mainColor);

        //top
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBackground(Alpha.additionalColor);

        loadPartnerBox();
        topPanel.add(partnerBox);
        add(topPanel, BorderLayout.NORTH);

        //center
        listModel = new DefaultListModel<>();
        loadInfo();
        JList<PartnerProduct> list = new JList<>(listModel);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
        add(list, BorderLayout.CENTER);

        //bottom
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setBackground(Alpha.additionalColor);

        JButton lastBtn = new JButton("Назад");
        lastBtn.addActionListener(e -> Main.openMainMenu());
        bottomPanel.add(lastBtn);

        JButton orderMakerBtn = new JButton("Расчёт");
        orderMakerBtn.addActionListener(e -> {
            OrderMaker orderMaker = new OrderMaker();
            orderMaker.setVisible(true);
        });
        bottomPanel.add(orderMakerBtn);

        add(bottomPanel, BorderLayout.SOUTH);


        Alpha.applyButtonStyle(this);
    }

    public void loadInfo() {
        String filter = partnerBox.getSelectedItem().toString();
        String query = "SELECT * FROM Partner_products";
        if (filter != "All") query += " WHERE Partner = ?";

        listModel.clear();

        try {
            PreparedStatement preparedStatement = Main.connection.prepareStatement(query);

            if (filter != "All") preparedStatement.setString(1, filter);

            try (ResultSet set = preparedStatement.executeQuery()) {
                while (set.next()) {
                    String product = set.getString(1);
                    String partner = set.getString(2);
                    String quantity = set.getString(3);
                    String date = set.getString(4);

                    PartnerProduct partnerProduct = new PartnerProduct(product, partner, quantity, date);
                    listModel.addElement(partnerProduct);
                }
            }

        } catch (SQLException e) {
            System.out.println("Загрузка истории: " + e.getMessage());
        }
    }

    private void loadPartnerBox() {
        partnerBox = new JComboBox<>();
        Set<String> partnerSet = new HashSet<>();
        partnerSet.add("All");
        String query = "SELECT * FROM Partner_products";

        try {
            ResultSet set = Main.statement.executeQuery(query);
            while (set.next()) {
                partnerSet.add(set.getString(2));
            }
        } catch (SQLException e) {
            System.out.println("PartnerMenu: " + e.getMessage());
        }

        for (String partner: partnerSet) partnerBox.addItem(partner);

        partnerBox.addActionListener(e -> loadInfo());
    }
}
