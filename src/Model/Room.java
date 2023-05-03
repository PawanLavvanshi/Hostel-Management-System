package Model;

import java.io.Serializable;

public class Room implements Serializable {
    int roomNo;
    String roomType;
    int occupancy, currentOccupancy;
    private static final long serialVersionUID = 9876543210L;

    public Room(int roomNo, String roomType, int occupany) {
        this.roomNo = roomNo;
        this.roomType = roomType;
        this.occupancy = occupany;
        this.currentOccupancy = 0;
        // this.isavailable = true;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public void setCurrentOccupancy(int changedOccupancy) {
        this.currentOccupancy = changedOccupancy;
    }

    public int getCurrentOccupancy() {
        return currentOccupancy;
    }

    public boolean isAvailable() {
        return currentOccupancy < occupancy;
    }

    public String toString() {
        return " Room No=" + roomNo +
                ", Room Type=" + roomType +
                ", Occupancy=" + occupancy +
                ", Current Occupancy=" + currentOccupancy;
    }

    public String[] toTableRow() {
        return new String[] {
                String.valueOf(roomNo),
                roomType,
                String.valueOf(occupancy),
                String.valueOf(currentOccupancy)
        };
    }
}
