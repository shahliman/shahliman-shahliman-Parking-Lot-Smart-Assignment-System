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
        System.out.println("History: New entry created for " + v.getPlate() +
                " at " + DateUtil.formatDateTime(v.getEntryTime()));
    }

    public ParkingHistory completedHistoryRecords(Vehicle v) {
        // DÜZƏLİŞ: Burada yalnız v.getPlate() olmalıdır. Vaxtı axtarışa qatmayın.
        ParkingHistory history = activeHistoryRecords.get(v.getPlate());

        if (history != null) {
            history.setExitTime(v.getExitTime());
            activeHistoryRecords.remove(v.getPlate());
            completedHistoryRecords.add(history);

            System.out.println("History: Exit recorded for " + v.getPlate() +
                    " at " + DateUtil.formatDateTime(v.getExitTime()));
            return history;
        }
        System.out.println("History: Error - Active record not found for " + v.getPlate());
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
        // Tamamlanmış tarixçə siyahısını qaytarır
        return this.completedHistoryRecords;
    }

    public void printCompletedHistoryRecords() {
        System.out.println("\n*** TAMAMLANMIŞ PARKİNG TARİXÇƏSİ HESABATI ***");

        if (completedHistoryRecords.isEmpty()) {
            System.out.println("Heç bir tamamlanmiş parkinq sessiyasi yoxdur.");
            return;
        }

        for (ParkingHistory record : completedHistoryRecords) {
            long minutes = DateUtil.getMinutesBetween(record.getEntryTime(), record.getExitTime());

            System.out.println(
                    "Nömrə: " + record.getPlate() +
                            " | Yer: " + record.getSpotID() +
                            " | Giriş: " + DateUtil.formatDateTime(record.getEntryTime()) +
                            " | Çıxış: " + DateUtil.formatDateTime(record.getExitTime()) +
                            " | Müddət: " + minutes + " dəq" +
                            " | Ödəniş: " + String.format("%.2f", record.getFee()) + " AZN");
        }
        System.out.println("----------------------------------------------");
    }
}