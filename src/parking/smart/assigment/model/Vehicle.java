package parking.smart.assigment.model;

import java.time.LocalDateTime;

public class Vehicle {
    private String plate;
    private String size;
    private boolean isParked;
    private String assignedSpotID;
    private LocalDateTime entryTime;

    public Vehicle(String plate, String size) {
        this.plate = plate;
        this.size = size;
        this.isParked = false;
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
        return plate + " | " + size + " | parked=" + isParked + " | spot=" + assignedSpotID;
    }

}
