package parking.smart.assignment.controller;

import parking.smart.assignment.model.Vehicle;
import parking.smart.assignment.model.ParkingHistory;
import parking.smart.assignment.service.AssignmentService;
import parking.smart.assignment.util.PlateValidator;

public class ParkingController {
    private final AssignmentService assignmentService;

    public ParkingController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;

    }

    public AssignmentService getAssignmentService() {
        return this.assignmentService;
    }

    public String enterVehicle(Vehicle vehicle) {
        if (!PlateValidator.isValid(vehicle.getPlate())) {

            return "Plaka Biçimi Yalnış";
        }
        boolean success = assignmentService.parkVehicle(vehicle);
        return success ? "Park işlemi başarıyla tamamlandı." : "Boş yer yok.!";
    }

    public String exitVehicle(Vehicle vehicle) {
        ParkingHistory history = assignmentService.unParkVehicle(vehicle);
        if (history != null) {
            double fee = history.getFee();
            return String.format(
                    "Araba çıktı. %s\n" +
                            "Ödenecek tutar: %.2f AZN\n" +
                            "Teşekkür ederiz, tekrar bekliyoruz.!",
                    vehicle.getPlate(), fee);

        } else {
            return "HATA: Bu araç sistemde bulunamadı.";
        }

    }

}
