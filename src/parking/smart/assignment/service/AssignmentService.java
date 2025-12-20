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

    public boolean parkVehicle(Vehicle vehicle) {
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

        ParkingSpot occupiedSpot = spotService.findSpotByVehicle(vehicle);

        if (occupiedSpot != null) {

            vehicle.unPark();
            occupiedSpot.setRemoveVehicle();

            ParkingHistory completedHistory = historyService.completedHistoryRecords(vehicle);

            if (completedHistory != null) {

                paymentService.calculateFee(completedHistory);

                System.out.println("SUCCESS: Vehicle " + vehicle.getPlate() +
                        " unparked from " + occupiedSpot.getSpotID() +
                        ". Fee: " + String.format("%.2f", completedHistory.getFee()) + " AZN");
                return completedHistory;
            }
        }
        System.out.println("FAILURE: Vehicle " + vehicle.getPlate() + " not found in any spot.");
        return null;
    }
}