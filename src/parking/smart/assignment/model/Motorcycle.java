package parking.smart.assignment.model;

public class Motorcycle extends Vehicle {
    public Motorcycle(String plate, VehicleSize size) {
        super(plate, VehicleType.Motorcycle, size);

    }

    @Override
    public void park() {
        super.park();
        System.out.println("Motorcycle with plate " + this.getPlate() + " has been parked.");

    }

}
