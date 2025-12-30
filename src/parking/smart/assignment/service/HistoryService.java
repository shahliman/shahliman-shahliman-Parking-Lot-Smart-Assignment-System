package parking.smart.assignment.service;

import parking.smart.assignment.model.ParkingHistory;
import parking.smart.assignment.model.Vehicle;
import parking.smart.assignment.util.DateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryService {
    private final Map<String, ParkingHistory> activeHistoryRecords;
    private final List<ParkingHistory> completedHistoryRecords;

    public HistoryService() {
        this.activeHistoryRecords = new HashMap<>();
        this.completedHistoryRecords = new ArrayList<>();
    }

    public void createParkingEntry(Vehicle v) {
        ParkingHistory history = new ParkingHistory(
                v,
                v.getPlate(),
                v.getAssignedSpotID().substring(0, 1),
                v.getAssignedSpotID(),
                v.getEntryTime(),
                v.getSize());
        activeHistoryRecords.put(v.getPlate(), history);
        System.out.println("Tarihçe: Yeni giriş oluşturuldu " + v.getPlate() +
                " da " + DateUtil.formatDateTime(v.getEntryTime()));
    }

    public ParkingHistory completedHistoryRecords(Vehicle v) {

        ParkingHistory history = activeHistoryRecords.get(v.getPlate());

        if (history != null) {
            history.setExitTime(v.getExitTime());
            activeHistoryRecords.remove(v.getPlate());
            completedHistoryRecords.add(history);

            return history;
        }
        System.out.println("Tarihçe: Hata - Aktif kayıt bulunamadı " + v.getPlate());
        return null;
    }

    public int getCompletedHistoryRecordsCount() {
        return completedHistoryRecords.size();
    }

    public double getTotalRevenue() {
        double total = 0.0;
        for (ParkingHistory records : completedHistoryRecords) {
            total += records.getFee();
        }
        return total;
    }

    public List<ParkingHistory> getCompletedHistoryRecords() {

        return completedHistoryRecords;
    }

    public void printCompletedHistoryRecords() {
        System.out.println("\n***TAMAMLANMIŞ PARK GEÇMİŞİ RAPORU ***");

        if (completedHistoryRecords.isEmpty()) {
            System.out.println("Tamamlanmış park seansı bulunmamaktadır..");
            return;
        }

        for (ParkingHistory record : completedHistoryRecords) {
            long minutes = DateUtil.getMinutesBetween(record.getEntryTime(), record.getExitTime());

            System.out.println(
                    "Plaka: " + record.getPlate() +
                            " | Yer: " + record.getSpotID() +
                            " | Giriş: " + DateUtil.formatDateTime(record.getEntryTime()) +
                            " | Çıkış: " + DateUtil.formatDateTime(record.getExitTime()) +
                            " | Süre: " + minutes + " dəq" +
                            " | Ücret: " + String.format("%.2f", record.getFee()) + " AZN");
        }
        System.out.println("----------------------------------------------");
    }
}