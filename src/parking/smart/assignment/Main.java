package parking.smart.assignment;

import parking.smart.assignment.model.Car;
import parking.smart.assignment.model.Motorcycle;
import parking.smart.assignment.model.Vehicle;
import parking.smart.assignment.model.Zone;

public class Main {
    public static void main(String[] args) {
        Zone zoneA = new Zone("A", 5);

        Car car1 = new Car("10-AB-123", Vehicle.VehicleSize.MEDIUM);
        Car car2 = new Car("01-ZZ-001", Vehicle.VehicleSize.MEDIUM);
        Motorcycle moto1 = new Motorcycle("20-XY-456", Vehicle.VehicleSize.SMALL);
        Car car3 = new Car("30-TEST-030", Vehicle.VehicleSize.MEDIUM);

        // 1. Parklanma
        zoneA.parkVehicle(car1); // A1
        zoneA.parkVehicle(moto1); // A2
        zoneA.parkVehicle(car2); // A3

        // A2 boşalır
        zoneA.unParkVehicle(moto1);

        // 2. Boş Spotların vəziyyəti
        zoneA.getAvailableSpotCount(); // 3 boş spot (A2, A4, A5)
        System.out.println("Next empty spot: " + zoneA.getAvailableNextSpotID()); // Nəticə A2 olmalıdır!

        // 3. A2-nin təkrar doldurulması (Test)
        zoneA.parkVehicle(car3); // A2-yə park olunmalıdır

        // 4. Son vəziyyət
        zoneA.printParkedVehicles();
        zoneA.getAvailableSpotCount(); // 2 boş spot (A4, A5)
        System.out.println("Next empty spot : " + zoneA.getAvailableNextSpotID()); // Nəticə A4 olmalıdır!
    }
}