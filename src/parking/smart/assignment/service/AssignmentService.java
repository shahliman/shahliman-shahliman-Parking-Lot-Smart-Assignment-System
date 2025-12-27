package parking.smart.assignment.service;

import parking.smart.assignment.model.ParkingSpot;
import parking.smart.assignment.model.Vehicle;
import parking.smart.assignment.model.Vehicle.VehicleSize;
import parking.smart.assignment.model.ParkingHistory;

public class AssignmentService {

    private final SpotService spotService;
    private final HistoryService historyService;
    private final PaymentService paymentService;

    public AssignmentService(SpotService spotService, HistoryService historyService, PaymentService paymentService) {
        this.spotService = spotService;
        this.historyService = historyService;
        this.paymentService = paymentService;
    }

    public SpotService getSpotService() {
        return this.spotService;
    }

    public boolean parkVehicle(Vehicle vehicle) {
        // 1. TƏKRAR GİRİŞ YOXALMASI: Maşın artıq sistemdə varmı?
        // SpotService daxilində findSpotByPlate metodunu çağırmalıyıq
        ParkingSpot existingSpot = spotService.findSpotByPlate(vehicle.getPlate());

        if (existingSpot != null) {
            System.out.println(
                    "FAILURE: Vehicle " + vehicle.getPlate() + " is already parked at " + existingSpot.getSpotID());
            // Controller-ə mesaj göndərmək üçün false qaytarırıq
            return false;
        }

        // 2. BOŞ YER AXTARIŞI: Əgər maşın yoxdursa, boş yer axtarırıq
        VehicleSize size = vehicle.getSize();
        ParkingSpot availableSpot = spotService.findAvailableSpot(size);

        if (availableSpot != null) {
            availableSpot.setAssignVehicle(vehicle);
            vehicle.assignSpot(availableSpot.getSpotID());
            vehicle.park();

            historyService.createParkingEntry(vehicle);

            System.out.println("SUCCESS: Vehicle " + vehicle.getPlate() +
                    " parked at " + availableSpot.getSpotID() + " (" + availableSpot.getType() + ")");
            return true;
        } else {
            System.out.println("FAILURE: No available spot found for vehicle " + vehicle.getPlate());
            return false;
        }
    }

    public ParkingHistory unParkVehicle(Vehicle vehicle) {
        // findSpotByVehicle(vehicle) əvəzinə findSpotByPlate(vehicle.getPlate())
        // istifadə edirik
        ParkingSpot occupiedSpot = spotService.findSpotByPlate(vehicle.getPlate());

        if (occupiedSpot != null) {
            // Tapılan real maşın obyektini götürürük
            Vehicle realVehicleInSystem = occupiedSpot.getVehicle();

            realVehicleInSystem.unPark();
            occupiedSpot.setRemoveVehicle();

            // Tarixçəni sistemdəki real obyektlə tamamlayırıq
            ParkingHistory completedHistory = historyService.completedHistoryRecords(realVehicleInSystem);

            if (completedHistory != null) {
                paymentService.calculateFee(completedHistory);
                return completedHistory;
            }
        }
        return null; // Maşın tapılmadıqda
    }
}