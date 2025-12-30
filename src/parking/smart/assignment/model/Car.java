package parking.smart.assignment.model;

public class Car extends Vehicle {
    public Car(String plate, VehicleSize size) {
        super(plate, VehicleType.Car, size);
    }

    @Override
    public void park() {
        super.park();
        System.out.println("Araba " + this.getPlate() + " Park edildi.");
    }

}
