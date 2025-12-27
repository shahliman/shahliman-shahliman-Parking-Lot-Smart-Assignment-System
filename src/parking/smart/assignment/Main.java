package parking.smart.assignment;

import parking.smart.assignment.controller.*;
import parking.smart.assignment.model.Zone;
import parking.smart.assignment.service.*;
import parking.smart.assignment.View.LoginGUI; // Paket adının kiçik hərflə (view) olduğundan əmin ol

import java.util.Arrays;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        // 1. Təməl servislərin yaradılması
        HistoryService historyService = new HistoryService();
        PaymentService paymentService = new PaymentService();

        // 2. SpotService-in yaradılması (Zonalarla)
        SpotService spotService = new SpotService(Arrays.asList(new Zone("A", 10)));

        // 3. AssignmentService-in yaradılması (Digər servislərdən asılıdır)
        AssignmentService assignmentService = new AssignmentService(spotService, historyService, paymentService);

        // 4. Kontrollerlərin yaradılması (Servisləri bura ötürürük)
        ParkingController parkingController = new ParkingController(assignmentService, paymentService);
        AdminController adminController = new AdminController(historyService, spotService);

        // 5. GUI (İnterfeys) Başlatılması
        SwingUtilities.invokeLater(() -> {
            LoginGUI login = new LoginGUI(parkingController, adminController);
            login.setVisible(true);
        });
    }
}