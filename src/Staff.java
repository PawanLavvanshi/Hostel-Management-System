import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Staff implements Serializable {
    private int staffId;
    private String staffName;
    private String staffFatherName;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private String staffType;
    private String job;
    private int workingHostel;
    private String email;
    private long mobNo;
    private static final long serialVersionUID = 1357924680L;
    private transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Staff(int id, String name, String fname, String gender, LocalDate dob, String address, String type,
            String job, int workingHostel, String email, long mobNo) {
        this.staffId = id;
        this.staffName = name;
        this.staffFatherName = fname;
        this.gender = gender;
        this.dateOfBirth = dob;
        this.address = address;
        this.staffType = type;
        this.job = job;
        this.workingHostel = workingHostel;
        this.email = email;
        this.mobNo = mobNo;
    }

    public String toString() {
        return " Staff Member's Id: " + staffId + ", Name: " + staffName + ", Father's Name: " + staffFatherName
                + ", Gender: " + gender + ", Date of Birth: " + dateOfBirth.format(formatter) + ", Address: " + address
                + ", Staff Type: " + staffType + ", Job: " + job + ", Working Hostel: " + workingHostel + ", Email: "
                + email + ", Mobile No: " + mobNo + "\n";
    }

    public int getStaffId() {
        return staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getStaffFatherName() {
        return staffFatherName;
    }

    public String getStaffGender() {
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

    public int getWorkingHostel() {
        return workingHostel;
    }

    public void setWorkingHostel(int newWorkingHostel) {
        this.workingHostel = newWorkingHostel;
    }

    public String getStaffType() {
        return staffType;
    }

    public String getStaffJob() {
        return job;
    }

    public String getStaffEmail() {
        return email;
    }

    public long getStaffMobNo() {
        return mobNo;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeUTF(formatter.toString());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }
}
