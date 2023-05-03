package Administration;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import Model.*;
import HostelManagementSystem.*;

public class Administration {
    static Scanner input = new Scanner(System.in);

    public static void admistrationPortal() throws Exception {
        int adminChoice;
        // admiistration portal
        do {
            System.out.println("\n\nWelcome To Admistration Portal");
            System.out.println("------------------------------");
            System.out.println("1. View hostels");
            System.out.println("2. Check In");
            System.out.println("3. Check out ");
            System.out.println("4. Add Room");
            System.out.println("5. Remove Room");
            System.out.println("6. Add hostel");
            System.out.println("7. Remove Hostel");
            System.out.println("8. View students");
            System.out.println("9. Add Student");
            System.out.println("10. Remove student");
            System.out.println("11. View Staff members");
            System.out.println("12. Add Staff member");
            System.out.println("13. Remove Staff member");
            System.out.println("14. Previous Menu");
            System.out.println("15. Exit from program");
            System.out.println("Enter your choice... ");
            adminChoice = input.nextInt();
            input.nextLine();
            switch (adminChoice) {
                case 1:
                    if (HostelManagementSystem.printHostelsInTable()) {
                        String inputStr;
                        do {
                            System.out.println("Enter the hostel number (or any non-numeric character to exit)... ");
                            inputStr = input.nextLine();
                            if (inputStr.matches("\\d+") && Integer.parseInt(inputStr) >= 0) {
                                int specialhostelchoice = Integer.parseInt(inputStr);
                                if (HostelManagementSystem.checkIfHostelExists(specialhostelchoice)) {
                                    if (HostelManagementSystem.showSpecialHostelDetails(specialhostelchoice)) {
                                        hostelRoomsDetails(specialhostelchoice);
                                    }

                                } else {
                                    System.out.println("Entered a wrong hostel no.");
                                }
                            } else {
                                System.out.println("Redirected to previous menu....");
                            }
                        } while (inputStr.matches("\\d+") && Integer.parseInt(inputStr) >= 0);
                    } else {
                        System.out.println("Please Add First.");
                    }
                    break;
                case 2:
                    System.out.println("Enter Roll Number of student: ");
                    checkIn(input.nextInt());
                    break;
                case 3:
                    System.out.println("Enter Roll Number of student: ");
                    checkOut(input.nextInt());
                    break;
                case 4:
                    System.out.println("Enter the hostel no in which you want to add room... ");
                    int roomAddHostelNo = input.nextInt();
                    input.nextLine();
                    String fileName = "Utils/hostel" + roomAddHostelNo + ".ser";
                    File roomAddHostelFile = new File(fileName);
                    if (roomAddHostelFile.exists() && HostelManagementSystem.checkIfHostelExists(roomAddHostelNo)) {
                        String ans;
                        do {
                            addHostelRoom(roomAddHostelFile, roomAddHostelNo);
                            input.nextLine();
                            System.out.println("Want to add more room in same hostel(y/n)... ");
                            ans = input.nextLine();
                        } while (ans.equalsIgnoreCase("y"));
                    } else {
                        System.out.println("Hostel" + roomAddHostelNo + " does not exist.");
                    }
                    break;
                case 5:
                    System.out.println("Enter the hostel no from which you want to remove room... ");
                    int roomRemoveHostelNo = input.nextInt();
                    input.nextLine();
                    String hostelNameFile = "Utils/hostel" + roomRemoveHostelNo + ".ser";
                    File roomRemoveHostelFile = new File(hostelNameFile);
                    if (roomRemoveHostelFile.exists()
                            && HostelManagementSystem.checkIfHostelExists(roomRemoveHostelNo)) {
                        String ans;
                        do {
                            removeHostelRoom(roomRemoveHostelFile, roomRemoveHostelNo);
                            // input.nextLine();
                            System.out.println("Want to remove more rooms from same hostel(y/n)... ");
                            ans = input.nextLine();
                        } while (ans.equalsIgnoreCase("y"));
                    } else {
                        System.out.println("Hostel" + roomRemoveHostelNo + " does not exist.");
                    }
                    break;
                case 6:
                    addHostel();
                    break;
                case 7:
                    removeHostel();
                    break;
                case 8:
                    viewStudents();
                    break;
                case 9:
                    addStudent();
                    break;
                case 10:
                    removeStudent();
                    break;
                case 11:
                    viewStaff();
                    break;
                case 12:
                    addStaff();
                    break;
                case 13:
                    removeStaff();
                    break;
                case 14:
                    System.out.println("Redirected to Main Menu.... \n");
                    break;
                case 15:
                    HostelManagementSystem.quit();
                    break;
                default:
                    System.out.println("Wrong Choice");

            }
        } while (adminChoice != 14);

    }

