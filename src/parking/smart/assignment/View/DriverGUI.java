package parking.smart.assignment.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import parking.smart.assignment.controller.ParkingController;
import parking.smart.assignment.model.Car;
import parking.smart.assignment.service.*;
import parking.smart.assignment.model.ParkingSpot;
import parking.smart.assignment.model.Vehicle;
import parking.smart.assignment.model.Zone;

public class DriverGUI extends JFrame {
    private ParkingController parkingController;
    private JTextField plateField;
    private JTable liveTable;
    private DefaultTableModel tableModel;

    public DriverGUI(ParkingController controller) {
        this.parkingController = controller;
        setupUI();
    }

    private void setupUI() {
        setTitle("Smart Parking - Driver Terminal");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(240, 242, 245));

        // --- 1. SOL PANEL: GİRİŞ/ÇIXIŞ ƏMƏLİYYATLARI ---
        JPanel actionPanel = new JPanel();
        actionPanel.setPreferredSize(new Dimension(320, 650));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 25));
        actionPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(200, 200, 200)));

        JLabel iconLabel = new JLabel("🚗", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 60));

        JLabel titleLabel = new JLabel("Quick Actions");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        plateField = new JTextField();
        plateField.setPreferredSize(new Dimension(250, 50));
        plateField.setFont(new Font("Arial", Font.BOLD, 18));
        plateField.setBorder(BorderFactory.createTitledBorder("Vehicle Plate Number"));

        JButton btnCheckIn = createStyledButton("CHECK-IN (ENTRY)", new Color(46, 204, 113));
        JButton btnCheckOut = createStyledButton("CHECK-OUT (EXIT)", new Color(231, 76, 60));

        actionPanel.add(iconLabel);
        actionPanel.add(titleLabel);
        actionPanel.add(plateField);
        actionPanel.add(btnCheckIn);
        actionPanel.add(btnCheckOut);

        // --- 2. SAĞ PANEL: CANLI MONITORİNQ ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        JLabel tableTitle = new JLabel("Live Parking Status");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 20));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        String[] cols = { "Spot ID", "Vehicle Plate", "Entry Time", "Status" };
        tableModel = new DefaultTableModel(cols, 0);
        liveTable = new JTable(tableModel);
        liveTable.setRowHeight(35);
        liveTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(liveTable);
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // --- DÜYMƏLƏRİN MƏNTİQİ ---
        btnCheckIn.addActionListener(e -> {
            String plate = plateField.getText().trim();
            if (!plate.isEmpty()) {
                String response = parkingController.enterVehicle(new Car(plate, Vehicle.VehicleSize.MEDIUM));
                JOptionPane.showMessageDialog(this, response, "Entry Result", JOptionPane.INFORMATION_MESSAGE);
                plateField.setText("");
                // updateTable(); // Bu metodu backend-ə görə dolduracağıq
                updateTable();
            }
        });

        btnCheckOut.addActionListener(e -> {
            String plate = plateField.getText().trim();
            if (!plate.isEmpty()) {
                // Əgər Car class-ında tək parametr yoxdursa, belə yaz:
                String response = parkingController.exitVehicle(new Car(plate, Vehicle.VehicleSize.MEDIUM));
                JOptionPane.showMessageDialog(this, response);
                plateField.setText("");
                updateTable();
            }
        });

        add(actionPanel, BorderLayout.WEST);
        add(tablePanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(250, 55));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void updateTable() {
        // 1. Cədvəli təmizləyirik
        tableModel.setRowCount(0);

        // 2. Məlumatları gətiririk
        // Qeyd: getAllZones() metodunun SpotService-də List<Zone> qaytardığından əmin
        // ol
        java.util.List<parking.smart.assignment.model.Zone> zones = parkingController.getAssignmentService()
                .getSpotService().getAllZones();

        if (zones == null)
            return;

        for (parking.smart.assignment.model.Zone zone : zones) {
            for (ParkingSpot spot : zone.getSpots()) {

                // Maşın nömrəsini təhlükəsiz şəkildə alırıq
                String plate = "---";
                if (spot.getIsOccupied() && spot.getVehicle() != null) {
                    plate = spot.getVehicle().getPlate();
                }

                // Status və Giriş vaxtı
                String status = spot.getIsOccupied() ? "🔴 Dolu" : "🟢 Boş";
                String entryTime = spot.getIsOccupied() ? "Daxil olub" : "---";

                // Cədvələ sətir əlavə edirik
                tableModel.addRow(new Object[] {
                        zone.getZoneID() + spot.getSpotID(), // Məsələn: A1
                        plate,
                        entryTime,
                        status
                });
            }
        }
    }
}
