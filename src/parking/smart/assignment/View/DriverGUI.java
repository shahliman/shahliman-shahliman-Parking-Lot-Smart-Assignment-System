package parking.smart.assignment.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import parking.smart.assignment.controller.ParkingController;
import parking.smart.assignment.model.Car;
import parking.smart.assignment.model.ParkingSpot;
import parking.smart.assignment.model.Vehicle;
import parking.smart.assignment.model.Zone;

public class DriverGUI extends JFrame {
    private ParkingController parkingController;
    private String currentPlate;
    private JTable liveTable;
    private DefaultTableModel tableModel;
    private Vehicle.VehicleSize currentSize;

    public DriverGUI(ParkingController controller, String plate, Vehicle.VehicleSize size) {
        this.parkingController = controller;
        this.currentPlate = plate.toUpperCase();
        this.currentSize = size;
        setupUI();
        updateTable();

        Timer timer = new Timer(1000, e -> updateTable());
        timer.start();
    }

    private String calculateTimeElapsed(java.time.LocalDateTime entry) {
        if (entry == null)
            return "---";

        java.time.Duration duration = java.time.Duration.between(entry, java.time.LocalDateTime.now());
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        if (hours > 0) {
            return String.format("%d saat, %d dak", hours, minutes);
        } else {
            return String.format("%d daq, %d san", minutes, seconds);
        }
    }

    private void setupUI() {
        setTitle("Müşteri Terminali - " + currentPlate);
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(240, 242, 245));

        JPanel actionPanel = new JPanel();
        actionPanel.setPreferredSize(new Dimension(320, 600));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 30));
        actionPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(200, 200, 200)));

        JLabel iconLabel = new JLabel(" ", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 80));

        JLabel welcomeLabel = new JLabel("Hoş geldiniz!  ");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel plateLabel = new JLabel(currentPlate);
        plateLabel.setFont(new Font("Arial", Font.ITALIC, 24));
        plateLabel.setForeground(new Color(52, 152, 219));

        JButton btnCheckIn = createStyledButton("PARK ET", new Color(46, 204, 113));
        JButton btnCheckOut = createStyledButton("ÇIKIŞ ET", new Color(231, 76, 60));

        actionPanel.add(iconLabel);
        actionPanel.add(welcomeLabel);
        actionPanel.add(plateLabel);
        actionPanel.add(new JLabel(""));
        actionPanel.add(btnCheckIn);
        actionPanel.add(btnCheckOut);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        JLabel tableTitle = new JLabel("Park Bilgilerim");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 18));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        String[] cols = { "Konum", "Plaka", "Durum", "Zaman" };
        tableModel = new DefaultTableModel(cols, 0);
        liveTable = new JTable(tableModel);
        liveTable.setRowHeight(40);

        JScrollPane scrollPane = new JScrollPane(liveTable);
        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        btnCheckIn.addActionListener(e -> {

            String response = parkingController.enterVehicle(new Car(currentPlate, currentSize));
            JOptionPane.showMessageDialog(this, response, "Otopark", JOptionPane.INFORMATION_MESSAGE);
            updateTable();
        });

        btnCheckOut.addActionListener(e -> {

            String response = parkingController.exitVehicle(new Car(currentPlate, currentSize));
            JOptionPane.showMessageDialog(this, response, "Çıkış", JOptionPane.INFORMATION_MESSAGE);
            updateTable();
        });

        add(actionPanel, BorderLayout.WEST);
        add(tablePanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(260, 60));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void updateTable() {
        tableModel.setRowCount(0);
        java.util.List<parking.smart.assignment.model.Zone> zones = parkingController.getAssignmentService()
                .getSpotService().getAllZones();

        if (zones == null)
            return;

        for (Zone zone : zones) {
            for (ParkingSpot spot : zone.getSpots()) {
                if (spot.getIsOccupied() && spot.getVehicle() != null &&
                        spot.getVehicle().getPlate().equalsIgnoreCase(currentPlate)) {

                    String timeElapsed = calculateTimeElapsed(spot.getVehicle().getEntryTime());

                    tableModel.addRow(new Object[] {
                            zone.getZoneID() + spot.getSpotID(),
                            currentPlate,
                            "Park halinde",
                            timeElapsed
                    });
                    return;
                }
            }
        }

        tableModel.addRow(new Object[] { "---", currentPlate, "---", "Park edilmemiş" });
    }
}