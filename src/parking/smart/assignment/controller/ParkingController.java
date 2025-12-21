package parking.smart.assignment.controller;

import parking.smart.assignment.model.Vehicle;
import parking.smart.assignment.model.ParkingHistory;
import parking.smart.assignment.service.AssignmentService;
import parking.smart.assignment.service.PaymentService;
import parking.smart.assignment.util.PlateValidator;

public class ParkingController {
    private final AssignmentService assignmentService;
    private final PaymentService paymentService;

    public ParkingController(AssignmentService assignmentService, PaymentService paymentService) {
        this.assignmentService = assignmentService;
        this.paymentService = paymentService;
    }

    public void enterVehicle(Vehicle vehicle) {
        if (!PlateValidator.isValid(vehicle.getPlate())) {
            System.out.println("⚠️ GİRİŞ RƏDD EDİLDİ: Nömrə formatı səhvdir (" + vehicle.getPlate() + ")");
            return;
        }
        boolean success = assignmentService.parkVehicle(vehicle);
        if (success) {
            System.out.println("✅ " + vehicle.getPlate() + " nömrəli vasitə uğurla içəri alındı.");
        } else {
            System.out.println("❌ Təəssüf ki, boş yer yoxdur.");
        }
    }

    public void exitVehicle(Vehicle vehicle) {
        ParkingHistory history = assignmentService.unParkVehicle(vehicle);
        if (history != null) {
            double fee = paymentService.calculateFee(history);
            System.out.println("💰 Çıxış tamamlandı. Ödəniş: " + fee + " AZN");

        } else {
            System.out.println("⚠️ XƏTA: Bu vasitə sistemdə tapılmadı.");
        }

    }

}
