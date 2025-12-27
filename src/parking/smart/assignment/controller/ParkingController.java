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

    public AssignmentService getAssignmentService() {
        return this.assignmentService;
    }

    public String enterVehicle(Vehicle vehicle) {
        if (!PlateValidator.isValid(vehicle.getPlate())) {

            return "Nömrə formatı səhvdir!";
        }
        boolean success = assignmentService.parkVehicle(vehicle);
        return success ? "Uğurla park edildi!" : "Boş yer yoxdur!";
    }

    public String exitVehicle(Vehicle vehicle) {
        ParkingHistory history = assignmentService.unParkVehicle(vehicle);
        if (history != null) {
            double fee = paymentService.calculateFee(history);
            return String.format(
                    "🚗 Maşın çıxış etdi: %s\n" +
                            "💰 Ödəniləcək məbləğ: %.2f AZN\n" +
                            "✅ Sağ olun, yenə gözləyirik!",
                    vehicle.getPlate(), fee);

        } else {
            return "XƏTA: Bu vasitə sistemdə tapılmadı.";
        }

    }

}
