package parking.smart.assignment;

import parking.smart.assignment.controller.*;
import parking.smart.assignment.model.Zone;
import parking.smart.assignment.service.*;
import parking.smart.assignment.View.LoginGUI;

import java.util.Arrays;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        HistoryService historyService = new HistoryService();
        PaymentService paymentService = new PaymentService();

        SpotService spotService = new SpotService(Arrays.asList(new Zone("A", 20)));

        AssignmentService assignmentService = new AssignmentService(spotService, historyService, paymentService);

        ParkingController parkingController = new ParkingController(assignmentService);
        AdminController adminController = new AdminController(historyService, spotService);

        SwingUtilities.invokeLater(() -> {
            LoginGUI login = new LoginGUI(parkingController, adminController);
            login.setVisible(true);
        });
    }
}