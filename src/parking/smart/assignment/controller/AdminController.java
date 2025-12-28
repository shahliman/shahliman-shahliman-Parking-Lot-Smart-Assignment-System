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

    // --- BU METODLARI ƏLAVƏ ET ---
    public HistoryService getHistoryService() {
        return historyService;
    }

    public SpotService getSpotService() {
        return spotService;
    }

    // Mövcud digər metodların aşağıda qalır...
    public void viewCurrentStatues() {
        spotService.printAllSpotStatuses();
    }
    // ...
}