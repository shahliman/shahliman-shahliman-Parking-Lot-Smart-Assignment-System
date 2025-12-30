// package parking.smart.assignment.service;

// import parking.smart.assignment.model.Vehicle;
// import parking.smart.assignment.util.PlateValidator;

// import java.util.HashMap;
// import java.util.Map;

// public class VehicleService {

// private final Map<String, Vehicle> registeredVehicles;

// public VehicleService() {
// this.registeredVehicles = new HashMap<>();
// }

// public void registerVehicle(Vehicle vehicle) {
// if (!PlateValidator.isValid(vehicle.getPlate())) {
// System.out.println("Araç Servisi HATASI: " + vehicle.getPlate()
// + " plaka yanlış formatta! Kayıt reddedildi.");
// return;
// }
// if (!registeredVehicles.containsKey(vehicle.getPlate())) {
// registeredVehicles.put(vehicle.getPlate(), vehicle);
// System.out.println("Araç Servisi: Araç " + vehicle.getPlate() + " Kayıt
// işlemi başarıyla tamamlandı..");
// } else {
// System.out.println("Araç Servisi: Araç " + vehicle.getPlate() + " zaten
// kayıtlı.");
// }
// }

// public Vehicle getVehicleByPlate(String plate) {
// return registeredVehicles.get(plate);
// }

// public void printRegisteredVehicles() {
// System.out.println("\n*** KAYITLI ARAÇLAR ***");
// if (registeredVehicles.isEmpty()) {
// System.out.println("Sistemde kayıtlı hiçbir araç bulunmamaktadır.");
// return;
// }
// registeredVehicles.forEach((plate, vehicle) -> System.out
// .println("Plaka: " + plate + " | Tür: " +
// vehicle.getClass().getSimpleName()));

// System.out.println("----------------------------------------------");
// }
// }