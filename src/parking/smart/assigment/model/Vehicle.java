package parking.smart.assigment.model;

import java.time.LocalDateTime;

import parking.smart.assigment.model.Vehicle.VehicleSize;

public class Vehicle {
    private String plate;
    private VehicleType type;
    private VehicleSize size;
    private boolean isParked;
    private String assignedSpotID;
    private LocalDateTime entryTime;

    public Vehicle(String plate, VehicleType type, VehicleSize size) {
        this.plate = plate;
        this.type = type;
        this.size = size;
        this.isParked = false;
    }

    public enum VehicleType {
        Car,
        Motorcycle,
    }

    public enum VehicleSize {
        SMALL,
        MEDIUM,
        LARGE
    }

    public String getPlate() {
        return plate;
    }

    public VehicleSize getSize() {
        return size;
    }

    public boolean getIsParked() {
        return isParked;
    }

    public String getAssignedSpotID() {
        return assignedSpotID;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public void setSize(VehicleSize size) {
        this.size = size;
    }

    public void Park() {
        this.isParked = true;
        this.entryTime = LocalDateTime.now();
    }

    public void leave() {
        this.isParked = false;
    }

    public void assignSpot(String spotID) {
        this.assignedSpotID = spotID;
    }

    @Override
    public String toString() {
        return type + "|" + plate + " | " + size + " | parked=" + isParked + " | spot=" + assignedSpotID;
    }

}
