package parking.smart.assigment;

import parking.smart.assigment.model.Car;
import parking.smart.assigment.model.Motorcycle;
import parking.smart.assigment.model.Vehicle;
import parking.smart.assigment.model.Zone;

public class Main {
    public static void main(String[] args) {
        Zone zoneA = new Zone("A", 5);

        Car car1 = new Car("10-AB-123", Vehicle.VehicleSize.MEDIUM);
        Car car2 = new Car("01-zz-001", Vehicle.VehicleSize.MEDIUM);
        Motorcycle moto1 = new Motorcycle("20-XY-456", Vehicle.VehicleSize.SMALL);
        zoneA.parkVehicle(car1);
        zoneA.parkVehicle(moto1);
        zoneA.parkVehicle(car2);

        zoneA.printParkedVehicles();

        zoneA.getAvailableSpotCount();

        System.out.println("Next empty spot: " + zoneA.getAvailableNextSpotID());
    }

}
