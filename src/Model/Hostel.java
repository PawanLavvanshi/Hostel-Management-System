package Model;

import java.io.Serializable;

public class Hostel implements Serializable {
    int hostelNo;
    String hostelName;
    String hostelAddress;
    int numberOfRooms, availableRooms;
    int numberOfStaff;
    String wardenName, wardenEmail;
    private static final long serialVersionUID = 1234567890L;

    public Hostel(int hostelNo, String hostelName, String hostelAddress, String wardenName, String wardenEmail) {
        this.hostelNo = hostelNo;
        this.hostelName = hostelName;
        this.hostelAddress = hostelAddress;
        this.numberOfRooms = 0;
        this.availableRooms = 0;
        this.numberOfStaff = 0;
        this.wardenName = wardenName;
        this.wardenEmail = wardenEmail;
    }

    public String toString() {
        return "Hostel [hostelNo=" + hostelNo + ", hostelName=" + hostelName + ", hostelAddress=" + hostelAddress
                + "\n numberOfRooms=" + numberOfRooms + ", availableRooms=" + availableRooms + ", numberOfStaff="
                + numberOfStaff + "\n wardenName=" + wardenName + ", wardenEmail=" + wardenEmail + "]";
    }

    public int getHostelNo() {
        return hostelNo;
    }

    public void setHostelNo(int newHostelNo) {
        this.hostelNo = newHostelNo;
    }

    public String getHostelName() {
        return hostelName;
    }

    public void setHostelName(String newHoatelName) {
        this.hostelName = newHoatelName;
    }

    public String getHostelAddress() {
        return hostelAddress;
    }

    public void setHostelAddress(String newAddress) {
        this.hostelAddress = newAddress;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public int getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(int availableRooms) {
        this.availableRooms = availableRooms;
    }

    public int getNumberOfStaff() {
        return numberOfStaff;
    }

    public void setNumberOfStaff(int newNumberOfStaff) {
        this.numberOfStaff = newNumberOfStaff;
    }

    public String[] toTableRow() {
        return new String[] {
                String.valueOf(hostelNo),
                hostelName,
                hostelAddress,
                String.valueOf(numberOfRooms),
                String.valueOf(availableRooms),
                String.valueOf(numberOfStaff),
                wardenName,
                wardenEmail
        };
    }

    public String getWardenName() {
        return wardenName;
    }

    public String getWardenEmail() {
        return wardenEmail;
    }

}