package parking.smart.assignment.service;

import parking.smart.assignment.model.ParkingHistory;
import parking.smart.assignment.model.Vehicle;
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
                v.getPlate(),
                v.getAssignedSpotID().substring(0, 1),
                v.getAssignedSpotID(),
                v.getEntryTime(),
                v.getSize());
        activeHistoryRecords.put(v.getPlate(), history);
        System.out.println("History: New entry created for " + v.getPlate());

    }

    public ParkingHistory completedHistoryRecords(Vehicle v) {
        ParkingHistory history = activeHistoryRecords.get(v.getPlate());

        if (history != null) {
            history.setExitTime(v.getExitTime());

            activeHistoryRecords.remove(v.getPlate());

            completedHistoryRecords.add(history);

            System.out.println("History: Exit recorded for " + v.getPlate());
            return history;
        }
        System.out.println("History: Error - Active record not found for " + v.getPlate());
        return null;
    }

    public void printCompletedHistoryRecords() {
        System.out.println("\n*** TAMAMLANMIŞ PARKİNG TARİXÇƏSİ HESABATI ***");
        if (completedHistoryRecords.isEmpty()) {
            System.out.println("Heç bir tamamlanmiş parkinq sessiyasi yoxdur.");
            return;
        }
        completedHistoryRecords.forEach(System.out::println);
        System.out.println("----------------------------------------------");

    }

}