    public static void hostelRoomsDetails(int hostelNo) throws FileNotFoundException, IOException {
        String fileName = "Utils/hostel" + hostelNo + ".ser";
        File hostelFile = new File(fileName);

        if (hostelFile.exists()) {
            List<Room> rooms = readRoomsFromFile(hostelFile);
            if (rooms.size() == 0) {
                System.out.println("No Room added in hostel" + hostelNo);
                return;
            }
            System.out.println("\nAll rooms in the Hostel : ");
            System.out.println("-".repeat(73));
            System.out.printf("|%-10s|%-15s|%-10s|%-18s|%-14s|\n", "Room No", "Room Type",
                    "Occupancy", "Current Occupancy", "Is Available");
            System.out.println("-".repeat(73));
            for (Room room : rooms) {
                String[] row = room.toTableRow();
                System.out.printf("|%-10s|%-15s|%-10s|%-18s|%-14s|\n", row[0], row[1], row[2], row[3],
                        room.isAvailable());
            }
            System.out.println("-".repeat(73));
        } else {
            System.out.println("Hostel " + hostelNo + "does not exist.");
            System.out.println("You have entered a wrong choice!!!");
        }
    }

    public static void checkOut(int rollNo) {
        Student student = null;
        List<Student> students = HostelManagementSystem.readStudentsFromFile("Utils/student.ser");
        for (Student stu : students) {
            if (stu.getRollNo() == rollNo) {
                student = stu;
                break;
            }
        }
        if (students.size() == 0 || student == null) {
            System.out.println("Student with roll no " + rollNo + " not found.");
            return;
        }

        // Write checkout data to file
        try {
            FileWriter writer = new FileWriter("Utils/checkInOut.csv", true);
            String data = student.getHostelNo() + ","
                    + student.getRoomNo() + ","
                    + student.getRollNo() + ","
                    + student.getName() + ","
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ","
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ","
                    + ","
                    + "\n";
            writer.write(data);
            writer.close();
            System.out.println("Checkout successful.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void checkIn(int rollNo) {
        // Read checkInOut data from file
        String filePath = "Utils/checkInOut.csv";
        String tempFilePath = "tempCheckInOut.csv";
        boolean rollNoFound = false;
        String[] dataRow;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            FileWriter writer = new FileWriter(tempFilePath, true);
            String line = reader.readLine();
            while (line != null) {
                dataRow = line.split(",");
                if (Integer.parseInt(dataRow[2]) == rollNo) {
                    if (dataRow[6].equals("") && dataRow[7].equals("")) {
                        // Update check-in date and time
                        dataRow[6] = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        dataRow[7] = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        rollNoFound = true;
                    }
                }
                writer.write(String.join(",", dataRow) + "\n");
                line = reader.readLine();
            }
            reader.close();
            writer.close();
            // Replace the original file with the updated temp file
            File originalFile = new File(filePath);
            originalFile.delete();
            File newFile = new File(tempFilePath);
            newFile.renameTo(originalFile);
        } catch (FileNotFoundException e) {
            rollNoFound = false;
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        if (!rollNoFound) {
            // Read the Student object from file
            Student student = null;
            List<Student> students = HostelManagementSystem.readStudentsFromFile("Utils/student.ser");
            for (Student stu : students) {
                if (stu.getRollNo() == rollNo) {
                    student = stu;
                    break;
                }
            }
            if (students.size() == 0 || student == null) {
                System.out.println("Student with roll no " + rollNo + " not found in file.");
                return;
            }
            // Write check-in data to file
            try {
                FileWriter writer = new FileWriter(filePath, true);
                String data = student.getHostelNo() + ","
                        + student.getRoomNo() + ","
                        + student.getRollNo() + ","
                        + student.getName() + ","
                        + ","
                        + ","
                        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ","
                        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n";
                writer.write(data);
                writer.close();
                System.out.println("Check-in successful.");
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Check-in successful.");
        }
    }

    public static void addHostel() throws IOException, ClassNotFoundException {
        List<Hostel> hostels = HostelManagementSystem.readHostelsFromFile("Utils/hostel.ser");
        // get hostel details from user
        System.out.println("\nEnter Hostel details to add it.");
        System.out.println("Enter hostel number: ");
        int hostelNo = input.nextInt();

        // Check if the hostel number already exists in the file
        File hostelFile = new File("Utils/hostel.ser");
        while (HostelManagementSystem.checkIfHostelExists(hostelNo)) {
            System.out.println("Hostel with that number already exists. Please enter a different number.");
            System.out.print("Enter the hostel number: ");
            hostelNo = input.nextInt();
        }
        input.nextLine();
        System.out.println("Enter hostel name: ");
        String hostelName = input.nextLine();
        System.out.println("Enter hostel address: ");
        String hostelAddress = input.nextLine();
        System.out.println("Enter warden name: ");
        String wardenName = input.nextLine();
        System.out.println("Enter warden contact email: ");
        String wardenEmail = input.nextLine();

        // create hostel object and write to file
        Hostel hostel = new Hostel(hostelNo, hostelName, hostelAddress, wardenName,
                wardenEmail);
        System.out.println("Adding the hostel... " + hostel);
        hostels.add(hostel);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(hostelFile))) {
            out.writeObject(hostels);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // create new file with hostel number
        File newHostelFile = new File("Utils/hostel" + hostelNo + ".ser");
        if (newHostelFile.createNewFile()) {
            System.out.println("\nHostel added successfully.");
        } else {
            System.out.println("Error occured in adding hostel.");
        }
    }

    public static void removeHostel() {
        System.out.println("\nEnter Hostel Number to remove it... ");
        int removeHostelNo = input.nextInt();
        input.nextLine();
        String fileName = "Utils/hostel" + removeHostelNo + ".ser";
        File hostelFile = new File(fileName);

        if (hostelFile.exists() && HostelManagementSystem.checkIfHostelExists(removeHostelNo)) {
            // Hostel file exists
            boolean isRemoved = false;
            Hostel removedHostel = null;
            try {
                List<Hostel> hostels = HostelManagementSystem.readHostelsFromFile("Utils/hostel.ser");
                if (hostels.size() == 0) {
                    System.out.println("There is no Hostel Added. Please add first.");
                    return;
                }
                for (Hostel h : hostels) {
                    if (h.getHostelNo() == removeHostelNo) {
                        System.out.print("You may loose all hostel data.\nPlease confirm (y/n)? ");
                        String answer = input.nextLine();
                        if (answer.equalsIgnoreCase("y")) {
                            System.out.println("Removing the Hostel....\n" + h);
                            try {
                                isRemoved = hostelFile.delete();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (isRemoved) {
                                removedHostel = h;
                            } else {
                                System.out.println("\nFailed to remove the hostel.");
                            }

                        }
                    }
                }
                if (isRemoved) {
                    hostels.remove(removedHostel);
                    System.out.println("\nHostel removed successfully.");
                }
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Utils/hostel.ser"));
                outputStream.writeObject(hostels);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else

        {
            System.out.println("Utils/Hostel" + removeHostelNo + " does not exist.");
        }
    }

    public static ArrayList<Room> readRoomsFromFile(File filename) {
        ArrayList<Room> rooms = new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = in.readObject();
            if (obj instanceof ArrayList) {
                rooms = (ArrayList<Room>) obj;
            } else if (obj instanceof Room) {
                rooms.add((Room) obj);
            }
        } catch (FileNotFoundException | EOFException e) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return rooms;
    }

    public static void addHostelRoom(File roomAddHostelFile, int hostelNo) throws Exception {
        List<Room> rooms = readRoomsFromFile(roomAddHostelFile);

        System.out.println("\n\nEnter Room Number: ");
        int roomNo = input.nextInt();
        while (checkIfRoomNoExists(roomAddHostelFile, roomNo)) {
            System.out.println("Room No is already exist.\nPlease enter different Room No: ");
            roomNo = input.nextInt();
        }
        input.nextLine();
        System.out.print("Enter room type: ");
        String roomType = input.nextLine();

        System.out.print("Enter occupancy: ");
        int occupancy = input.nextInt();

        // Create a new Room object and add it to the list
        Room newRoom = new Room(roomNo, roomType, occupancy);
        rooms.add(newRoom);

        // Write the updated list of rooms to the file
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(roomAddHostelFile))) {
            out.writeObject(rooms);
            System.out.println("Adding the room: " + newRoom);
        }
        // Update the numberOfRooms and availableRooms properties in the hostel file
        List<Hostel> hostels = HostelManagementSystem.readHostelsFromFile("Utils/hostel.ser");

        for (Hostel hostel : hostels) {
            if (hostel.getHostelNo() == hostelNo) {
                hostel.setNumberOfRooms(hostel.getNumberOfRooms() + 1);
                hostel.setAvailableRooms(hostel.getAvailableRooms() + 1);
            }
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Utils/hostel.ser"))) {
            out.writeObject(hostels);
            System.out.println("New room added successfully.");
        }
    }

    private static boolean checkIfRoomNoExists(File roomAddHostelFile, int roomNo) {
        List<Room> rooms = readRoomsFromFile(roomAddHostelFile);
        for (Room room : rooms) {
            if (room.getRoomNo() == roomNo) {
                return true;
            }
        }
        return false;
    }

    public static void removeHostelRoom(File rmFileName, int hostelNo) {
        System.out.println("Enter Room Number to remove it... ");
        int removeRoomNo = input.nextInt();
        input.nextLine();
        if (checkIfRoomNoExists(rmFileName, removeRoomNo)) {
            try {
                List<Room> rooms = readRoomsFromFile(rmFileName);
                Room removedRoom = null;
                boolean roomRemoved = false;
                if (rooms.size() == 0) {
                    System.out.println("Empty Hostel!!!\nNo room to remove.");
                    return;
                }
                for (Room r : rooms) {
                    if (r.getRoomNo() == removeRoomNo) {
                        System.out.print("You may loose all data for that room.\nPlease confirm (y/n)? ");
                        String answer = input.nextLine();
                        if (answer.equalsIgnoreCase("y")) {
                            System.out.println("Removing the Room.....\n" + r);
                            removedRoom = r;
                            roomRemoved = true;
                        }
                    }
                }
                rooms.remove(removedRoom);
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(rmFileName));
                outputStream.writeObject(rooms);
                outputStream.close();
                if (roomRemoved) {
                    // Update the number of rooms and available rooms for the corresponding hostel
                    List<Hostel> hostels = HostelManagementSystem.readHostelsFromFile("Utils/hostel.ser");
                    ObjectOutputStream hostelOutputStream = new ObjectOutputStream(
                            new FileOutputStream("Utils/hostel.ser"));
                    for (Hostel h : hostels) {
                        if (h.getHostelNo() == hostelNo) {
                            h.setNumberOfRooms(h.getNumberOfRooms() - 1);
                            h.setAvailableRooms(h.getAvailableRooms() - 1);
                        }
                    }
                    hostelOutputStream.writeObject(hostels);
                    hostelOutputStream.close();
                    System.out.println("Room removed successfully.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Room with Room No " + removeRoomNo + " doesn't exist in Hostel No" + hostelNo);
        }

    }

    public static void viewStudents() {
        int studentViewChoice;
        do {
            System.out.println("\nStudents View Menu");
            System.out.println("1. All Students");
            System.out.println("2. Search Hostel wise");
            System.out.println("3. Search by Room number");
            System.out.println("4. Search by Roll Number");
            System.out.println("5. Previous Menu");
            System.out.println("6. Exit from program");
            System.out.println("Enter your choice... ");
            studentViewChoice = input.nextInt();
            switch (studentViewChoice) {
                case 1:
                    viewAllStudents();
                    break;
                case 2:
                    System.out.println("Enter the hostel number: ");
                    int hostelnum = input.nextInt();
                    if (HostelManagementSystem.checkIfHostelExists(hostelnum)) {
                        viewStudentsByHostel(hostelnum);
                    } else {
                        System.out.println("Not a valid hostel no.");
                    }
                    break;
                case 3:
                    System.out.println("Enter Hostel No: ");
                    int hostelNum = input.nextInt();
                    String fileName = "Utils/hostel" + hostelNum + ".ser";
                    File hostelFile = new File(fileName);
                    if (HostelManagementSystem.checkIfHostelExists(hostelNum) && hostelFile.exists()) {
                        System.out.println("Enter the Room No: ");
                        int roomNum = input.nextInt();
                        if (checkIfRoomNoExists(hostelFile, roomNum)) {
                            viewStudentsByRoom(hostelNum, roomNum);
                        } else {
                            System.out.println("Room No " + roomNum + " doesn't exist in Hostel No " + hostelNum);
                        }
                    } else {
                        System.out.println("Hostel No " + hostelNum + " doesn't exist.");
                    }
                    break;
                case 4:
                    System.out.println("Enter the Roll No:");
                    int rollNo = input.nextInt();
                    if (HostelManagementSystem.checkIfRollnoExists(rollNo)) {
                        viewStudentByRollNo(rollNo);
                    }
                    break;
                case 5:
                    System.out.println("Redirecting Previous Menu");
                    break;
                case 6:
                    HostelManagementSystem.quit();
                    break;
                default:
                    System.out.println("Wrong Choice.");
            }
        } while (studentViewChoice != 5);

    }

    public static void viewAllStudents() {
        List<Student> students = HostelManagementSystem.readStudentsFromFile("Utils/student.ser");
        if (students.size() == 0) {
            System.out.println("No student to show.\nPlease add first.");
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println("-".repeat(142));
        System.out.printf("|%-10s |%-20s |%-20s |%-10s |%-15s |%-30s |%-10s |%-10s |%n",
                "Roll No", "Name", "Father's Name", "Gender", "Date of Birth", "Address", "Hostel No", "Room No");

        System.out.println("-".repeat(142));

        for (Student student : students) {
            System.out.printf("|%-10d |%-20s |%-20s |%-10s |%-15s |%-30s |%-10d |%-10d |%n",
                    student.getRollNo(), student.getName(), student.getFathersName(),
                    student.getGender(), formatter.format(student.getDateOfBirth()), student.getAddress(),
                    student.getHostelNo(), student.getRoomNo());
        }
        System.out.println("-".repeat(142));
    }

    public static void viewStudentsByHostel(int hostelNo) {
        List<Student> students = HostelManagementSystem.readStudentsFromFile("Utils/student.ser");
        if (students.size() == 0) {
            System.out.println("No student to show.\nPlease add first.");
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println("-".repeat(142));
        System.out.printf("|%-10s |%-20s |%-20s |%-10s |%-15s |%-30s |%-10s |%-10s |%n",
                "Roll No", "Name", "Father's Name", "Gender", "Date of Birth", "Address", "Hostel No", "Room No");

        System.out.println("-".repeat(142));

        students.stream().filter(student -> student.getHostelNo() == hostelNo).forEach(student -> {
            System.out.printf("|%-10d |%-20s |%-20s |%-10s |%-15s |%-30s |%-10d |%-10d |%n",
                    student.getRollNo(), student.getName(), student.getFathersName(),
                    student.getGender(), formatter.format(student.getDateOfBirth()), student.getAddress(),
                    student.getHostelNo(), student.getRoomNo());
        });
        System.out.println("-".repeat(142));

    }

    public static void viewStudentsByRoom(int hostelNo, int roomNo) {
        List<Student> students = HostelManagementSystem.readStudentsFromFile("Utils/student.ser");
        if (students.size() == 0) {
            System.out.println("No student to show.\nPlease add first.");
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println("-".repeat(142));
        System.out.printf("|%-10s |%-20s |%-20s |%-10s |%-15s |%-30s |%-10s |%-10s |%n",
                "Roll No", "Name", "Father's Name", "Gender", "Date of Birth", "Address", "Hostel No", "Room No");

        System.out.println("-".repeat(142));

        students.stream().filter(student -> student.getHostelNo() == hostelNo && student.getRoomNo() == roomNo)
                .forEach(student -> {
                    System.out.printf("|%-10d |%-20s |%-20s |%-10s |%-15s |%-30s |%-10d |%-10d |%n",
                            student.getRollNo(), student.getName(), student.getFathersName(),
                            student.getGender(), formatter.format(student.getDateOfBirth()), student.getAddress(),
                            student.getHostelNo(), student.getRoomNo());
                });
        System.out.println("-".repeat(142));

    }

    public static void viewStudentByRollNo(int rollNo) {
        List<Student> students = HostelManagementSystem.readStudentsFromFile("Utils/student.ser");
        if (students.size() == 0) {
            System.out.println("No student to show.\n\nPlease add first.");
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println("-".repeat(142));
        System.out.printf("|%-10s |%-20s |%-20s |%-10s |%-15s |%-30s |%-10s |%-10s |%n",
                "Roll No", "Name", "Father's Name", "Gender", "Date of Birth", "Address", "Hostel No", "Room No");

        System.out.println("-".repeat(142));

        students.stream().filter(student -> student.getRollNo() == rollNo).forEach(student -> {
            System.out.printf("|%-10d |%-20s |%-20s |%-10s |%-15s |%-30s |%-10d |%-10d |%n",
                    student.getRollNo(), student.getName(), student.getFathersName(),
                    student.getGender(), formatter.format(student.getDateOfBirth()), student.getAddress(),
                    student.getHostelNo(), student.getRoomNo());
        });
        System.out.println("-".repeat(142));
    }

    public static void addStudent() throws ClassNotFoundException {
        try {
            List<Student> students = HostelManagementSystem.readStudentsFromFile("Utils/student.ser");
            System.out.print("Enter rollno: ");
            int rollno = input.nextInt();

            // Check if rollno already exists in Utils/student.ser file
            while (HostelManagementSystem.checkIfRollnoExists(rollno)) {
                System.out.println("Rollno already exists. Please enter a unique rollno: ");
                rollno = input.nextInt();
            }
            input.nextLine();
            System.out.print("Enter name: ");
            String name = input.nextLine();
            System.out.print("Enter father's Name: ");
            String fatherName = input.nextLine();
            System.out.print("Enter gender (M/F): ");
            String gender = input.nextLine();
            System.out.print("Enter date of birth (dd/MM/yyyy): ");
            String dobStr = input.nextLine();

            // Parse date of birth to LocalDate
            LocalDate dob = LocalDate.parse(dobStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            System.out.print("Enter address: ");
            String address = input.nextLine();
            System.out.print("Enter hostel no: ");
            int hostelNo = input.nextInt();

            // Check if hostel no available
            while (!checkIfHostelAvailable(hostelNo)) {
                System.out.println("Please enter a different hostel no: ");
                hostelNo = input.nextInt();
            }

            System.out.print("Enter room no: ");
            int roomNo = input.nextInt();

            // Check if room available
            while (!checkIfRoomAvailable(hostelNo, roomNo)) {
                System.out.println("Room is not available. Please enter a valid room no: ");
                roomNo = input.nextInt();
            }

            // Create student object and write to Utils/student.ser file
            Student student = new Student(rollno, name, fatherName, gender, dob, address, hostelNo, roomNo);
            students.add(student);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("Utils/student.ser"));
            System.out.println("Adding the student: " + student);
            objectOutputStream.writeObject(students);
            objectOutputStream.close();

            // Update currentOccupancy and availableRooms
            File roomFile = new File("Utils/hostel" + hostelNo + ".ser");
            List<Room> rooms = readRoomsFromFile(roomFile);
            if (roomFile.exists() || rooms.size() != 0) {
                for (Room room : rooms) {
                    if (room.getRoomNo() == roomNo) {
                        room.setCurrentOccupancy(room.getCurrentOccupancy() + 1);
                        if (!room.isAvailable()) {
                            System.out.println("This room is completely occupied now.");
                            List<Hostel> hostels = HostelManagementSystem.readHostelsFromFile("Utils/hostel.ser");
                            for (Hostel hostel : hostels) {
                                if (hostel.getHostelNo() == hostelNo) {
                                    hostel.setAvailableRooms(hostel.getAvailableRooms() - 1);
                                    System.out.println("Updating available rooms in Hostel...");
                                }
                            }
                            ObjectOutputStream outputStream = new ObjectOutputStream(
                                    new FileOutputStream("Utils/hostel.ser"));
                            outputStream.writeObject(hostels);
                            outputStream.close();
                        }
                        break;
                    }
                }
                ObjectOutputStream out = new ObjectOutputStream(
                        new FileOutputStream("Utils/hostel" + hostelNo + ".ser"));
                out.writeObject(rooms);
                out.close();
                System.out.println("Student details added successfully to Utils/student.ser file.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkIfHostelAvailable(int hostelNo) {
        List<Hostel> hostels = HostelManagementSystem.readHostelsFromFile("Utils/hostel.ser");
        for (Hostel hostel : hostels) {
            if (hostel.getHostelNo() == hostelNo) {
                if (hostel.getAvailableRooms() == 0) {
                    System.out.println("No room available in this hostel. All rooms are occupied.");
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkIfRoomAvailable(int hostelNo, int roomNo) {
        File roomFile = new File("Utils/hostel" + hostelNo + ".ser");
        List<Room> rooms = readRoomsFromFile(roomFile);
        for (Room room : rooms) {
            if (room.getRoomNo() == roomNo) {
                return room.isAvailable();
            }
        }
        return false;
    }

    public static void removeStudent() throws ClassNotFoundException {
        System.out.println("Enter the roll number of student whose entry you want to remove... ");
        int removeRollNo = input.nextInt();
        input.nextLine();
        if (HostelManagementSystem.checkIfRollnoExists(removeRollNo)) {
            try {
                List<Student> students = HostelManagementSystem.readStudentsFromFile("Utils/student.ser");
                boolean isRemoved = false;
                Student removedStudent = null;
                for (Student student : students) {
                    if (student.getRollNo() == removeRollNo) {
                        System.out.print("You may loose data of stuudent.\nPlease confirm (y/n)? ");
                        String answer = input.nextLine();
                        if (answer.equalsIgnoreCase("y")) {
                            System.out.println("Removing the Student....\n" + student);

                            // Update currentOccupancy and availableRooms
                            File hostelFile = new File("Utils/hostel" + student.getHostelNo() + ".ser");

                            List<Room> rooms = readRoomsFromFile(hostelFile);
                            for (Room room : rooms) {
                                if (room.getRoomNo() == student.getRoomNo()) {
                                    room.setCurrentOccupancy(room.getCurrentOccupancy() - 1);
                                    break;
                                }
                            }
                            ObjectOutputStream out = new ObjectOutputStream(
                                    new FileOutputStream("Utils/hostel" + student.getHostelNo() + ".ser"));
                            out.writeObject(rooms);
                            out.close();
                            isRemoved = true;
                            removedStudent = student;
                        }
                    }
                }
                if (isRemoved) {
                    students.remove(removedStudent);
                    ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Utils/student.ser"));
                    outputStream.writeObject(students);
                    outputStream.close();
                    System.out.println("Student details removed successfully from Utils/student.ser file.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Student with Roll No: " + removeRollNo + " doesn't exist.");
        }
    }

    public static void viewStaff() {
        int staffViewChoice;
        do {
            System.out.println("\n\nStaff View Menu");
            System.out.println("---------------");
            System.out.println("1. All Staffs details");
            System.out.println("2. Filter by Hostel No");
            System.out.println("3. Filter by Staff Type");
            System.out.println("4. Filter by Job");
            System.out.println("5. Search by Staff Id");
            System.out.println("6. Previous Menu");
            System.out.println("7. Exit from program");
            System.out.println("Enter Your choice... ");
            staffViewChoice = input.nextInt();
            input.nextLine();
            switch (staffViewChoice) {
                case 1:
                    viewAllStaffs();
                    break;
                case 2:
                    System.out.println("Enter the Hostel No: ");
                    int hostelNo = input.nextInt();
                    if (HostelManagementSystem.checkIfHostelExists(hostelNo)) {
                        viewStaffsByWorkingHostel(hostelNo);
                    } else {
                        System.out.println("Hostel " + hostelNo + " doesn't exist.");
                    }
                    break;
                case 3:
                    System.out.println("Enter Type of Staff:");
                    String type = input.nextLine();
                    viewStaffsByType(type);
                    break;
                case 4:
                    System.out.println("Enter Job:");
                    String job = input.nextLine();
                    viewStaffsByJob(job);
                    break;
                case 5:
                    System.out.println("Enter Staff ID: ");
                    int id = input.nextInt();
                    if (HostelManagementSystem.checkIfStaffIdExist(id)) {
                        viewStaffById(id);
                    } else {
                        System.out.println("Staff member with Id " + id + " doesn't exit.");
                    }
                    break;
                case 6:
                    System.out.println("Redirecting Previous Menu... ");
                    break;
                case 7:
                    HostelManagementSystem.quit();
                    break;
                default:
                    System.out.println("Wrong Choice!!!");
            }
        } while (staffViewChoice != 6);

    }

    public static void viewAllStaffs() {
        try {
            List<Staff> staffList = HostelManagementSystem.readStaffsFromFile("Utils/staff.ser");
            if (staffList.size() == 0) {
                System.out.println("No staff member in file.");
                return;
            }

            // Display the details in a table
            System.out.println("-".repeat(192));
            System.out.printf("|%-5s |%-15s |%-15s |%-10s |%-15s |%-30s |%-10s |%-15s |%-15s |%-25s |%-15s |\n",
                    "ID", "Name", "Father's Name", "Gender", "Date of Birth", "Address", "Type", "Job Title",
                    "Working Hostel", "Email", "Mobile No");
            System.out.println("-".repeat(192));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for (Staff staff : staffList) {
                System.out.printf("|%-5d |%-15s |%-15s |%-10s |%-15s |%-30s |%-10s |%-15s |%-15d |%-25s |%-15d |\n",
                        staff.getStaffId(), staff.getStaffName(), staff.getStaffFatherName(), staff.getStaffGender(),
                        staff.getDateOfBirth().format(formatter), staff.getAddress(), staff.getStaffType(),
                        staff.getStaffJob(), staff.getWorkingHostel(), staff.getStaffEmail(), staff.getStaffMobNo());
            }
            System.out.println("-".repeat(192));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void viewStaffsByWorkingHostel(int hostelNo) {
        try {
            List<Staff> staffs = HostelManagementSystem.readStaffsFromFile("Utils/staff.ser");
            if (staffs.size() == 0) {
                System.out.println("No staff member in file.");
                return;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            // Display the details in a table
            System.out.println("-".repeat(192));
            System.out.printf("|%-5s |%-15s |%-15s |%-10s |%-15s |%-30s |%-10s |%-15s |%-15s |%-25s |%-15s |\n",
                    "ID", "Name", "Father's Name", "Gender", "Date of Birth", "Address", "Type", "Job Title",
                    "Working Hostel", "Email", "Mobile No");
            System.out.println("-".repeat(192));

            staffs.stream().filter(staff -> staff.getWorkingHostel() == hostelNo).forEach(staff -> {
                System.out.printf("|%-5d |%-15s |%-15s |%-10s |%-15s |%-30s |%-10s |%-15s |%-15d |%-25s |%-15d |\n",
                        staff.getStaffId(), staff.getStaffName(), staff.getStaffFatherName(), staff.getStaffGender(),
                        staff.getDateOfBirth().format(formatter), staff.getAddress(), staff.getStaffType(),
                        staff.getStaffJob(), staff.getWorkingHostel(), staff.getStaffEmail(), staff.getStaffMobNo());
            });
            System.out.println("-".repeat(192));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void viewStaffsByType(String type) {
        try {
            List<Staff> staffs = HostelManagementSystem.readStaffsFromFile("Utils/staff.ser");
            if (staffs.size() == 0) {
                System.out.println("No staff member in file.");
                return;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            // Display the details in a table
            System.out.println("-".repeat(192));
            System.out.printf("|%-5s |%-15s |%-15s |%-10s |%-15s |%-30s |%-10s |%-15s |%-15s |%-25s |%-15s |\n",
                    "ID", "Name", "Father's Name", "Gender", "Date of Birth", "Address", "Type", "Job Title",
                    "Working Hostel", "Email", "Mobile No");
            System.out.println("-".repeat(192));

            staffs.stream().filter(staff -> staff.getStaffType() == type).forEach(staff -> {
                System.out.printf("|%-5d |%-15s |%-15s |%-10s |%-15s |%-30s |%-10s |%-15s |%-15d |%-25s |%-15d |\n",
                        staff.getStaffId(), staff.getStaffName(), staff.getStaffFatherName(), staff.getStaffGender(),
                        staff.getDateOfBirth().format(formatter), staff.getAddress(), staff.getStaffType(),
                        staff.getStaffJob(), staff.getWorkingHostel(), staff.getStaffEmail(), staff.getStaffMobNo());
            });
            System.out.println("-".repeat(192));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void viewStaffsByJob(String job) {
        try {
            List<Staff> staffs = HostelManagementSystem.readStaffsFromFile("Utils/staff.ser");
            if (staffs.size() == 0) {
                System.out.println("No staff member in file.");
                return;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            // Display the details in a table
            System.out.println("-".repeat(192));
            System.out.printf("|%-5s |%-15s |%-15s |%-10s |%-15s |%-30s |%-10s |%-15s |%-15s |%-25s |%-15s |\n",
                    "ID", "Name", "Father's Name", "Gender", "Date of Birth", "Address", "Type", "Job Title",
                    "Working Hostel", "Email", "Mobile No");
            System.out.println("-".repeat(192));

            staffs.stream().filter(staff -> staff.getStaffJob() == job).forEach(staff -> {
                System.out.printf("|%-5d |%-15s |%-15s |%-10s |%-15s |%-30s |%-10s |%-15s |%-15d |%-25s |%-15d |\n",
                        staff.getStaffId(), staff.getStaffName(), staff.getStaffFatherName(), staff.getStaffGender(),
                        staff.getDateOfBirth().format(formatter), staff.getAddress(), staff.getStaffType(),
                        staff.getStaffJob(), staff.getWorkingHostel(), staff.getStaffEmail(), staff.getStaffMobNo());
            });
            System.out.println("-".repeat(192));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void viewStaffById(int id) {
        try {
            List<Staff> staffs = HostelManagementSystem.readStaffsFromFile("Utils/staff.ser");
            if (staffs.size() == 0) {
                System.out.println("No staff member in file.");
                return;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Display the details in a table
            System.out.println("-".repeat(192));
            System.out.printf("|%-5s |%-15s |%-15s |%-10s |%-15s |%-30s |%-10s |%-15s |%-15s |%-25s |%-15s |\n",
                    "ID", "Name", "Father's Name", "Gender", "Date of Birth", "Address", "Type", "Job Title",
                    "Working Hostel", "Email", "Mobile No");
            System.out.println("-".repeat(192));

            staffs.stream().filter(staff -> staff.getStaffId() == id).forEach(staff -> {
                System.out.printf("|%-5d |%-15s |%-15s |%-10s |%-15s |%-30s |%-10s |%-15s |%-15d |%-25s |%-15d |\n",
                        staff.getStaffId(), staff.getStaffName(), staff.getStaffFatherName(), staff.getStaffGender(),
                        staff.getDateOfBirth().format(formatter), staff.getAddress(), staff.getStaffType(),
                        staff.getStaffJob(), staff.getWorkingHostel(), staff.getStaffEmail(), staff.getStaffMobNo());
            });
            System.out.println("-".repeat(192));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addStaff() throws FileNotFoundException, IOException {
        List<Staff> staffs = HostelManagementSystem.readStaffsFromFile("Utils/staff.ser");
        System.out.println("\nEnter Staff ID: ");
        int id = input.nextInt();
        while (HostelManagementSystem.checkIfStaffIdExist(id)) {
            System.out.println("Entered staff ID is already used!!! \nPlease enter a different ID: ");
            id = input.nextInt();
        }
        input.nextLine();
        System.out.println("Enter Name: ");
        String name = input.nextLine();
        System.out.println("Enter Father Name: ");
        String fname = input.nextLine();
        System.out.print("Enter Gender (M/F): ");
        String gender = input.nextLine();
        System.out.print("Enter Date of Birth (dd/MM/yyyy): ");
        String dobStr = input.nextLine();

        // Parse date of birth to LocalDate
        LocalDate dob = LocalDate.parse(dobStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        System.out.print("Enter address: ");
        String address = input.nextLine();

        System.out.println("Enter Type of Staff: ");
        String type = input.nextLine();
        System.out.println("Enter the Job: ");
        String job = input.nextLine();
        System.out.println("Enter Working Hostel No: ");
        int workingHostel = input.nextInt();
        while (!HostelManagementSystem.checkIfHostelExists(workingHostel)) {
            System.out.println("Hostel" + workingHostel + " does not exist.\nPlease enter a valid hostel No: ");
            workingHostel = input.nextInt();
        }
        input.nextLine();
        System.out.println("Enter Email: ");
        String email = input.nextLine();
        System.out.println("Enter Mobile Number: ");
        long mobNo = input.nextLong();

        // Create staff object and write to Utils/staff.ser file
        Staff staff = new Staff(id, name, fname, gender, dob, address, type, job, workingHostel, email, mobNo);
        staffs.add(staff);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("Utils/staff.ser"));
        System.out.println("Adding the staff member: " + staff);
        objectOutputStream.writeObject(staffs);
        objectOutputStream.close();

        // Update No of Staff for Hostel in Utils/hostel.ser file
        List<Hostel> hostels = HostelManagementSystem.readHostelsFromFile("Utils/hostel.ser");
        for (Hostel hostel : hostels) {
            if (hostel.getHostelNo() == workingHostel) {
                hostel.setNumberOfStaff(hostel.getNumberOfStaff() + 1);
            }
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Utils/hostel.ser"))) {
            out.writeObject(hostels);
            System.out.println("Staff Member added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeStaff() throws ClassNotFoundException {
        System.out.println("\nEnter the ID of staff whose entry you want to remove... ");
        int removeStaffId = input.nextInt();
        input.nextLine();
        if (HostelManagementSystem.checkIfStaffIdExist(removeStaffId)) {
            try {
                List<Staff> staffs = HostelManagementSystem.readStaffsFromFile("Utils/staff.ser");
                boolean isRemoved = false;
                Staff removedStaff = null;

                for (Staff staff : staffs) {
                    if (staff.getStaffId() == removeStaffId) {
                        System.out.print("You may loose data of staff.\nPlease confirm (y/n)? ");
                        String answer = input.nextLine();
                        if (answer.equalsIgnoreCase("y")) {
                            System.out.println("Removing the Staff....\n" + staff);

                            // Update No of Staff for Hostel in Utils/hostel.ser file
                            List<Hostel> hostels = HostelManagementSystem.readHostelsFromFile("Utils/hostel.ser");
                            for (Hostel hostel : hostels) {
                                if (hostel.getHostelNo() == staff.getWorkingHostel()) {
                                    hostel.setNumberOfStaff(hostel.getNumberOfStaff() - 1);
                                    removedStaff = staff;
                                    isRemoved = true;
                                }
                            }
                            try (ObjectOutputStream out = new ObjectOutputStream(
                                    new FileOutputStream("Utils/hostel.ser"))) {
                                out.writeObject(hostels);
                            } catch (Exception e) {
                                isRemoved = false;
                            }
                        }
                    }
                }
                if (isRemoved) {
                    staffs.remove(removedStaff);
                    ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Utils/student.ser"));
                    outputStream.writeObject(staffs);
                    outputStream.close();
                    System.out.println("Staff details removed successfully.");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Staff member with Staff Id " + removeStaffId + " doesn't exist.");
        }
    }

}
