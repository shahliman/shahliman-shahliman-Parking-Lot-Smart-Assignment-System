package parking.smart.assignment.service;

import parking.smart.assignment.model.ParkingSpot;
import parking.smart.assignment.model.Vehicle;
import parking.smart.assignment.model.Vehicle.VehicleSize;
import parking.smart.assignment.util.DatabaseConfig;
import parking.smart.assignment.model.ParkingHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AssignmentService {

    private final SpotService spotService;
    private final HistoryService historyService;
    private final PaymentService paymentService;

    public AssignmentService(SpotService spotService, HistoryService historyService, PaymentService paymentService) {
        this.spotService = spotService;
        this.historyService = historyService;
        this.paymentService = paymentService;
    }

    public SpotService getSpotService() {
        return this.spotService;
    }

    public boolean parkVehicle(Vehicle vehicle) {
        ParkingSpot existingSpot = spotService.findSpotByPlate(vehicle.getPlate());

        if (existingSpot != null) {
            System.out.println("HATA: Araç " + vehicle.getPlate() + " zaten park halinde.");
            return false;
        }

        VehicleSize size = vehicle.getSize();
        ParkingSpot availableSpot = spotService.findAvailableSpot(size);

        if (availableSpot != null) {

            availableSpot.setAssignVehicle(vehicle);
            vehicle.assignSpot(availableSpot.getSpotID());
            vehicle.park();
            historyService.createParkingEntry(vehicle);

            saveEntryToDatabase(vehicle, availableSpot.getSpotID());

            System.out.println("BAŞARILI: " + vehicle.getPlate() + " Veritabanına kaydedilen bilgiler.");
            return true;
        } else {
            System.out.println("HATA: Boş yer bulunmadı.");
            return false;
        }
    }

    public ParkingHistory unParkVehicle(Vehicle vehicle) {
        ParkingSpot occupiedSpot = spotService.findSpotByPlate(vehicle.getPlate());

        if (occupiedSpot != null) {
            Vehicle realVehicle = occupiedSpot.getVehicle();
            realVehicle.setExitTime(java.time.LocalDateTime.now());

            ParkingHistory completedHistory = historyService.completedHistoryRecords(realVehicle);

            if (completedHistory != null) {
                paymentService.calculateFee(completedHistory);

                updateExitInDatabase(completedHistory);

                occupiedSpot.setRemoveVehicle();
                return completedHistory;
            }
        }
        return null;
    }

    private void saveEntryToDatabase(Vehicle vehicle, String spotId) {
        String sql = "INSERT INTO parking_history (plate, spot_id, vehicle_size, entry_time) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vehicle.getPlate());
            pstmt.setString(2, spotId);
            pstmt.setString(3, vehicle.getSize().toString());
            pstmt.setTimestamp(4, Timestamp.valueOf(vehicle.getEntryTime()));

            pstmt.executeUpdate();
            System.out.println("DB: Giriş kaydı yaratıldı.");
        } catch (SQLException e) {
            System.err.println("DB HATA: " + e.getMessage());
        }
    }

    private void updateExitInDatabase(ParkingHistory history) {
        String sql = "UPDATE parking_history SET exit_time = ?, fee = ? WHERE plate = ? AND exit_time IS NULL";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(history.getExitTime()));
            pstmt.setDouble(2, history.getFee());
            pstmt.setString(3, history.getPlate());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("DB: Çıkış kaydı güncellendi.");
            } else {
                System.out.println("DB: Güncellenicek aktiv kayd bulunamadı!");
            }
        } catch (SQLException e) {
            System.err.println("DB HATA: " + e.getMessage());
        }
    }
}