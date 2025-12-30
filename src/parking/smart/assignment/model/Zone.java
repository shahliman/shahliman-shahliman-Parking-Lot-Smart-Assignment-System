package parking.smart.assignment.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Zone {
    private String zoneID;
    private List<ParkingSpot> spots;

    public Zone(String zoneID, int capacity) {
        this.zoneID = zoneID;
        this.spots = new ArrayList<>();

        for (int i = 1; i <= capacity; i++) {
            this.spots.add(new ParkingSpot(zoneID + i, "Genel"));
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

        return spots.stream()
                .filter(ParkingSpot::getIsOccupied)
                .map(ParkingSpot::getAssignedVehicle)
                .collect(Collectors.toList());
    }

    public boolean parkVehicle(Vehicle vehicle) {

        ParkingSpot freeSpot = spots.stream()
                .filter(spot -> !spot.getIsOccupied())
                .findFirst()
                .orElse(null);

        if (freeSpot != null) {

            freeSpot.setAssignVehicle(vehicle);

            vehicle.assignSpot(freeSpot.getSpotID());
            vehicle.park();

            System.out.println("Araç " + vehicle.getPlate() + " park edildi " + freeSpot.getSpotID());
            return true;
        } else {
            System.out.println("Bölge " + zoneID + " dolu!");
            return false;
        }
    }

    public void unParkVehicle(Vehicle vehicle) {

        ParkingSpot occupiedSpot = spots.stream()
                .filter(spot -> spot.getAssignedVehicle().equals(vehicle))
                .findFirst()
                .orElse(null);

        if (occupiedSpot != null) {

            vehicle.unPark();

            occupiedSpot.setRemoveVehicle();

            System.out.println(
                    vehicle.getPlate() + " park yerinden başarıyla çıkarıldı" + occupiedSpot.getSpotID() + "!");
        } else {
            System.out.println("Bu bölgede araç bulunamadı.");
        }
    }

    public int getAvailableSpotCount() {
        int empty = (int) spots.stream().filter(spot -> !spot.getIsOccupied()).count();
        System.out.println("Boş yer: " + empty);
        return empty;
    }

    public String getAvailableNextSpotID() {
        ParkingSpot nextSpot = spots.stream()
                .filter(spot -> !spot.getIsOccupied())
                .findFirst()
                .orElse(null);

        return (nextSpot != null) ? nextSpot.getSpotID() : "Yok";
    }

    public void printParkedVehicles() {
        System.out.println("\n--- Bölgedeki Park Halindeki Araçlar " + zoneID + " ---");
        List<Vehicle> parked = getParkedVehicle();
        if (parked.isEmpty()) {
            System.out.println("Şu anda park halinde araç bulunmamaktadır..");
        } else {
            for (Vehicle v : parked) {
                System.out.println(v);
            }
        }
        System.out.println("----------------------------------------\n");
    }
}