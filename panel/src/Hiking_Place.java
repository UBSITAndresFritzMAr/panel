import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Hiking_Place extends JFrame {

    public Hiking_Place() {

        setTitle("Hiking Places");
        setSize(1200, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(30, 50, 30, 50));

        // ---------- HEADER ----------
        JPanel headerRow = new JPanel(new GridLayout(1, 2));
        headerRow.setBackground(Color.WHITE);
        headerRow.setBorder(new EmptyBorder(5, 0, 20, 0));

        JLabel placeH = new JLabel("PLACE");
        JLabel descH = new JLabel("DESCRIPTION");

        placeH.setFont(new Font("Arial", Font.BOLD, 20));
        descH.setFont(new Font("Arial", Font.BOLD, 20));

        placeH.setForeground(new Color(216, 177, 163));
        descH.setForeground(new Color(216, 177, 163));

        headerRow.add(placeH);
        headerRow.add(descH);

        content.add(headerRow);

        // ---------- CHECKPOINT ROWS ----------
        content.add(makeCheckpointRow("picture/ka", "Mount Kagalugong", "10km", "VIEW 1"));
        content.add(makeCheckpointRow("/mnt/data/Screen Shot 2025-11-22 at 7.52.40 PM.png", "ValleyWood", "12km", "VIEW 2"));
        content.add(makeCheckpointRow("/mnt/data/Screen Shot 2025-11-22 at 7.52.40 PM.png", "Yang Bew", "7km", "VIEW 3"));

        content.add(Box.createVerticalStrut(40));

        root.add(content, BorderLayout.CENTER);

        // ---------- CONFIRM BUTTON ----------




        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(20, 40, 40, 40));


        root.add(bottom, BorderLayout.SOUTH);

        add(root);
        setVisible(true);
    }

    // ------------------ CHECKPOINT ROW ------------------
    private JPanel makeCheckpointRow(String imgPath, String placeName, String distance, String buttonLabel) {

        JPanel row = new JPanel(new BorderLayout(20, 0));
        row.setBackground(Color.WHITE);
        row.setBorder(new EmptyBorder(25, 0, 25, 0));

        // IMAGE
        ImageIcon icon = new ImageIcon(imgPath);
        Image scaled = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel img = new JLabel(new ImageIcon(scaled));
        img.setPreferredSize(new Dimension(150, 150));

        // LEFT PANEL (IMAGE + TEXT)
        JPanel leftPanel = new JPanel(new BorderLayout(15, 0));
        leftPanel.setBackground(Color.WHITE);

        // TEXT PANEL
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel placeLabel = new JLabel(placeName);
        placeLabel.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel distanceLabel = new JLabel(distance);
        distanceLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        distanceLabel.setForeground(new Color(216, 177, 163));

        textPanel.add(placeLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(distanceLabel);

        leftPanel.add(img, BorderLayout.WEST);
        leftPanel.add(textPanel, BorderLayout.CENTER);

        // RIGHT PANEL (BUTTON)
        RectButton viewButton = new RectButton(buttonLabel, 20, 60);
        viewButton.setForeground(Color.WHITE);
        viewButton.setBackground(Color.BLACK);
        viewButton.setFont(new Font("Arial", Font.BOLD, 18));
        viewButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        //action
        viewButton.addActionListener(e -> {
            new CheckpointsUI();
            dispose();


        });

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(viewButton, BorderLayout.CENTER);

        // ADD TO ROW
        row.add(leftPanel, BorderLayout.CENTER);
        row.add(rightPanel, BorderLayout.EAST);

        return row;
    }

    // ------------------ RECTANGLE BUTTON ------------------
    static class RectButton extends JButton {

        private final int width;
        private final int height;

        public RectButton(String text, int width, int height) {
            super(text);
            this.width = width;
            this.height = height;
            setContentAreaFilled(false);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(160, height);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight()); // rectangle

            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Hiking_Place::new);
    }
}
