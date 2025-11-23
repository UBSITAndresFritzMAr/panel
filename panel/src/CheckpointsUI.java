import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CheckpointsUI extends JFrame {

    public CheckpointsUI() {

        setTitle("Checkpoints");
        setSize(1200, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        // ---------- HEADER ----------
        JLabel header = new JLabel("Checkpoints", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 36));
        header.setBorder(new EmptyBorder(30, 0, 30, 0));
        root.add(header, BorderLayout.NORTH);

        // ---------- MAIN CONTENT ----------
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Destination
        content.add(makeSection("DESTINATION", "Mount Kalugong"));
        content.add(Box.createVerticalStrut(30));

        // Participants
        content.add(makeSection("PARTICIPANTS", "Tim"));
        content.add(Box.createVerticalStrut(35));

        // Checkpoint row
        content.add(makeCheckpointRow());

        root.add(content, BorderLayout.CENTER);

        // ---------- CONTINUE BUTTON ----------
        JButton continueBtn = new RoundedButton("CONTINUE");
        continueBtn.setForeground(Color.WHITE);
        continueBtn.setBackground(Color.BLACK);
        continueBtn.setPreferredSize(new Dimension(0, 80));
        continueBtn.setFont(new Font("Arial", Font.BOLD, 28));

        // ⭐ Add functionality → Go to ConfirmationUI
        continueBtn.addActionListener(e -> {
            new ConfirmationUI();   // open next screen
            dispose();              // close this screen
        });

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(20, 40, 40, 40));
        bottom.add(continueBtn, BorderLayout.CENTER);

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
        val.setFont(new Font("Arial", Font.PLAIN, 28));
        val.setBorder(new EmptyBorder(10, 0, 0, 0));

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(val, BorderLayout.SOUTH);

        return panel;
    }


    private JPanel makeCheckpointRow() {
        JPanel row = new JPanel(new BorderLayout(15, 15));
        row.setBackground(Color.WHITE);

        // ---------- IMAGE ----------
        ImageIcon icon = new ImageIcon("/mnt/data/Unknown.jpg");
        Image scaled = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel img = new JLabel(new ImageIcon(scaled));
        img.setPreferredSize(new Dimension(150, 150));

        // ---------- TEXT ----------
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

        // ---------- PRICE ----------
        JLabel price = new JLabel("100–200 php");
        price.setFont(new Font("Arial", Font.PLAIN, 26));
        price.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(img, BorderLayout.WEST);
        row.add(info, BorderLayout.CENTER);
        row.add(price, BorderLayout.EAST);

        row.setBorder(new EmptyBorder(20, 0, 20, 0));

        return row;
    }


    // ---------- CUSTOM ROUNDED BUTTON ----------
    static class RoundedButton extends JButton {

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
        }

        @Override
        public void setBorder(Border border) { }

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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(CheckpointsUI::new);
    }
}
