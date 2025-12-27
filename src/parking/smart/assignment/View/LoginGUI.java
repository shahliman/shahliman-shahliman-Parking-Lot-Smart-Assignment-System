package parking.smart.assignment.View;

import javax.swing.*;
import java.awt.*;
import parking.smart.assignment.controller.*;

public class LoginGUI extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private ParkingController parkingController;
    private AdminController adminController;

    public LoginGUI(ParkingController pCtrl, AdminController aCtrl) {
        this.parkingController = pCtrl;
        this.adminController = aCtrl;

        setTitle("Smart Parking System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Əsas Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Komponentlər
        JLabel titleLabel = new JLabel("PARKING LOGIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        userField = new JTextField();
        userField.setBorder(BorderFactory.createTitledBorder("Username"));

        passField = new JPasswordField();
        passField.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton loginBtn = new JButton("Login as Admin");
        loginBtn.setBackground(new Color(44, 62, 80));
        loginBtn.setForeground(Color.WHITE);

        JButton driverBtn = new JButton("Continue as Driver");
        driverBtn.setContentAreaFilled(false);
        driverBtn.setBorderPainted(false);
        driverBtn.setForeground(Color.GRAY);

        // Panellərə əlavə etmək
        mainPanel.add(titleLabel);
        mainPanel.add(userField);
        mainPanel.add(passField);
        mainPanel.add(loginBtn);
        mainPanel.add(driverBtn);

        add(mainPanel);

        // --- MƏNTİQ (BAĞLANTI) ---

        // Admin girişi
        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if (user.equals("admin") && pass.equals("1234")) {
                AdminGUI adminWin = new AdminGUI(adminController); // Yeni yaratdığımız class
                adminWin.setVisible(true);
                dispose();
                // Login pəncərəsini bağla
            } else {
                JOptionPane.showMessageDialog(this, "Səhv istifadəçi adı və ya şifrə!", "Xəta",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Sürücü kimi davam et
        driverBtn.addActionListener(e -> {
            DriverGUI driverWin = new DriverGUI(parkingController);
            driverWin.setVisible(true);
            dispose();
        });
    }
}
