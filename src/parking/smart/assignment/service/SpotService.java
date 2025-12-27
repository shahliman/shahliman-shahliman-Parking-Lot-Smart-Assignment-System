package parking.smart.assignment.service;

import parking.smart.assignment.model.*;
import parking.smart.assignment.model.Vehicle.VehicleSize;

// import java.util.ArrayList;
import java.util.List;

public class SpotService {
    private final List<Zone> zones;

    public SpotService(List<Zone> initialZones) {
        this.zones = initialZones;
    }

    // SpotService daxilində zonaların siyahısını qaytaran metod
    public List<Zone> getAllZones() {
        return this.zones; // Zonaların saxlandığı List-i qaytarır
    }

    public ParkingSpot findAvailableSpot(VehicleSize requestedSize) {
        for (Zone zone : zones) {
            ParkingSpot availableSpot = zone.getSpots().stream()
                    .filter(spot -> !spot.getIsOccupied())
                    // .filter(spot-> spot.getType().equals(requestedSize.toString()))
                    .findFirst()
                    .orElse(null);

            if (availableSpot != null) {
                return availableSpot;
            }

        }
        return null;
    }

    public ParkingSpot findSpotByVehicle(Vehicle vehicle) {
        for (Zone zone : zones) {
            ParkingSpot occupiedSpot = zone.getSpots().stream()
                    .filter(spot -> spot.getAssignedVehicle() != null && spot.getAssignedVehicle().equals(vehicle))
                    .findFirst()
                    .orElse(null);

            if (occupiedSpot != null) {
                return occupiedSpot;
            }

        }
        return null;
    }

    public ParkingSpot findSpotByPlate(String plate) {
        for (Zone zone : zones) {
            for (ParkingSpot spot : zone.getSpots()) {
                if (spot.getIsOccupied() && spot.getVehicle() != null) {
                    if (spot.getVehicle().getPlate().equalsIgnoreCase(plate)) {
                        return spot;
                    }
                }
            }
        }
        return null;
    }

    public void printAllSpotStatuses() {
        System.out.println("\n*** BÜTÜN ZONALAR ÜZRƏ SPOT VƏZİYYƏTİ ***");
        for (Zone zone : zones) {
            System.out.println("--- Zone " + zone.getZoneID() + " ---");
            zone.getSpots().forEach(spot -> {
                String status = spot.getIsOccupied()
                        ? "DOLU (" + spot.getAssignedVehicle().getPlate() + ")"
                        : "BOŞ";
                System.out.println("Spot " + spot.getSpotID() + " (" + spot.getType() + "): " + status);

            });

        }
    }

}
