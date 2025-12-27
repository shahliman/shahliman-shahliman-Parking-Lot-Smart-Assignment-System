package parking.smart.assignment.model;

public class ParkingSpot {
    private String spotID;
    private String type;
    private boolean isOccupied;
    private Vehicle assignedVehicle;

    public ParkingSpot(String spotID, String type) {
        this.spotID = spotID;
        this.type = type;
        this.isOccupied = false;
        this.assignedVehicle = null;

    }

    public String getSpotID() {
        return spotID;
    }

    public Vehicle getVehicle() {
        return assignedVehicle;
    }

    public String getType() {
        return type;
    }

    public boolean getIsOccupied() {
        return isOccupied;
    }

    public Vehicle getAssignedVehicle() {
        return assignedVehicle;
    }

    public void setAssignVehicle(Vehicle vehicle) {
        this.assignedVehicle = vehicle;
        this.isOccupied = true;
    }

    public void setRemoveVehicle() {
        this.assignedVehicle = null;
        this.isOccupied = false;
    }

}