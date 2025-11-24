import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ConfirmationUI extends JFrame {

    public ConfirmationUI() {

        setTitle("Let's G");
        setSize(1200, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        JLabel header = new JLabel("Let’s G", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 36));
        header.setBorder(new EmptyBorder(30, 0, 30, 0));
        root.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(20, 40, 20, 40));

        content.add(makeSection("DESTINATION", "Mount Kalugong"));
        content.add(Box.createVerticalStrut(30));

        content.add(makeSection("PARTICIPANTS", "Tim"));
        content.add(Box.createVerticalStrut(35));

        // Header row
        JPanel headerRow = new JPanel(new GridLayout(1, 3));
        headerRow.setBackground(Color.WHITE);
        headerRow.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel placeH = new JLabel("PLACE");
        JLabel descH = new JLabel("DESCRIPTION");
        JLabel priceH = new JLabel("PRICE");

        placeH.setFont(new Font("Arial", Font.BOLD, 18));
        descH.setFont(new Font("Arial", Font.BOLD, 18));
        priceH.setFont(new Font("Arial", Font.BOLD, 18));

        placeH.setForeground(new Color(216, 177, 163));
        descH.setForeground(new Color(216, 177, 163));
        priceH.setForeground(new Color(216, 177, 163));

        headerRow.add(placeH);
        headerRow.add(descH);
        headerRow.add(priceH);

        content.add(headerRow);

        // Checkpoint rows (same image twice)
        content.add(makeCheckpointRow("/mnt/data/Screen Shot 2025-11-22 at 7.52.40 PM.png"));
        content.add(makeCheckpointRow("/mnt/data/Screen Shot 2025-11-22 at 7.52.40 PM.png"));

        content.add(Box.createVerticalStrut(30));

        content.add(makeSummaryRow("Subtotal (2)", "php 200.00"));
        content.add(makeSummaryRow("Taxes", "php 20.00"));
        content.add(makeSummaryRowBold("Total", "php 220.00"));

        root.add(content, BorderLayout.CENTER);

        JButton confirmBtn = new RoundedButton("CONFIRM");
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setBackground(Color.BLACK);
        confirmBtn.setPreferredSize(new Dimension(0, 80));
        confirmBtn.setFont(new Font("Arial", Font.BOLD, 28));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(20, 40, 40, 40));
        bottom.add(confirmBtn, BorderLayout.CENTER);

        root.add(bottom, BorderLayout.SOUTH);

        add(root);
        setVisible(true);
    }

    private JPanel makeSection(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel(label);
        lbl.setForeground(new Color(216, 177, 163));
        lbl.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Arial", Font.BOLD, 28));
        val.setBorder(new EmptyBorder(10, 0, 0, 0));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(val, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel makeCheckpointRow(String imgPath) {
        JPanel row = new JPanel(new BorderLayout(15, 15));
        row.setBackground(Color.WHITE);

        ImageIcon icon = new ImageIcon(imgPath);
        Image scaled = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel img = new JLabel(new ImageIcon(scaled));
        img.setPreferredSize(new Dimension(150, 150));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(Color.WHITE);

        JLabel desc = new JLabel("1000 km of 4,954");
        desc.setFont(new Font("Arial", Font.PLAIN, 26));

        JLabel type = new JLabel("Cafe");
        type.setForeground(new Color(216, 177, 163));
        type.setFont(new Font("Arial", Font.PLAIN, 22));

        info.add(desc);
        info.add(type);

        JLabel price = new JLabel("100–200php");
        price.setFont(new Font("Arial", Font.PLAIN, 26));
        price.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(img, BorderLayout.WEST);
        row.add(info, BorderLayout.CENTER);
        row.add(price, BorderLayout.EAST);

        row.setBorder(new EmptyBorder(20, 0, 20, 0));

        return row;
    }

    private JPanel makeSummaryRow(String label, String amount) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel l = new JLabel(label);
        l.setFont(new Font("Arial", Font.PLAIN, 22));

        JLabel a = new JLabel(amount);
        a.setFont(new Font("Arial", Font.PLAIN, 22));

        row.add(l, BorderLayout.WEST);
        row.add(a, BorderLayout.EAST);

        return row;
    }

    private JPanel makeSummaryRowBold(String label, String amount) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel l = new JLabel(label);
        l.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel a = new JLabel(amount);
        a.setFont(new Font("Arial", Font.BOLD, 24));

        row.add(l, BorderLayout.WEST);
        row.add(a, BorderLayout.EAST);

        return row;
    }

    // Copy your RoundedButton class here
    static class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
        }
        @Override
        public void setBorder(Border border) {}
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
