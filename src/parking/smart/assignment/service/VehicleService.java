package parking.smart.assignment.service;

import parking.smart.assignment.model.Vehicle;
import parking.smart.assignment.util.PlateValidator;

import java.util.HashMap;
import java.util.Map;

public class VehicleService {

    private final Map<String, Vehicle> registeredVehicles;

    public VehicleService() {
        this.registeredVehicles = new HashMap<>();
    }

    public void registerVehicle(Vehicle vehicle) {
        if (!PlateValidator.isValid(vehicle.getPlate())) {
            System.out.println("VehicleService ERROR: " + vehicle.getPlate()
                    + " nömrəsi yanlış formatdadır! Qeydiyyat rədd edildi.");
            return;
        }
        if (!registeredVehicles.containsKey(vehicle.getPlate())) {
            registeredVehicles.put(vehicle.getPlate(), vehicle);
            System.out.println("VehicleService: Vehicle " + vehicle.getPlate() + " registered successfully.");
        } else {
            System.out.println("VehicleService: Vehicle " + vehicle.getPlate() + " is already registered.");
        }
    }

    public Vehicle getVehicleByPlate(String plate) {
        return registeredVehicles.get(plate);
    }

    public void printRegisteredVehicles() {
        System.out.println("\n*** QEYDİYYATDAN KEÇMİŞ NƏQLİYYAT VASİTƏLƏRİ ***");
        if (registeredVehicles.isEmpty()) {
            System.out.println("Sistemdə qeydiyyatdan keçmiş heç bir avtomobil yoxdur.");
            return;
        }
        registeredVehicles.forEach((plate, vehicle) -> System.out
                .println("Plate: " + plate + " | Type: " + vehicle.getClass().getSimpleName()));

        System.out.println("----------------------------------------------");
    }
}