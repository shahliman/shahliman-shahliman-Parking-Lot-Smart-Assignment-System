package parking.smart.assignment.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import parking.smart.assignment.controller.AdminController;

public class AdminGUI extends JFrame {
    private AdminController adminController;
    private JPanel contentPanel; // Sağ tərəfdəki dəyişən hissə
    private CardLayout cardLayout; // Səhifələr arası keçid üçün

    public AdminGUI(AdminController controller) {
        this.adminController = controller;
        setupUI();
    }

    private void setupUI() {
        setTitle("Parking Control Center - Admin");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. SOL SIDEBAR (MENYU) ---
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(33, 37, 41)); // Professional tünd rəng
        sidebar.setPreferredSize(new Dimension(250, 700));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));

        JLabel logoLabel = new JLabel("ADMIN PANEL");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 22));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        sidebar.add(logoLabel);

        JButton btnDashboard = createSidebarButton("📊 Dashboard");
        JButton btnHistory = createSidebarButton("📜 Full History");
        JButton btnSettings = createSidebarButton("⚙️ Settings");

        sidebar.add(btnDashboard);
        sidebar.add(btnHistory);
        sidebar.add(btnSettings);

        // --- 2. SAĞ MƏZMUN SAHƏSİ (CONTENT AREA) ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Səhifələri yaradırıq
        contentPanel.add(createDashboardPanel(), "DASHBOARD");
        contentPanel.add(createHistoryPanel(), "HISTORY");

        // --- DÜYMƏLƏRİN FUNKSİYASI ---
        btnDashboard.addActionListener(e -> cardLayout.show(contentPanel, "DASHBOARD"));
        btnHistory.addActionListener(e -> cardLayout.show(contentPanel, "HISTORY"));

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Statistika Kartları
        JPanel statsContainer = new JPanel(new GridLayout(1, 3, 20, 0));
        statsContainer.add(new StatCard("Total Revenue", "250.00 AZN", new Color(40, 167, 69)));
        statsContainer.add(new StatCard("Active Cars", "12", new Color(0, 123, 255)));
        statsContainer.add(new StatCard("Free Spots", "8", new Color(255, 193, 7)));

        panel.add(new JLabel("Welcome back, Admin!", SwingConstants.LEFT), BorderLayout.NORTH);
        panel.add(statsContainer, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Full Parking History Report"));

        String[] cols = { "Plate", "Entry", "Exit", "Fee", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setBackground(new Color(52, 58, 64));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.PLAIN, 16));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        return btn;
    }
}

// --- STATİSTİKA KARTLARI ÜÇÜN KÖMƏKÇİ CLASS ---
class StatCard extends JPanel {
    public StatCard(String title, String value, Color color) {
        setLayout(new BorderLayout());
        setBackground(color);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel t = new JLabel(title);
        t.setForeground(Color.WHITE);
        JLabel v = new JLabel(value);
        v.setForeground(Color.WHITE);
        v.setFont(new Font("Arial", Font.BOLD, 28));

        add(t, BorderLayout.NORTH);
        add(v, BorderLayout.CENTER);
    }

}