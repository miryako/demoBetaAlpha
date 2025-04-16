import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class PartnersListWindow extends JFrame {
    private static DefaultListModel<Partner> listModel;

    private static Connection connection;
    private static Statement statement;

    public PartnersListWindow() {
        setTitle("Учёт партнёров");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        connection = Main.connection;
        statement = Main.statement;

        //top
        Image icon = new ImageIcon("src/Resources/Мастер пол.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel iconLab = new JLabel(new ImageIcon(icon));
        iconLab.setBorder(BorderFactory.createEmptyBorder(10, 0,10, 0));
        add(iconLab, BorderLayout.NORTH);

        //main
        listModel = new DefaultListModel<>();
        loadData();
        JList<Partner> list = new JList<>(listModel);
        list.setCellRenderer(new PartnerRender());
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Partner partner = list.getSelectedValue();
                openEditor(partner.name);
            }
        });
        JScrollPane pane = new JScrollPane(list);
        pane.setBackground(Style.gray);
        add(pane, BorderLayout.CENTER);

        //bottom
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        bottomPanel.setBackground(Style.white);

        JButton addPartnerBtn = new JButton("Добавить партнёра");
        addPartnerBtn.addActionListener(e -> openEditor(null));
        bottomPanel.add(addPartnerBtn);

        JButton storyButton = new JButton("История реализации");
        storyButton.addActionListener(e -> Main.changeWindow(true));
        bottomPanel.add(storyButton);

        add(bottomPanel, BorderLayout.SOUTH);

        setStyle();
    }

    public static void loadData() {
        listModel.clear();
        String query = "SELECT * FROM Partners";
        try {
            ResultSet set = statement.executeQuery(query);
            while (set.next()) {
                String type, name, director, mail, phoneNumber, address, inn;
                int rating, percent;
                type = set.getString(1);
                name = set.getString(2);
                director = set.getString(3);
                mail = set.getString(4);
                phoneNumber = set.getString(5);
                address = set.getString(6);
                inn = set.getString(7);
                rating = set.getInt(8);
                percent = percentCounting(name);

                Partner partner = new Partner(type, name, director, mail, phoneNumber, address, inn, rating, percent);
                listModel.addElement(partner);
            }
        } catch (SQLException e) {
        }
    }

    private static int percentCounting(String name) {
        int summa = 0;

        String query = "SELECT * FROM Partner_products WHERE [Наименование партнера] = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    summa += resultSet.getInt(3);
                }
            }

        } catch (SQLException e) {
        }

        //до 10000 – 0%, от 10000 –
        //до 50000 – 5%, от 50000 – до 300000 – 10%, более 300000 – 15%.
        if (summa >= 10000 && summa < 50000) return 5;
        else if (summa >= 50000 && summa < 300000) return 10;
        else if (summa >= 300000) return 15;
        else return 0;
    }

    private void openEditor(String name) {
        PartnerEditorWindow editorWindow = new PartnerEditorWindow();
        editorWindow.setVisible(true);
        editorWindow.loadPartnerData(name);
    }

    private void setStyle() {
        setIconImage(new ImageIcon("src/Resources/Мастер пол.png").getImage());
        Style.applyButtonStyle(this);
        Style.applyTextFieldStyle(this);
        Style.applyLabelStyle(this);
        getContentPane().setBackground(Style.white);
    }
}
