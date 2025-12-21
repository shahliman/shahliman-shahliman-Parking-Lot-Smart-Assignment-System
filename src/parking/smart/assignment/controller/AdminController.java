package parking.smart.assignment.controller;

import parking.smart.assignment.service.HistoryService;
import parking.smart.assignment.service.SpotService;

public class AdminController {
    private final HistoryService historyService;
    private final SpotService spotService;

    public AdminController(HistoryService historyService, SpotService spotService) {
        this.historyService = historyService;
        this.spotService = spotService;
    }

    public void viewCurrentStatues() {
        System.out.println("\n--- [ADMİN] CARİ PARKİNG VƏZİYYƏTİ ---");
        spotService.printAllSpotStatuses();
    }

    public void viewFullHistoryReports() {
        System.out.println("\n--- [ADMİN] TAM TARİXÇƏ HESABATI ---");
        historyService.printCompletedHistoryRecords();

    }

    public void showSystemStatistics() {
        System.out.println("\n--- [ADMİN] SİSTEM STATİSTİKALARI ---");
        double totalRevenue = historyService.getTotalRevenue();
        int totalTransactions = historyService.getCompletedHistoryRecordsCount();
        System.out.println("Cəmi qazanc: " + String.format("%.2f", totalRevenue) + " AZN");
        System.out.println("Tamamlanmış parkinq sayı: " + totalTransactions);
        System.out.println("--------------------------------------");
    }

}
