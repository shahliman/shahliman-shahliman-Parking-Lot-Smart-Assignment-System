package parking.smart.assignment.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import parking.smart.assignment.controller.AdminController;
import parking.smart.assignment.model.ParkingHistory;

public class AdminGUI extends JFrame {
    private AdminController adminController;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Dinamik yenilənən komponentlər
    private JLabel lblRevenue, lblActiveCars, lblFreeSpots;
    private DefaultTableModel historyTableModel;

    public AdminGUI(AdminController controller) {
        this.adminController = controller;
        setupUI();
        startRealTimeUpdates(); // Məlumatları hər saniyə yeniləyir
    }

    private void setupUI() {
        setTitle("Parking Control Center - Admin");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. SIDEBAR ---
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(33, 37, 41));
        sidebar.setPreferredSize(new Dimension(250, 700));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));

        JLabel logoLabel = new JLabel("ADMIN PANEL");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 22));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        sidebar.add(logoLabel);

        JButton btnDashboard = createSidebarButton("📊 Dashboard");
        JButton btnHistory = createSidebarButton("📜 Full History");

        sidebar.add(btnDashboard);
        sidebar.add(btnHistory);

        // --- 2. CONTENT AREA ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createDashboardPanel(), "DASHBOARD");
        contentPanel.add(createHistoryPanel(), "HISTORY");

        // Düymə funksiyaları
        btnDashboard.addActionListener(e -> {
            updateStats();
            cardLayout.show(contentPanel, "DASHBOARD");
        });
        btnHistory.addActionListener(e -> {
            updateHistoryTable();
            cardLayout.show(contentPanel, "HISTORY");
        });

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Statistika Kartları üçün Label-ləri yaradırıq
        lblRevenue = new JLabel("0.00 AZN");
        lblActiveCars = new JLabel("0");
        lblFreeSpots = new JLabel("0");

        JPanel statsContainer = new JPanel(new GridLayout(1, 3, 20, 0));
        statsContainer.add(new StatCard("Total Revenue", lblRevenue, new Color(40, 167, 69)));
        statsContainer.add(new StatCard("Active Cars", lblActiveCars, new Color(0, 123, 255)));
        statsContainer.add(new StatCard("Free Spots", lblFreeSpots, new Color(255, 193, 7)));

        panel.add(new JLabel("Welcome back, Admin!", SwingConstants.LEFT), BorderLayout.NORTH);
        panel.add(statsContainer, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Full Parking History Report"));

        String[] cols = { "Plate", "Entry Time", "Exit Time", "Fee (AZN)", "Spot" };
        historyTableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(historyTableModel);
        table.setRowHeight(30);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // --- Backend-dən Məlumatları Çəkən Metodlar ---

    public void updateStats() {
        // İndi bu metodlar Controller-dən real datanı çəkir
        double revenue = adminController.getHistoryService().getTotalRevenue();
        int occupied = adminController.getSpotService().getOccupiedSpotsCount();
        int total = adminController.getSpotService().getTotalSpotsCount();

        lblRevenue.setText(String.format("%.2f AZN", revenue));
        lblActiveCars.setText(String.valueOf(occupied));
        lblFreeSpots.setText(String.valueOf(total - occupied));
    }

    public void updateHistoryTable() {
        historyTableModel.setRowCount(0);
        List<ParkingHistory> history = adminController.getHistoryService().getCompletedHistoryRecords();

        if (history == null)
            return; // Siyahı boşdursa çıx

        for (ParkingHistory record : history) {
            // Əgər record və ya daxilindəki vehicle null-dursa xəta verməsin deyə
            // yoxlayırıq
            if (record != null && record.getVehicle() != null) {
                historyTableModel.addRow(new Object[] {
                        record.getVehicle().getPlate(),
                        record.getEntryTime() != null ? record.getEntryTime() : "---",
                        record.getExitTime() != null ? record.getExitTime() : "Davam edir",
                        String.format("%.2f", record.getFee()),
                        record.getVehicle().getAssignedSpotID()
                });
            }
        }
    }

    private void startRealTimeUpdates() {
        Timer timer = new Timer(1000, e -> {
            if (contentPanel.getComponent(0).isVisible()) { // Dashboard açıqdırsa
                updateStats();
            }
        });
        timer.start();
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
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}

// --- Yenilənmiş StatCard Class-ı ---
class StatCard extends JPanel {
    public StatCard(String title, JLabel valueLabel, Color color) {
        setLayout(new BorderLayout());
        setBackground(color);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel t = new JLabel(title);
        t.setForeground(Color.WHITE);
        t.setFont(new Font("Arial", Font.PLAIN, 14));

        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));

        add(t, BorderLayout.NORTH);
        add(valueLabel, BorderLayout.CENTER);
    }
}