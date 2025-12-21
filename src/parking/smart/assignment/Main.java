package parking.smart.assignment;

import parking.smart.assignment.controller.AdminController;
import parking.smart.assignment.controller.ParkingController;
import parking.smart.assignment.model.*;
import parking.smart.assignment.service.*;
import java.util.Arrays;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SMART PARKING SYSTEM STARTING ===");

        // 1. SERVİSLƏRİ HAZIRLAYIRIQ
        HistoryService historyService = new HistoryService();
        PaymentService paymentService = new PaymentService();
        Zone zoneA = new Zone("A", 10);
        SpotService spotService = new SpotService(Arrays.asList(zoneA));
        AssignmentService assignmentService = new AssignmentService(spotService, historyService, paymentService);
        ParkingController parkingController = new ParkingController(assignmentService, paymentService);
        AdminController adminController = new AdminController(historyService, spotService);
        // 2. MAŞINLAR DAXİL OLUR (Vaxt avtomatik car1 daxilində yaranır)
        System.out.println("\n--- GİRİŞLƏR ---");
        Vehicle car1 = new Car("10-FB-1907", Vehicle.VehicleSize.MEDIUM);
        assignmentService.parkVehicle(car1);

        Vehicle car2 = new Car("99-ZZ-444", Vehicle.VehicleSize.SMALL);
        assignmentService.parkVehicle(car2);

        // 3. ÖDƏNİŞİ TEST ETMƏK ÜÇÜN SİMULYASIYA
        // Əgər setEntryTime yoxdursa, biz ParkingHistory daxilindəki vaxtı əl ilə
        // dəyişib
        // PaymentService-ə göndərə bilərik ki, 0 AZN çıxmasın.

        System.out.println("\n--- ÇIXIŞLAR VƏ ÖDƏNİŞ HESABLANMASI ---");

        // Car 1 çıxış edir
        ParkingHistory h1 = assignmentService.unParkVehicle(car1);
        if (h1 != null) {
            // Ödənişi görmək üçün giriş vaxtını tarixçədə 2 saat əvvələ çəkirik
            forcePastTime(h1, 120);
            paymentService.calculateFee(h1);
        }

        // Car 2 çıxış edir
        ParkingHistory h2 = assignmentService.unParkVehicle(car2);
        if (h2 != null) {
            // 45 dəqiqə əvvələ çəkirik
            forcePastTime(h2, 45);
            paymentService.calculateFee(h2);
        }

        // 4. YEKUN HESABAT
        historyService.printCompletedHistoryRecords();

        System.out.println("\n--- ADMİN PANELİ: YEKUN STATİSTİKA ---");
        adminController.showSystemStatistics();
        adminController.viewFullHistoryReports();
    }

    private static void forcePastTime(ParkingHistory history, int minutesAgo) {
        // Bu hissə üçün ParkingHistory-də setEntryTime olmalıdır.
        // Əgər orada da yoxdursa, PaymentService-də birbaşa hesablama zamanı dəqiqəni
        // əl ilə verə bilərik.
        try {
            java.lang.reflect.Field field = history.getClass().getDeclaredField("entryTime");
            field.setAccessible(true);
            field.set(history, LocalDateTime.now().minusMinutes(minutesAgo));
        } catch (Exception e) {
            // Reflection xətası olarsa, sadəcə keçirik
        }

    }
}