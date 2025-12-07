package parking.smart.assignment.model;

import java.time.Duration;
import java.time.LocalDateTime;

import parking.smart.assignment.model.Vehicle.VehicleSize;

public class ParkingHistory {
    private String plate;
    private String spotID;
    private String zoneID;
    private double fee;
    private Vehicle.VehicleSize vehicleSize;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    public ParkingHistory(String plate, String zoneID, String spotID, LocalDateTime entryTime, VehicleSize size) {
        this.plate = plate;
        this.zoneID = zoneID;
        this.spotID = spotID;
        this.vehicleSize = size;
        this.entryTime = entryTime;

        this.fee = 0.0;

    }

    public String getPlate() {
        return plate;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public Vehicle.VehicleSize getVehicleSize() {
        return vehicleSize;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public long getParkingDurationInMinutes() {
        if (exitTime == null)
            return 0;
        return Duration.between(entryTime, exitTime).toMinutes();

    }

    @Override
    public String toString() {
        return plate + " | zone=" + zoneID +
                " | spot=" + spotID +
                " | in=" + entryTime +
                " | out=" + exitTime +
                " | duration=" + getParkingDurationInMinutes() + " min";
    }

}
