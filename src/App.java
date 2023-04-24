import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class App {
    static Scanner input = new Scanner(System.in);
    private static final String DELIMITER = ",";

    public static void main(String[] args) throws Exception {
        int choice;
        do {
            System.out.println("\n\n\nHOSTEL MANAGEMENT SYSTEM");
            System.out.println("------------------------");
            System.out.println("1. Hostel details");
            System.out.println("2. Admistration portal");
            System.out.println("3. Student portal");
            System.out.println("4. Staff portal");
            System.out.println("5. About");
            System.out.println("6. Exit");
            System.out.println("Enter your choice... ");
            choice = input.nextInt();
            switch (choice) {
                case 1:
                    int hostelChoise;
                    do {
                        System.out.println("\n\nWelcome to hostel datails portal.");
                        System.out.println("---------------------------------");
                        System.out.println("1. All hostels details ");
                        System.out.println("2. Specific hostel detail");
                        System.out.println("3. Previous menu");
                        System.out.println("4. Exit from program");
                        System.out.println("Enter your choice... ");
                        hostelChoise = input.nextInt();
                        switch (hostelChoise) {
                            case 1:
                                printHostelsInTable();
                                break;
                            case 2:
                                hostelDetails();
                                break;
                            case 3:
                                System.out.println("Redirected to Main Menu....");
                                break;
                            case 4:
                                quit();
                                break;
                            default:
                                System.out.println("Wrong choice!!! \nPlease enter again...");
                        }
                    } while (hostelChoise != 3);
                    break;
                case 2:
                    // if (authentication(2)) {
                    admistrationPortal();
                    // }
                    break;
                case 3:
                    if (authentication(3)) {
                        studentPortal();
                    }
                    break;
                case 4:
                    if (authentication(4)) {
                        staffPortal();
                    }
                    break;
                case 5:
                    about();
                    break;
                case 6:
                    quit();
                    break;
                default:
                    System.out.println("Wrong choice!!!\nPlease enter again... ");
            }
        } while (choice != 6);
    }

    static void hostelDetails() throws ClassNotFoundException, IOException {
        // hostel Menu
        ArrayList<Hostel> hostels = readHostelsFromFile("hostel.ser");
        if (hostels.size() == 0) {
            System.out.println("There is no Hostel Added. Please contact Admistration.");
            return;
        }
        String inputStr;
        do {
            System.out.println("\n\nAvailable hostels:");
            printHostelMenu(hostels);
            input.nextLine();
            System.out.println("Enter the hostel number (or any non-numeric character to exit)... ");
            inputStr = input.nextLine();
            if (inputStr.matches("\\d+") && Integer.parseInt(inputStr) >= 0) {
                int specialhostelchoice = Integer.parseInt(inputStr);
                showSpecialHostelDetails(specialhostelchoice);
            } else {
                System.out.println("Redirected to previous menu....\n\n");
            }
        } while (inputStr.matches("\\d+") && Integer.parseInt(inputStr) >= 0);

    }

    public static boolean showSpecialHostelDetails(int hostelNumber) throws IOException, ClassNotFoundException {
        String fileName = "hostel" + hostelNumber + ".ser";
        File hostelFile = new File(fileName);

        if (hostelFile.exists()) {
            List<Hostel> hostels = readHostelsFromFile("hostel.ser");
            if (hostels.size() == 0) {
                System.out.println("No hostel entry found with " + hostelNumber + " hostel number.");
            }
            for (Hostel hostel : hostels) {
                if (hostel.getHostelNo() == hostelNumber) {
                    System.out.println("-".repeat(164));
                    System.out.printf("|%-10s|%-20s|%-30s|%-15s|%-15s|%-15s|%-20s|%-30s|\n", "Hostel No",
                            "Hostel Name",
                            "Hostel Address", "No of Rooms", "Available Rooms", "No of Staffs", "Warden Name",
                            "Wardan Email");
                    System.out.println("-".repeat(164));
                    String[] row = hostel.toTableRow();
                    System.out.printf("|%-10s|%-20s|%-30s|%-15s|%-15s|%-15s|%-20s|%-30s|\n", row[0], row[1], row[2],
                            row[3],
                            row[4], row[5], row[6], row[7]);
                    System.out.println("-".repeat(164));
                    return true;
                }
            }
            return true;
        } else {
            System.out.println("Hostel " + hostelNumber + "does not exist.");
            System.out.println("You have entered a wrong choice!!!");
            return false;
        }
    }

    public static ArrayList<Hostel> readHostelsFromFile(String filename) {
        ArrayList<Hostel> hostels = new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = in.readObject();
            if (obj instanceof ArrayList) {
                hostels = (ArrayList<Hostel>) obj;
            } else if (obj instanceof Hostel) {
                hostels.add((Hostel) obj);
            }
        } catch (FileNotFoundException | EOFException e) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return hostels;
    }

    public static void printHostelMenu(ArrayList<Hostel> hostels) {
        for (Hostel hostel : hostels) {
            System.out.printf("%d. %s\n", hostel.getHostelNo(), hostel.getHostelName());
        }
    }

    public static boolean printHostelsInTable() {
        ArrayList<Hostel> hostels = readHostelsFromFile("hostel.ser");
        if (hostels.size() == 0) {
            System.out.println("There is no Hostel Added.");
            return false;
        }
        System.out.println("-".repeat(164));
        System.out.printf("|%-10s|%-20s|%-30s|%-15s|%-15s|%-15s|%-20s|%-30s|\n", "Hostel No", "Hostel Name",
                "Hostel Address",
                "No of Rooms", "Available Rooms", "No of Staffs", "Warden Name", "Wardan Email");
        System.out.println("-".repeat(164));

        for (Hostel hostel : hostels) {
            String[] row = hostel.toTableRow();
            System.out.printf("|%-10s|%-20s|%-30s|%-15s|%-15s|%-15s|%-20s|%-30s|\n", row[0], row[1], row[2], row[3],
                    row[4], row[5], row[6], row[7]);
        }
        System.out.println("-".repeat(164));
        return true;
    }

    static void admistrationPortal() throws Exception {
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
                    if (printHostelsInTable()) {
                        String inputStr;
                        do {
                            System.out.println("Enter the hostel number (or any non-numeric character to exit)... ");
                            inputStr = input.nextLine();
                            if (inputStr.matches("\\d+") && Integer.parseInt(inputStr) >= 0) {
                                int specialhostelchoice = Integer.parseInt(inputStr);
                                if (checkIfHostelExists(specialhostelchoice)) {
                                    if (showSpecialHostelDetails(specialhostelchoice)) {
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
                    String fileName = "hostel" + roomAddHostelNo + ".ser";
                    File roomAddHostelFile = new File(fileName);
                    if (roomAddHostelFile.exists() && checkIfHostelExists(roomAddHostelNo)) {
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
                    String hostelNameFile = "hostel" + roomRemoveHostelNo + ".ser";
                    File roomRemoveHostelFile = new File(hostelNameFile);
                    if (roomRemoveHostelFile.exists() && checkIfHostelExists(roomRemoveHostelNo)) {
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
                    quit();
                    break;
                default:
                    System.out.println("Wrong Choice");

            }
        } while (adminChoice != 14);

    }

    public static void hostelRoomsDetails(int hostelNo) throws FileNotFoundException, IOException {
        String fileName = "hostel" + hostelNo + ".ser";
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
        List<Student> students = readStudentsFromFile("student.ser");
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
            FileWriter writer = new FileWriter("checkInOut.csv", true);
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
        String filePath = "checkInOut.csv";
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
            List<Student> students = readStudentsFromFile("student.ser");
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
        List<Hostel> hostels = readHostelsFromFile("hostel.ser");
        // get hostel details from user
        System.out.println("\nEnter Hostel details to add it.");
        System.out.println("Enter hostel number: ");
        int hostelNo = input.nextInt();

        // Check if the hostel number already exists in the file
        File hostelFile = new File("hostel.ser");
        while (checkIfHostelExists(hostelNo)) {
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
        File newHostelFile = new File("hostel" + hostelNo + ".ser");
        if (newHostelFile.createNewFile()) {
            System.out.println("\nHostel added successfully.");
        } else {
            System.out.println("Error occured in adding hostel.");
        }
    }

    public static boolean checkIfHostelExists(int hostelNo) {
        List<Hostel> hostels = readHostelsFromFile("hostel.ser");
        for (Hostel hostel : hostels) {
            if (hostel.getHostelNo() == hostelNo) {
                return true;
            }
        }
        return false;
    }

    public static void removeHostel() {
        System.out.println("\nEnter Hostel Number to remove it... ");
        int removeHostelNo = input.nextInt();
        input.nextLine();
        String fileName = "hostel" + removeHostelNo + ".ser";
        File hostelFile = new File(fileName);

        if (hostelFile.exists() && checkIfHostelExists(removeHostelNo)) {
            // Hostel file exists
            boolean isRemoved = false;
            Hostel removedHostel = null;
            try {
                List<Hostel> hostels = readHostelsFromFile("hostel.ser");
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
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("hostel.ser"));
                outputStream.writeObject(hostels);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else

        {
            System.out.println("Hostel" + removeHostelNo + " does not exist.");
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
        List<Hostel> hostels = readHostelsFromFile("hostel.ser");

        for (Hostel hostel : hostels) {
            if (hostel.getHostelNo() == hostelNo) {
                hostel.setNumberOfRooms(hostel.getNumberOfRooms() + 1);
                hostel.setAvailableRooms(hostel.getAvailableRooms() + 1);
            }
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hostel.ser"))) {
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
                    List<Hostel> hostels = readHostelsFromFile("hostel.ser");
                    ObjectOutputStream hostelOutputStream = new ObjectOutputStream(new FileOutputStream("hostel.ser"));
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
                    if (checkIfHostelExists(hostelnum)) {
                        viewStudentsByHostel(hostelnum);
                    } else {
                        System.out.println("Not a valid hostel no.");
                    }
                    break;
                case 3:
                    System.out.println("Enter Hostel No: ");
                    int hostelNum = input.nextInt();
                    String fileName = "hostel" + hostelNum + ".ser";
                    File hostelFile = new File(fileName);
                    if (checkIfHostelExists(hostelNum) && hostelFile.exists()) {
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
                    if (checkIfRollnoExists(rollNo)) {
                        viewStudentByRollNo(rollNo);
                    }
                    break;
                case 5:
                    System.out.println("Redirecting Previous Menu");
                    break;
                case 6:
                    quit();
                    break;
                default:
                    System.out.println("Wrong Choice.");
            }
        } while (studentViewChoice != 5);

    }

    public static void viewAllStudents() {
        List<Student> students = readStudentsFromFile("student.ser");
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
        List<Student> students = readStudentsFromFile("student.ser");
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
        List<Student> students = readStudentsFromFile("student.ser");
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
        List<Student> students = readStudentsFromFile("student.ser");
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
            List<Student> students = readStudentsFromFile("student.ser");
            System.out.print("Enter rollno: ");
            int rollno = input.nextInt();

            // Check if rollno already exists in student.ser file
            while (checkIfRollnoExists(rollno)) {
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

            // Create student object and write to student.ser file
            Student student = new Student(rollno, name, fatherName, gender, dob, address, hostelNo, roomNo);
            students.add(student);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("student.ser"));
            System.out.println("Adding the student: " + student);
            objectOutputStream.writeObject(students);
            objectOutputStream.close();

            // Update currentOccupancy and availableRooms
            File roomFile = new File("hostel" + hostelNo + ".ser");
            List<Room> rooms = readRoomsFromFile(roomFile);
            if (roomFile.exists() || rooms.size() != 0) {
                for (Room room : rooms) {
                    if (room.getRoomNo() == roomNo) {
                        room.setCurrentOccupancy(room.getCurrentOccupancy() + 1);
                        if (!room.isAvailable()) {
                            System.out.println("This room is completely occupied now.");
                            List<Hostel> hostels = readHostelsFromFile("hostel.ser");
                            for (Hostel hostel : hostels) {
                                if (hostel.getHostelNo() == hostelNo) {
                                    hostel.setAvailableRooms(hostel.getAvailableRooms() - 1);
                                    System.out.println("Updating available rooms in Hostel...");
                                }
                            }
                            ObjectOutputStream outputStream = new ObjectOutputStream(
                                    new FileOutputStream("hostel.ser"));
                            outputStream.writeObject(hostels);
                            outputStream.close();
                        }
                        break;
                    }
                }
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hostel" + hostelNo + ".ser"));
                out.writeObject(rooms);
                out.close();
                System.out.println("Student details added successfully to student.ser file.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkIfRollnoExists(int rollno) {
        List<Student> students = readStudentsFromFile("student.ser");
        for (Student room : students) {
            if (room.getRollNo() == rollno) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkIfHostelAvailable(int hostelNo) {
        List<Hostel> hostels = readHostelsFromFile("hostel.ser");
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
        File roomFile = new File("hostel" + hostelNo + ".ser");
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
        if (checkIfRollnoExists(removeRollNo)) {
            try {
                List<Student> students = readStudentsFromFile("student.ser");
                boolean isRemoved = false;
                Student removedStudent = null;
                for (Student student : students) {
                    if (student.getRollNo() == removeRollNo) {
                        System.out.print("You may loose data of stuudent.\nPlease confirm (y/n)? ");
                        String answer = input.nextLine();
                        if (answer.equalsIgnoreCase("y")) {
                            System.out.println("Removing the Student....\n" + student);

                            // Update currentOccupancy and availableRooms
                            File hostelFile = new File("hostel" + student.getHostelNo() + ".ser");

                            List<Room> rooms = readRoomsFromFile(hostelFile);
                            for (Room room : rooms) {
                                if (room.getRoomNo() == student.getRoomNo()) {
                                    room.setCurrentOccupancy(room.getCurrentOccupancy() - 1);
                                    break;
                                }
                            }
                            ObjectOutputStream out = new ObjectOutputStream(
                                    new FileOutputStream("hostel" + student.getHostelNo() + ".ser"));
                            out.writeObject(rooms);
                            out.close();
                            isRemoved = true;
                            removedStudent = student;
                        }
                    }
                }
                if (isRemoved) {
                    students.remove(removedStudent);
                    ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("student.ser"));
                    outputStream.writeObject(students);
                    outputStream.close();
                    System.out.println("Student details removed successfully from student.ser file.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Student with Roll No: " + removeRollNo + " doesn't exist.");
        }
    }

    public static List<Student> readStudentsFromFile(String filename) {
        ArrayList<Student> students = new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = in.readObject();
            if (obj instanceof ArrayList) {
                students = (ArrayList<Student>) obj;
            } else if (obj instanceof Student) {
                students.add((Student) obj);
            }
        } catch (FileNotFoundException | EOFException e) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return students;
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
                    if (checkIfHostelExists(hostelNo)) {
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
                    if (checkIfStaffIdExist(id)) {
                        viewStaffById(id);
                    } else {
                        System.out.println("Staff member with Id " + id + " doesn't exit.");
                    }
                    break;
                case 6:
                    System.out.println("Redirecting Previous Menu... ");
                    break;
                case 7:
                    quit();
                    break;
                default:
                    System.out.println("Wrong Choice!!!");
            }
        } while (staffViewChoice != 6);

    }

    public static void viewAllStaffs() {
        try {
            List<Staff> staffList = readStaffsFromFile("staff.ser");
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
            List<Staff> staffs = readStaffsFromFile("staff.ser");
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
            List<Staff> staffs = readStaffsFromFile("staff.ser");
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
            List<Staff> staffs = readStaffsFromFile("staff.ser");
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
            List<Staff> staffs = readStaffsFromFile("staff.ser");
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
        List<Staff> staffs = readStaffsFromFile("staff.ser");
        System.out.println("\nEnter Staff ID: ");
        int id = input.nextInt();
        while (checkIfStaffIdExist(id)) {
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
        while (!checkIfHostelExists(workingHostel)) {
            System.out.println("Hostel" + workingHostel + " does not exist.\nPlease enter a valid hostel No: ");
            workingHostel = input.nextInt();
        }
        input.nextLine();
        System.out.println("Enter Email: ");
        String email = input.nextLine();
        System.out.println("Enter Mobile Number: ");
        long mobNo = input.nextLong();

        // Create staff object and write to staff.ser file
        Staff staff = new Staff(id, name, fname, gender, dob, address, type, job, workingHostel, email, mobNo);
        staffs.add(staff);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("staff.ser"));
        System.out.println("Adding the staff member: " + staff);
        objectOutputStream.writeObject(staffs);
        objectOutputStream.close();

        // Update No of Staff for Hostel in hostel.ser file
        List<Hostel> hostels = readHostelsFromFile("hostel.ser");
        for (Hostel hostel : hostels) {
            if (hostel.getHostelNo() == workingHostel) {
                hostel.setNumberOfStaff(hostel.getNumberOfStaff() + 1);
            }
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hostel.ser"))) {
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
        if (checkIfStaffIdExist(removeStaffId)) {
            try {
                List<Staff> staffs = readStaffsFromFile("staff.ser");
                boolean isRemoved = false;
                Staff removedStaff = null;

                for (Staff staff : staffs) {
                    if (staff.getStaffId() == removeStaffId) {
                        System.out.print("You may loose data of staff.\nPlease confirm (y/n)? ");
                        String answer = input.nextLine();
                        if (answer.equalsIgnoreCase("y")) {
                            System.out.println("Removing the Staff....\n" + staff);

                            // Update No of Staff for Hostel in hostel.ser file
                            List<Hostel> hostels = readHostelsFromFile("hostel.ser");
                            for (Hostel hostel : hostels) {
                                if (hostel.getHostelNo() == staff.getWorkingHostel()) {
                                    hostel.setNumberOfStaff(hostel.getNumberOfStaff() - 1);
                                    removedStaff = staff;
                                    isRemoved = true;
                                }
                            }
                            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hostel.ser"))) {
                                out.writeObject(hostels);
                            } catch (Exception e) {
                                isRemoved = false;
                            }
                        }
                    }
                }
                if (isRemoved) {
                    staffs.remove(removedStaff);
                    ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("student.ser"));
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

    private static List<Staff> readStaffsFromFile(String filename) {

        ArrayList<Staff> staffs = new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = in.readObject();
            if (obj instanceof ArrayList) {
                staffs = (ArrayList<Staff>) obj;
            } else if (obj instanceof Staff) {
                staffs.add((Staff) obj);
            }
        } catch (FileNotFoundException | EOFException e) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return staffs;
    }

    private static boolean checkIfStaffIdExist(int id) {
        List<Staff> staffs = readStaffsFromFile("staff.ser");
        for (Staff staff : staffs) {
            if (staff.getStaffId() == id) {
                return true;
            }
        }
        return false;
    }

    static void studentPortal() throws IOException {
        int studentchoice;
        do {
            System.out.println("\n\nWelcome to Student Portal");
            System.out.println("-------------------------");
            System.out.println("1. View Hostels");
            System.out.println("2. View available rooms");
            System.out.println("3. Requist for change the room");
            System.out.println("4. Requist for check in or check out");
            System.out.println("5. Update your details");
            System.out.println("6. Reqest for Staff");
            System.out.println("7. Contact wardan");
            System.out.println("8. Main Menu");
            System.out.println("9. Exit from Program");
            System.out.println("Enter your choice... ");
            studentchoice = input.nextInt();
            switch (studentchoice) {
                case 1:
                    printHostelsInTable();
                    break;
                case 2:
                    if (printHostelsInTable()) {
                        System.out.println("Enter the hostel number (or any non-numeric character to exit)... ");
                        String inputStr = input.nextLine();
                        if (inputStr.matches("\\d+") && Integer.parseInt(inputStr) >= 0) {
                            int specialhostelchoice = Integer.parseInt(inputStr);
                            viewAvailableRooms(specialhostelchoice);
                        } else {
                            System.out.println("Redirected to previous menu....");
                        }
                    } else {
                        System.out.println("Please contact admistrator.");
                    }
                    break;
                case 3:
                    System.out.println("Please contact your warder/admin for this.");
                    break;
                case 4:
                    System.out.println("Please contact your warder/admin for this.");
                    break;
                case 5:
                    System.out.println("Please contact your warder/admin for this.");
                    break;
                case 6:
                    studentRequestToStaff();
                    break;
                case 7:
                    contactWarden();
                    break;
                case 8:
                    System.out.println("Redirecting to Main Menu.... \n");
                    break;
                case 9:
                    quit();
                    break;
                default:
                    System.out.println("Wrong choice!!!\n\n");
            }
        } while (studentchoice != 8);
    }

    public static void viewAvailableRooms(int hostelNo) {
        File f = new File("hostel" + hostelNo + ".ser");
        if (f.exists()) {
            List<Room> rooms = readRoomsFromFile(f);

            // Filter the list of rooms to only include those that are available
            System.out.println("Available rooms:");
            rooms.stream().filter(Room::isAvailable).forEach(System.out::println);

        } else {
            System.out.println("Hostel" + hostelNo + " doesn't exist.");
        }
    }

    public static void studentRequestToStaff() throws IOException {
        int studentRequestChoice;
        do {
            System.out.println("\n\nStudent Request Portal");
            System.out.println("----------------------");
            System.out.println("1. Make a new Request");
            System.out.println("2. Request Status");
            System.out.println("3. Contact Staff");
            System.out.println("4. Previous Menu");
            System.out.println("5. Exit from program");
            System.out.println("Enter your choice...");
            studentRequestChoice = input.nextInt();
            switch (studentRequestChoice) {
                case 1:
                    // Get the request details from the student
                    System.out.print("Enter your hostel number: ");
                    int hostelNo = input.nextInt();
                    System.out.print("Enter your room number: ");
                    int roomNo = input.nextInt();
                    input.nextLine();
                    System.out.print("Enter the type of staff you need (e.g. security, cleaner): ");
                    String type = input.nextLine();
                    System.out.print("Enter the staff's specific job (if known, otherwise leave blank): ");
                    String job = input.nextLine();
                    System.out.print("Enter a description of the request: ");
                    String description = input.nextLine();
                    LocalDateTime dateTime = LocalDateTime.now();

                    // Make the request and store the details in the file
                    makeRequest(hostelNo, roomNo, type, job, description, dateTime);
                    break;
                case 2:
                    System.out.println("Enter the Request ID(Whose status you want to know): ");
                    int requestId = input.nextInt();
                    input.nextLine();
                    showRequestStatus(requestId);
                    break;
                case 3:
                    viewStaff();
                    break;
                case 4:
                    System.out.println("Redirecting to Previous Menu...\n\n");
                    break;
                case 5:
                    quit();
                    break;
                default:
                    System.out.println("Wrong Choice!!!");
            }
        } while (studentRequestChoice != 4);
    }

    public static void makeRequest(int hostelNo, int roomNo, String type, String job, String description,
            LocalDateTime dateTime) throws IOException {
        int requestId;
        File requestFile = new File("studentRequestToStaff");
        if (!requestFile.exists()) {
            requestFile.createNewFile();
            requestId = 1;
        } else {
            BufferedReader reader = new BufferedReader(new FileReader(requestFile));
            String lastLine = "";
            String currentLine = reader.readLine();
            while (currentLine != null) {
                lastLine = currentLine;
                currentLine = reader.readLine();
            }
            reader.close();

            String[] fields = lastLine.split(",");
            requestId = Integer.parseInt(fields[0]) + 1;
        }

        BufferedWriter out = new BufferedWriter(new FileWriter(requestFile, true));
        out.write(String.format("%d,%d,%d,%s,%s,%s,%s\n", requestId, hostelNo, roomNo, type, job, description,
                dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
        out.close();

        System.out.printf("Your request has been submitted with ID %d at %s\n", requestId,
                dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
    }

    public static void showRequestStatus(int requestID) {
        String line;
        String[] fields;
        boolean requestFound = false;
        try (BufferedReader br = new BufferedReader(new FileReader("requestDoneByStaff"))) {
            while ((line = br.readLine()) != null) {
                fields = line.split(",");
                if (fields[0].equals(String.valueOf(requestID))) {
                    System.out.println("Request ID: " + fields[0]);
                    System.out.println("Description: " + fields[7]);
                    System.out.println("Status: " + fields[2]);
                    System.out.println("Remark by Staff: " + fields[1]);
                    System.out.println("Date and Time: " + fields[6]);
                    System.out.println("Request Handled By:");
                    System.out.println("Staff ID: " + fields[3]);
                    System.out.println("Staff Type: " + fields[4]);
                    System.out.println("Staff Job: " + fields[5]);
                    if (fields[2].equals("can't be done")) {
                        System.out
                                .println("Request can't be handle. You can contact your admin/warden regarding this.");
                    }
                    requestFound = true;
                }
            }
            if (!requestFound) {
                // Check if request exists in studentRequestToStaff.csv
                try (BufferedReader br2 = new BufferedReader(new FileReader("studentRequestToStaff.csv"))) {
                    while ((line = br2.readLine()) != null) {
                        fields = line.split(",");
                        if (fields[0].equals(String.valueOf(requestID))) {
                            System.out.println("Request ID: " + fields[0]);
                            System.out.println("Request Status: Pending");
                            System.out.println("Request Description: " + fields[5]);
                            System.out.println("Request Date and Time: " + fields[6]);
                            System.out.println("Note: Request is not yet handled by staff.");
                            requestFound = true;
                            break;
                        }
                    }
                    if (!requestFound) {
                        System.out.println("Request with ID " + requestID + " not found.");
                        System.out
                                .println("Have you forget Request ID.\nDo you want to search for a request ID? (Y/N)");
                        if (input.nextLine().equalsIgnoreCase("y")) {
                            searchRequestId();
                        }

                    }
                } catch (IOException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public static void searchRequestId() {
        System.out.println("Please enter your hostel number:");
        int hostelNo = input.nextInt();
        System.out.println("Please enter your room number:");
        int roomNo = input.nextInt();
        String requestFile = "studentRequestToStaff";
        BufferedReader br = null;
        String line = "";
        boolean requestFound = false;

        try {
            br = new BufferedReader(new FileReader(requestFile));
            while ((line = br.readLine()) != null) {
                String[] request = line.split(",");
                int requestId = Integer.parseInt(request[0]);
                int hNo = Integer.parseInt(request[1]);
                int rNo = Integer.parseInt(request[2]);
                if (hNo == hostelNo && rNo == roomNo) {
                    System.out.println("\nRequest ID: " + requestId);
                    System.out.println("Discreption: " + request[5]);
                    requestFound = true;
                }
            }
            if (!requestFound) {
                System.out.println("No requests found for Hostel No " + hostelNo + " and Room No " + roomNo);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File " + requestFile + " not found.");
        } catch (IOException e) {
            System.out.println("Error reading " + requestFile + ".");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println("Error closing " + requestFile + ".");
                }
            }
        }
    }

    static void staffPortal() throws IOException {
        int staffchoice;
        do {
            System.out.println("\n\nWelcome to Staff Portal");
            System.out.println("-----------------------");
            System.out.println("1. View Staff Members");
            System.out.println("2. Students requests");
            System.out.println("3. Request for Leave");
            System.out.println("4. Update your Details");
            System.out.println("5. Request for change the Working Hostel");
            System.out.println("6. Contact Warden/Admin");
            System.out.println("7. Previous Menu");
            System.out.println("8. Exit from the Program");
            System.out.println("Enter your choice... ");
            staffchoice = input.nextInt();
            switch (staffchoice) {
                case 1:
                    viewStaff();
                    break;
                case 2:
                    System.out.println("Enter your ID: ");
                    int id = input.nextInt();
                    System.out.print("Enter the hostel number where you work: ");
                    int workingHostel = input.nextInt();
                    System.out.print("Enter your staff type (e.g. security, cleaner, etc.): ");
                    input.nextLine();
                    String staffType = input.nextLine();
                    System.out.print("Enter your job: ");
                    String job = input.nextLine();

                    // Process the requests for the working hostel and matching type and job
                    handleRequests(id, workingHostel, staffType, job);
                    break;
                case 3:
                    System.out.println("Please contact your Admin for this");
                    break;
                case 4:
                    // update details
                    System.out.println("Please Contact Your Admin for this");
                    break;
                case 5:
                    System.out.println("Please Contact Your Admin for this");
                    break;
                case 6:
                    // contact warden
                    contactWarden();
                    break;
                case 7:
                    System.out.println("Redirecting to Main Menu... \n");
                    break;
                case 8:
                    quit();
                    break;
                default:
                    System.out.println("Wrong Choice!!!");
            }
        } while (staffchoice != 7);

    }

    public static void handleRequests(int staffId, int workingHostel, String staffType, String staffJob)
            throws IOException {
        // Read the requests from the file
        try {
            BufferedReader in = new BufferedReader(new FileReader("studentRequestToStaff"));
            String line;
            while ((line = in.readLine()) != null) {
                String[] data = line.split(",");
                int requestId = Integer.parseInt(data[0]);
                int hostelNo = Integer.parseInt(data[1]);
                int roomNo = Integer.parseInt(data[2]);
                String type = data[3];
                String job = data[4];
                String description = data[5];
                LocalDateTime dateTime = LocalDateTime.parse(data[6],
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

                // Check if the request matches the staff member's working hostel, type, and job
                if (hostelNo == workingHostel && type.equalsIgnoreCase(staffType)
                        && (staffJob.equalsIgnoreCase(job) || job.isEmpty())) {
                    // Check if the request has not already been handled
                    boolean handled = false;
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader("requestDoneByStaff"));
                        String doneLine;
                        while ((doneLine = reader.readLine()) != null) {
                            String[] doneData = doneLine.split(",");
                            int doneRequestId = Integer.parseInt(doneData[0]);
                            String doneRequestStatus = doneData[2];
                            if (doneRequestId == requestId) {
                                if (doneRequestStatus.equalsIgnoreCase("pending")) {
                                    handled = false;
                                    break;
                                } else {
                                    handled = true;
                                }

                            }
                        }
                        reader.close();
                    } catch (FileNotFoundException e) {
                        handled = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (!handled) {
                        // Print the request details and prompt the staff member to enter the remark and
                        // status
                        System.out.println("\nRequest ID: " + requestId);
                        System.out.println("Hostel No.: " + hostelNo);
                        System.out.println("Room No.: " + roomNo);
                        System.out.println("Description of Request: " + description);
                        System.out.println("Date and Time of Request: " + dateTime);

                        System.out.print("Enter your remark for the request: ");
                        String remark = input.nextLine();
                        System.out.print("Enter the status of the request (done, pending, can't be done): ");
                        String status = input.nextLine();

                        // Write the request details to the file
                        BufferedWriter out = new BufferedWriter(new FileWriter("requestDoneByStaff", true));
                        out.write(String.format("%d,%s,%s,%d,%s,%s,%s,%s\n", requestId, remark, status, staffId,
                                staffType, staffJob,
                                dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), description));
                        out.close();

                        System.out.println("Request handled successfully.");
                    }
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("No request till now");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void contactWarden() {
        boolean isfound = false;
        List<Hostel> hostels = readHostelsFromFile("hostel.ser");
        if (hostels.size() == 0) {
            System.out.println("There is no hostel");
        } else {
            System.out.print("Enter hostel number: ");
            do {
                int hostelNo = input.nextInt();
                for (Hostel hostel : hostels) {
                    if (hostel.getHostelNo() == hostelNo) {
                        System.out.println("Warden name: " + hostel.getWardenName());
                        System.out.println("Warden email: " + hostel.getWardenEmail());
                        isfound = true;
                        break;
                    }
                }
                if (!isfound) {
                    System.out.println("Hostel " + hostelNo + " doesn't exist!!!\nPlease enter a valid hostel No: ");
                }

            } while (!isfound);
        }

    }

    public static void about() {
        System.out.println("\n\nWelcome to the Hostel Management System!");
        System.out.println(
                "This software application was developed by Pawan Kumar Lavvanshi, under the guidance of Training Officer Pooja Salva.");
        System.out.println(
                "The system is designed to simplify and streamline the management of a hostel or hotel, allowing administrators to manage guest check-in and check-out, room allocation, and billing information.");
        System.out.println(
                "The project is developed in Java and utilizes file system storage, with features such as Java 8's Lambda expressions and Stream API for efficient data processing.");
        System.out.println(
                "The system also includes a user-friendly interface that makes it easy to navigate and perform tasks quickly.");
        System.out.println(
                "Whether you're managing a small hostel or a large hotel, the Hostel Management System is an essential tool that can help you manage your business effectively.");
        System.out.println(
                "We hope you find our Hostel Management System useful in managing your hostel or hotel. Thank you for using our software!");
    }

    public static void quit() {
        System.out.println("Thank you for using the Hostel Management System!");
        System.out.println("We hope our software has been useful in managing your hostel or hotel.");
        System.out.println(
                "If you need help with any other software project or development work, please feel free to contact Pawan Kumar Lavvanshi at pawankumarlavvanshi@gmail.com.");
        System.out.println("Have a great day!");
        System.exit(0);
    }

    public static boolean userRegistration(int userType) {
        String userName, password;
        int referenceId;

        // Check if reference ID is valid based on user type
        switch (userType) {
            case 1: // Programmer
                referenceId = 0;
                break;
            case 2: // Admin/Warden
                System.out.print("Enter working hostel: ");
                referenceId = input.nextInt();
                if (!checkIfHostelExists(referenceId)) {
                    System.out.println("Hostel does not exist.");
                    return false;
                }
                break;
            case 3: // Student
                System.out.print("Enter roll number: ");
                referenceId = input.nextInt();
                if (!checkIfRollnoExists(referenceId)) {
                    System.out.println("Roll number does not exist.");
                    return false;
                }
                break;
            case 4: // Staff
                System.out.print("Enter staff ID: ");
                referenceId = input.nextInt();
                if (!checkIfStaffIdExist(referenceId)) {
                    System.out.println("Staff ID does not exist.");
                    return false;
                }
                break;
            default:
                System.out.println("Invalid user type.");
                return false;
        }
        System.out.print("Enter username: ");
        userName = input.nextLine();
        System.out.println(userName);
        while (isUsernameAvailable(userName)) {
            System.out.println("Username is already taken. Please try a different one.\nPlease enter username again: ");
            userName = input.nextLine();
        }

        System.out.print("Enter password: ");
        password = input.nextLine();

        // Hash password
        String hashedPassword = hashPassword(password);

        // Write credentials to file
        try (FileWriter writer = new FileWriter("credentials.txt", true)) {
            writer.write(userName + DELIMITER + hashedPassword + DELIMITER + userType + DELIMITER + referenceId + "\n");
            System.out.println("User registered successfully.");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    private static String hashPassword(String password) {
        String hashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            hashedPassword = Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

    public static boolean isUsernameAvailable(String username) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("credentials.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(DELIMITER);
                if (parts[0].equals(username)) {
                    reader.close();
                    return false;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean isAuthorized(String userName, String password, int userType) {
        boolean authorized = false;
        try (BufferedReader br = new BufferedReader(new FileReader("credentials.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(DELIMITER);
                if (parts.length == 3 && Integer.parseInt(parts[2]) == userType && parts[0].equals(userName)) {
                    String hashedPassword = hashPassword(password);
                    if (hashedPassword.equals(parts[1])) {
                        authorized = true;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authorized;
    }

    private static boolean authentication(int userType) {
        boolean authenticated = false;
        int remainingAttempts = 3;
        String username = null;
        String password = null;

        System.out.println("\n\nWelcome to Hostel Management System!\n");
        System.out.println("To enter in this portal first authenticate yourself.\n");
        System.out.println("1. Log in");
        System.out.println("2. Register\nSelect one option...");

        int option = input.nextInt();
        switch (option) {
            case 1:
                while (!authenticated && remainingAttempts > 0) {
                    System.out.print("Enter username: ");
                    username = input.nextLine();
                    System.out.print("Enter password: ");
                    password = input.nextLine();

                    authenticated = isAuthorized(username, password, userType);

                    if (!authenticated) {
                        remainingAttempts--;
                        System.out
                                .println("Invalid username or password. " + remainingAttempts + " attempts remaining.");
                    }
                }

                if (authenticated) {
                    System.out.println("Welcome, " + username + "!");
                    return authenticated;
                } else {
                    System.out.println("You have exceeded the maximum number of attempts. Please try again later.");
                }
                break;

            case 2:
                if (userRegistration(userType)) {
                    System.out
                            .println("Keep USERNAME and PASSWORD safe and remember them for log .\nYou can login now.");
                }

                break;
            default:
                System.out.println("Wrong Choice!!!");
        }
        return authenticated;
    }

}