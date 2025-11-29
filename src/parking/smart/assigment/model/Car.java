package parking.smart.assigment.model;

public class Car extends Vehicle {
    public Car(String plate, VehicleSize size) {
        super(plate, VehicleType.Car, size);
    }

    @Override
    public void Park() {
        super.Park();
        System.out.println("Car with plate " + this.getPlate() + " has been parked.");
    }

}
