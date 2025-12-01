package parking.smart.assigment.model;

import java.util.ArrayList;
import java.util.List;

public class Zone {
    private String zoneID;
    private int capacity;
    private List<Vehicle> parkedVehicles;

    public Zone(String zoneID, int capacity) {
        this.zoneID = zoneID;
        this.capacity = capacity;
        this.parkedVehicles = new ArrayList<>();
    }

    public String getZoneID() {
        return zoneID;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Vehicle> getParkedVehicle() {
        return parkedVehicles;
    }

    public boolean parkVehicle(Vehicle vehicle) {
        if (parkedVehicles.size() < capacity) {
            parkedVehicles.add(vehicle);
            vehicle.assignSpot(zoneID + (parkedVehicles.size()));
            vehicle.Park();
            return true;
        } else {
            System.out.println("Zone " + zoneID + " is full!");
            return false;
        }
    }

    public int getAvailableSpotCount() {
        int empty = capacity - parkedVehicles.size();
        System.out.println("Empty spots: " + empty);
        return empty;
    }

    public String getAvailableNextSpotID() {
        if (parkedVehicles.size() < capacity) {
            return zoneID + (parkedVehicles.size() + 1);
        }
        return null;

    }

    public void printParkedVehicles() {
        for (Vehicle v : parkedVehicles) {
            System.out.println(v);
        }
    }

}
