package parking.smart.assignment.service;

import parking.smart.assignment.model.ParkingHistory;
import parking.smart.assignment.model.Vehicle.VehicleSize;
import parking.smart.assignment.util.DateUtil;

public class PaymentService {

    private static final double Hourly_Rate_Small = 2.0;
    private static final double Hourly_Rate_Medium = 3.0;
    private static final double Hourly_Rate_Large = 5.0;

    public PaymentService() {
    };

    public double calculateFee(ParkingHistory history) {
        if (history == null) {
            return 0.0;
        }

        long durationInMinutes = DateUtil.getMinutesBetween(history.getEntryTime(), history.getExitTime());

        long hoursBilled = (long) Math.ceil(durationInMinutes / 60.0);
        if (durationInMinutes > 0 && hoursBilled == 0) {
            hoursBilled = 1;
        }

        double hourlyRate = getHourlyRate(history.getVehicleSize());

        double finalFee = hourlyRate * hoursBilled;

        history.setFee(finalFee);

        System.out.println("--- ÖDƏNİŞ QƏBZİ ---");
        System.out.println("Vasitə: " + history.getPlate());
        System.out.println("Giriş: " + DateUtil.formatDateTime(history.getEntryTime()));
        System.out.println("Çıxış: " + DateUtil.formatDateTime(history.getExitTime()));
        System.out.println("Müddət: " + durationInMinutes + " dəqiqə (" + hoursBilled + " saat hesablanıb)");
        System.out.println("Yekun Məbləğ: " + String.format("%.2f", finalFee) + " AZN");
        System.out.println("--------------------");

        return finalFee;

    }

    private double getHourlyRate(VehicleSize size) {
        switch (size) {
            case SMALL:
                return Hourly_Rate_Small;
            case MEDIUM:
                return Hourly_Rate_Medium;
            case LARGE:
                return Hourly_Rate_Large;
            default:
                return Hourly_Rate_Medium;
        }
    }
}
