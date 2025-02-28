import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PartnerMenu extends JFrame {
    private static DefaultListModel<Partner> listModel;

    public PartnerMenu() {
        setTitle("Партнёры");
        setIconImage(new ImageIcon("src/Resources/Мастер пол.png").getImage());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Alpha.mainColor);

        //top
        Image icon = new ImageIcon("src/Resources/Мастер пол.png").
                getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        JLabel iconlLab = new JLabel(new ImageIcon(icon));
        iconlLab.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(iconlLab, BorderLayout.NORTH);

        //center
        listModel = new DefaultListModel<>();
        loadData();
        JList<Partner> partnerJList = new JList<>(listModel);
        PartnerRender render = new PartnerRender();
        partnerJList.setCellRenderer(render);
        partnerJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openEditor(partnerJList.getSelectedValue(), partnerJList.getSelectedIndex());
            }
        });
        partnerJList.setBackground(Alpha.mainColor);
        add(new JScrollPane(partnerJList), BorderLayout.CENTER);

        //bottom
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setBackground(Alpha.additionalColor);

        JButton addBtn = new JButton("Добавить");
        addBtn.addActionListener(e -> openEditor(null, listModel.size()));
        bottomPanel.add(addBtn);

        JButton nextButton = new JButton("Дальше");
        nextButton.addActionListener(e -> Main.openHistory());
        bottomPanel.add(nextButton);

        add(bottomPanel, BorderLayout.SOUTH);

        //style
        Alpha.applyButtonStyle(this);
    }

    public static void loadData() {
        listModel.clear();
        String query = "SELECT * FROM Partners";
        try {
            ResultSet set = Main.statement.executeQuery(query);
            while (set.next()) {
                String id, name, type, director, mail, phone, rating, percent;
                id = set.getString("ID");
                type = set.getString("Partner_type");
                name = set.getString("Name_of_the_partner");
                director = set.getString("Director");
                mail = set.getString("Partners_email");
                phone = set.getString("Partners_phone_number");
                rating = set.getString("Rating");
                percent = String.valueOf(percentCounting(name));

                Partner partner = new Partner(id, name, type, director, mail, phone, rating, percent);
                listModel.addElement(partner);
            }
        } catch (SQLException e) {
            System.out.println("PartnerMenu: " + e.getMessage());
        }
    }

    private static int percentCounting(String name) {
        int summa = 0;

        String query = "SELECT * FROM Partner_products WHERE Partner = ?";

        try (PreparedStatement statement = Main.connection.prepareStatement(query)) {
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    summa += resultSet.getInt("Quantity");
                }
            }

        } catch (SQLException e) {
            System.out.println("percentCounting: " + e.getMessage());
        }

        //до 10000 – 0%, от 10000 –
        //до 50000 – 5%, от 50000 – до 300000 – 10%, более 300000 – 15%.
        if (summa >= 10000 && summa < 50000) return 5;
        else if (summa >= 50000 && summa < 300000) return 10;
        else if (summa >= 300000) return 15;
        else return 0;
    }

    private void openEditor(Partner partner, int index) {
        PartnerEditor editor = new PartnerEditor();
        editor.setVisible(true);
        editor.loadDate(partner);
    }
}
