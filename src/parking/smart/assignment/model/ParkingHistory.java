package parking.smart.assignment.model;

import java.time.LocalDateTime;

import parking.smart.assignment.util.DateUtil;

public class ParkingHistory {
    private Vehicle vehicle;
    private String plate;
    private String zoneID;
    private String spotID;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Vehicle.VehicleSize vehicleSize;
    private double fee;

    public ParkingHistory(Vehicle vehicle, String plate, String zone, String spotID, LocalDateTime entryTime,
            Vehicle.VehicleSize size) {
        this.vehicle = vehicle;
        this.plate = plate;
        this.zoneID = zone;
        this.spotID = spotID;
        this.entryTime = entryTime;
        this.vehicleSize = size;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public String getPlate() {
        return plate;
    }

    public String getSpotID() {
        return spotID;
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

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    @Override
    public String toString() {

        String entryStr = DateUtil.formatDateTime(entryTime);
        String exitStr = (exitTime != null) ? DateUtil.formatDateTime(exitTime) : "Henüz Parkdan Çıkmadı";

        long duration = DateUtil.getMinutesBetween(entryTime, exitTime);

        return String.format(
                "Geçmiş [Numara: %s | Konum: %s | Bölge: %s | Giriş: %s | Çıkış: %s | Süre: %d dk | Ücret: %.2f AZN]",
                plate, spotID, zoneID, entryStr, exitStr, duration, fee);
    }

}
