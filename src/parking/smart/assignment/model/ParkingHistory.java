package parking.smart.assignment.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class ParkingHistory {
    private String plate;
    private String spotID;
    private String zoneID;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    public ParkingHistory(String plate, String zoneID, String spotID, LocalDateTime entryTime) {
        this.plate = plate;
        this.zoneID = zoneID;
        this.spotID = spotID;
        this.entryTime = entryTime;

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
