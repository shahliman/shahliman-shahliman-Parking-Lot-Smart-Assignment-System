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

        // BURANI ƏLAVƏ ET:
        loadHistoryFromDatabase();

        updateStats();
        startRealTimeUpdates();
    }

    private void setupUI() {
        setTitle("Park Kontrol Merkezi - Yönetim");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(33, 37, 41));
        sidebar.setPreferredSize(new Dimension(250, 700));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));

        JLabel logoLabel = new JLabel("Yönetici Paneli");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 22));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        sidebar.add(logoLabel);

        JButton btnDashboard = createSidebarButton("Gösterge Paneli");
        JButton btnHistory = createSidebarButton("Tüm Tarihçe");

        sidebar.add(btnDashboard);
        sidebar.add(btnHistory);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createDashboardPanel(), "Gösterge Paneli");
        contentPanel.add(createHistoryPanel(), "Tüm Tarihçe");

        btnDashboard.addActionListener(e -> {
            updateStats();
            cardLayout.show(contentPanel, "Gösterge Paneli");
        });
        btnHistory.addActionListener(e -> {
            loadHistoryFromDatabase(); // Hər basanda bazadan ən son halı çəkir
            cardLayout.show(contentPanel, "Tüm Tarihçe");
        });
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        lblRevenue = new JLabel("0.00 AZN");
        lblActiveCars = new JLabel("0");
        lblFreeSpots = new JLabel("0");

        JPanel statsContainer = new JPanel(new GridLayout(1, 3, 20, 0));
        statsContainer.add(new StatCard("Toplam Gelir", lblRevenue, new Color(40, 167, 69)));
        statsContainer.add(new StatCard("Aktif Araçlar", lblActiveCars, new Color(0, 123, 255)));
        statsContainer.add(new StatCard("Boş Yerler", lblFreeSpots, new Color(255, 193, 7)));

        panel.add(new JLabel("Tekrar hoş geldiniz, Yönetici!", SwingConstants.LEFT), BorderLayout.NORTH);
        panel.add(statsContainer, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Tüm Park Tarihçe Raporu"));

        String[] cols = { "Plaka", "Boyut", "Giriş Saati", "Çikiş Saati", "Ücret (AZN)", "Yer" };
        historyTableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(historyTableModel);
        table.setRowHeight(30);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    public void updateStats() {

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
            return;

        for (ParkingHistory record : history) {

            if (record != null && record.getVehicle() != null) {
                historyTableModel.addRow(new Object[] {
                        record.getVehicle().getPlate(),
                        record.getVehicleSize(),
                        record.getEntryTime() != null ? record.getEntryTime() : "---",
                        record.getExitTime() != null ? record.getExitTime() : "Devam edir",
                        String.format("%.2f", record.getFee()),
                        record.getVehicle().getAssignedSpotID()
                });
            }
        }
    }

    // AdminGUI.java daxilində uyğun bir yerə əlavə et:
    private void loadHistoryFromDatabase() {
        if (historyTableModel == null)
            return;

        historyTableModel.setRowCount(0); // Köhnə siyahını təmizləyirik

        // Bütün tarixi (həm içəridəkiləri, həm çıxanları) bazadan çəkirik
        String query = "SELECT * FROM parking_history ORDER BY entry_time DESC";

        try (java.sql.Connection conn = parking.smart.assignment.util.DatabaseConfig.getConnection();
                java.sql.Statement stmt = conn.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String plate = rs.getString("plate");
                String spot = rs.getString("spot_id");
                String size = rs.getString("vehicle_size");
                java.sql.Timestamp entry = rs.getTimestamp("entry_time");
                java.sql.Timestamp exit = rs.getTimestamp("exit_time");
                double fee = rs.getDouble("fee");

                String entryStr = (entry != null) ? entry.toString() : "---";
                String exitStr = (exit != null) ? exit.toString() : "Hələ də parkdadır";
                String feeStr = (exit != null) ? String.format("%.2f AZN", fee) : "---";

                // Sənin cədvəl sütunlarının sırasına uyğun əlavə et:
                historyTableModel.addRow(new Object[] { plate, size, entryStr, exitStr, feeStr, spot });
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    private void startRealTimeUpdates() {
        Timer timer = new Timer(1000, e -> {
            if (contentPanel.getComponent(0).isVisible()) {
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