package parking.smart.assignment.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Zone {
    private String zoneID;
    private List<ParkingSpot> spots; // Artıq Vehicle siyahısı deyil, Spot siyahısıdır

    public Zone(String zoneID, int capacity) {
        this.zoneID = zoneID;
        this.spots = new ArrayList<>();
        // Zone yaradılanda bütün spotları da yarat
        for (int i = 1; i <= capacity; i++) {
            // Hələlik bütün spotları "GENERAL" tipi ilə yaradırıq.
            // Gələcəkdə hər zone üçün spot tiplərini fərqli təyin etmək olar.
            this.spots.add(new ParkingSpot(zoneID + i, "GENERAL"));
        }
    }

    public String getZoneID() {
        return zoneID;
    }

    public int getCapacity() {
        return spots.size();
    }

    public List<ParkingSpot> getSpots() {
        return spots;
    }

    public List<Vehicle> getParkedVehicle() {
        // Hazırda park edilmiş avtomobillərin siyahısını qaytarır
        return spots.stream()
                .filter(ParkingSpot::getIsOccupied)
                .map(ParkingSpot::getAssignedVehicle)
                .collect(Collectors.toList());
    }

    public boolean parkVehicle(Vehicle vehicle) {
        // Boş bir spot tap
        ParkingSpot freeSpot = spots.stream()
                .filter(spot -> !spot.getIsOccupied())
                .findFirst() // İlk boş yeri götürürük (LIFO, FIFO məntiqi sizə qalıb)
                .orElse(null);

        if (freeSpot != null) {
            // 1. Spota avtomobili təyin et
            freeSpot.setAssignVehicle(vehicle);

            // 2. Avtomobilin statusunu yenilə
            vehicle.assignSpot(freeSpot.getSpotID());
            vehicle.park();

            System.out.println("Vehicle " + vehicle.getPlate() + " parked at " + freeSpot.getSpotID());
            return true;
        } else {
            System.out.println("Zone " + zoneID + " is full!");
            return false;
        }
    }

    public void unParkVehicle(Vehicle vehicle) {
        // Avtomobilin hansı spotda olduğunu tap
        ParkingSpot occupiedSpot = spots.stream()
                .filter(spot -> spot.getAssignedVehicle() == vehicle)
                .findFirst()
                .orElse(null);

        if (occupiedSpot != null) {
            // 1. Avtomobilin statusunu yenilə
            vehicle.unPark();

            // 2. Spotu boşalt
            occupiedSpot.setRemoveVehicle();

            System.out.println(vehicle.getPlate() + " successfully unparked from " + occupiedSpot.getSpotID() + "!");
        } else {
            System.out.println("Vehicle not found in this zone.");
        }
    }

    public int getAvailableSpotCount() {
        int empty = (int) spots.stream().filter(spot -> !spot.getIsOccupied()).count();
        System.out.println("Empty spots: " + empty);
        return empty;
    }

    public String getAvailableNextSpotID() {
        ParkingSpot nextSpot = spots.stream()
                .filter(spot -> !spot.getIsOccupied())
                .findFirst()
                .orElse(null);

        return (nextSpot != null) ? nextSpot.getSpotID() : "None";
    }

    public void printParkedVehicles() {
        System.out.println("\n--- Parked Vehicles in Zone " + zoneID + " ---");
        List<Vehicle> parked = getParkedVehicle();
        if (parked.isEmpty()) {
            System.out.println("No vehicles currently parked.");
        } else {
            for (Vehicle v : parked) {
                System.out.println(v);
            }
        }
        System.out.println("----------------------------------------\n");
    }
}