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

        ParkingSpot existingSpot = spotService.findSpotByPlate(vehicle.getPlate());

        if (existingSpot != null) {
            System.out.println(
                    "HATA: Araç " + vehicle.getPlate() + " zaten park halinde " + existingSpot.getSpotID());

            return false;
        }

        VehicleSize size = vehicle.getSize();
        ParkingSpot availableSpot = spotService.findAvailableSpot(size);

        if (availableSpot != null) {
            availableSpot.setAssignVehicle(vehicle);
            vehicle.assignSpot(availableSpot.getSpotID());
            vehicle.park();

            historyService.createParkingEntry(vehicle);

            System.out.println("BAŞARILI: Araç " + vehicle.getPlate() +
                    " park edilmiş " + availableSpot.getSpotID() + " (" + availableSpot.getType() + ")");
            return true;
        } else {
            System.out.println("HATA: Araç için uygun park yeri bulunamadı. " + vehicle.getPlate());
            return false;
        }
    }

    public ParkingHistory unParkVehicle(Vehicle vehicle) {

        ParkingSpot occupiedSpot = spotService.findSpotByPlate(vehicle.getPlate());

        if (occupiedSpot != null) {

            Vehicle realVehicleInSystem = occupiedSpot.getVehicle();

            realVehicleInSystem.unPark();
            occupiedSpot.setRemoveVehicle();

            ParkingHistory completedHistory = historyService.completedHistoryRecords(realVehicleInSystem);

            if (completedHistory != null) {
                paymentService.calculateFee(completedHistory);
                return completedHistory;
            }
        }
        return null;
    }
}