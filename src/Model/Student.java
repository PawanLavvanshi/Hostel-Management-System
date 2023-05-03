package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Student implements Serializable {
    private int rollNo;
    private String name;
    private String fathersName;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private int hostelNo;
    private int roomNo;
    private static final long serialVersionUID = 2468135790L;
    private transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Student(int rollNo, String name, String fatherName, String gender, LocalDate dob, String address,
            int hostelNo, int roomNo) {
        this.rollNo = rollNo;
        this.name = name;
        this.fathersName = fatherName;
        this.gender = gender;
        this.dateOfBirth = dob;
        this.address = address;
        this.hostelNo = hostelNo;
        this.roomNo = roomNo;
    }

    public String toString() {
        return " Student's Roll No: " + rollNo + ", Name: " + name + ", Father's Name: " + fathersName
                + ", Gender: " + gender + ", Date of Birth: " + dateOfBirth.format(formatter) + ", Address: " + address
                + ", Hostel: " + hostelNo + ", Room No: " + roomNo + "\n";
    }

    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFathersName() {
        return fathersName;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getHostelNo() {
        return hostelNo;
    }

    public void setHostelNo(int hostelNo) {
        this.hostelNo = hostelNo;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    // Implementing readObject and writeObject methods for custom serialization
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.defaultWriteObject();
    }
}
