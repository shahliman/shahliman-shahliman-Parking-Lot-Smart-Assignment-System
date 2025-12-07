package parking.smart.assignment.service;

import parking.smart.assignment.model.ParkingHistory;
import parking.smart.assignment.model.Vehicle.VehicleSize;

public class PaymentService {
    // PRO payment modulu
    private static final double Hourly_Rate_Small = 2.0;
    private static final double Hourly_Rate_Medium = 3.0;
    private static final double Hourly_Rate_Large = 5.0;

    public PaymentService() {
    };

    public double calculateFee(ParkingHistory history) {
        if (history == null) {
            return 0.0;
        }

        long durationInMinutes = history.getParkingDurationInMinutes();

        long hoursBilled = (long) Math.ceil(durationInMinutes / 60.0);

        if (durationInMinutes > 0 && hoursBilled == 0) {
            hoursBilled = 1;
        }

        double hourlyRate = getHourlyRate(history.getVehicleSize());

        double finalFee = hourlyRate * hoursBilled;

        history.setFee(finalFee);

        System.out.println(history.getPlate() + " parked for " + durationInMinutes + " min. Billed " + hoursBilled
                + " hours. Fee: " + String.format("%.2f", finalFee) + " AZN.");

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
