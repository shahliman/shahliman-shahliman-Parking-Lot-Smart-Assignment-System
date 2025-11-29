package parking.smart.assigment.model;

public class Motorcycle extends Vehicle {
    public Motorcycle(String plate, VehicleSize size) {
        super(plate, VehicleType.Motorcycle, size);

    }

    @Override
    public void Park() {
        super.Park();
        System.out.println("Motorcycle with plate " + this.getPlate() + " has been parked.");

    }

}
