package parking.smart.assignment.model;

import java.time.LocalDateTime;

import parking.smart.assignment.model.Vehicle.VehicleSize;
import parking.smart.assignment.util.DateUtil;

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
        // 2. DateUtil-i burada istifadə edərək vaxtları formatlayırıq
        String entryStr = DateUtil.formatDateTime(entryTime);
        String exitStr = (exitTime != null) ? DateUtil.formatDateTime(exitTime) : "Hələ çıxış etməyib";

        // 3. İki vaxt arasındakı dəqiqəni hesablamaq üçün yenə Util-dən istifadə edirik
        long duration = DateUtil.getMinutesBetween(entryTime, exitTime);

        return String.format(
                "Tarixçə [Nömrə: %s | Yer: %s | Zone: %s | Giriş: %s | Çıxış: %s | Müddət: %d dəq | Ödəniş: %.2f AZN]",
                plate, spotID, zoneID, entryStr, exitStr, duration, fee);
    }

}
