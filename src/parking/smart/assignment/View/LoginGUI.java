package parking.smart.assignment.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import parking.smart.assignment.controller.*;
import parking.smart.assignment.model.Vehicle;

public class LoginGUI extends JFrame {
    private ParkingController parkingController;
    private AdminController adminController;

    public LoginGUI(ParkingController parkingController, AdminController adminController) {
        this.parkingController = parkingController;
        this.adminController = adminController;
        setupUI();
    }

    private void setupUI() {
        setTitle("Akıllı Park Etme - Giriş");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(Color.WHITE);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        JButton driverBtn = new JButton("KULLANICI GİRİŞİ");
        driverBtn.setPreferredSize(new Dimension(280, 80));
        driverBtn.setBackground(new Color(46, 204, 113));
        driverBtn.setForeground(Color.WHITE);
        driverBtn.setFont(new Font("Arial", Font.BOLD, 20));
        driverBtn.setFocusPainted(false);
        driverBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        centerPanel.add(driverBtn);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);

        JButton adminBtn = new JButton("Yönetici Paneli");
        adminBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        adminBtn.setForeground(Color.LIGHT_GRAY);
        adminBtn.setBorderPainted(false);
        adminBtn.setContentAreaFilled(false);
        adminBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottomPanel.add(adminBtn);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(bottomPanel, BorderLayout.SOUTH);

        driverBtn.addActionListener(e -> {
            String plateInput = JOptionPane.showInputDialog(this, "Araba plaka numarasın giriniz:");

            if (plateInput != null && !plateInput.trim().isEmpty()) {
                String finalPlate = plateInput.trim().toUpperCase();

                Vehicle.VehicleSize[] sizes = Vehicle.VehicleSize.values();
                Vehicle.VehicleSize selectedSize = (Vehicle.VehicleSize) JOptionPane.showInputDialog(
                        this, "Boyutu seçin:", "Seçim",
                        JOptionPane.QUESTION_MESSAGE, null, sizes, sizes[1]);

                if (selectedSize != null) {
                    DriverGUI driverWin = new DriverGUI(parkingController, finalPlate, selectedSize);
                    this.setVisible(false);
                    driverWin.setVisible(true);

                    driverWin.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            setVisible(true);
                        }
                    });
                }
            }
        });

        adminBtn.addActionListener(e -> {

            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();

            Object[] message = {
                    "Kullanıcı Adı:", usernameField,
                    "Şifre:", passwordField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Admin Girişi", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (option == JOptionPane.OK_OPTION) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.equals("admin") && password.equals("12345")) {
                    AdminGUI adminWin = new AdminGUI(adminController);
                    this.setVisible(false);
                    adminWin.setVisible(true);

                    adminWin.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            setVisible(true);
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(this, "Kullanıcı adı veya şifre yanlış.!", "Hata",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}