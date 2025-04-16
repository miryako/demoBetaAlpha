import javax.swing.*;
import java.awt.*;

public class PartnerRender extends JPanel implements ListCellRenderer<Partner>{
    private JLabel titleLabel = new JLabel();
    private JLabel directorLabel = new JLabel();
    private JLabel communicationLabel = new JLabel();
    private JLabel ratingLabel = new JLabel();
    private JLabel persentLabel = new JLabel();

    public PartnerRender() {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20),
                BorderFactory.createLineBorder(Color.BLACK, 1)
        ));

        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        dataPanel.add(titleLabel);
        dataPanel.add(directorLabel);
        dataPanel.add(communicationLabel);
        dataPanel.add(ratingLabel);

        panel.add(dataPanel, BorderLayout.CENTER);
        persentLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 55, 55));
        panel.add(persentLabel, BorderLayout.EAST);

        add(panel, BorderLayout.CENTER);

        setStyle();
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Partner> jList, Partner partner, int i, boolean b, boolean b1) {
        titleLabel.setText(partner.type + " | " + partner.name);
        directorLabel.setText(partner.director);
        communicationLabel.setText(partner.phoneNumber);
        ratingLabel.setText("Рейтинг: " + partner.rating);
        persentLabel.setText(partner.percent + "%");

        setStyle();

        return this;
    }

    private void setStyle() {
        Style.applyButtonStyle(this);
        Style.applyTextFieldStyle(this);
        Style.applyLabelStyle(this);
    }
}
