package HostelManagementSystem;

import java.io.*;
import java.security.*;
import java.util.*;

import Administration.Administration;
import Model.*;
import StaffPortal.StaffPortal;
import StudentPortal.StudentPortal;

public class HostelManagementSystem {
    static Scanner input = new Scanner(System.in);
    private static final String DELIMITER = ",";

    public static void mainMenu() throws Exception {
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
            choice = isValidInt();
            switch (choice) {
                case 1:
                    hostelDetailsPortal();
                    break;
                case 2:
                    // if (authentication(2)) {
                    Administration.admistrationPortal();
                    // }
                    break;
                case 3:
                    if (authentication(3)) {
                        StudentPortal.studentPortal();
                    }
                    break;
                case 4:
                    if (authentication(4)) {
                        StaffPortal.staffPortal();
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

    public static void hostelDetailsPortal() throws ClassNotFoundException, IOException {
        int hostelChoise;
        do {
            System.out.println("\n\nWelcome to hostel datails portal.");
            System.out.println("---------------------------------");
            System.out.println("1. All hostels details ");
            System.out.println("2. Specific hostel detail");
            System.out.println("3. Previous menu");
            System.out.println("4. Exit from program");
            System.out.println("Enter your choice... ");
            hostelChoise = isValidInt();
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
    }

    public static boolean printHostelsInTable() {
        ArrayList<Hostel> hostels = readHostelsFromFile("Utils/hostel.ser");
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

    static void hostelDetails() throws ClassNotFoundException, IOException {
        ArrayList<Hostel> hostels = readHostelsFromFile("Utils/hostel.ser");
        if (hostels.size() == 0) {
            System.out.println("There is no Hostel Added. Please contact Admistration.");
            return;
        }
        String inputStr;
        do {
            System.out.println("\n\nAvailable hostels:");
            printHostelMenu(hostels);
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

    public static void printHostelMenu(ArrayList<Hostel> hostels) {
        for (Hostel hostel : hostels) {
            System.out.printf("%d. %s\n", hostel.getHostelNo(), hostel.getHostelName());
        }
    }

    public static boolean showSpecialHostelDetails(int hostelNumber) throws IOException, ClassNotFoundException {
        String fileName = "Utils/hostel" + hostelNumber + ".ser";
        File hostelFile = new File(fileName);

        if (hostelFile.exists()) {
            List<Hostel> hostels = readHostelsFromFile("Utils/hostel.ser");
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

    public static void contactWarden() {
        boolean isfound = false;
        List<Hostel> hostels = readHostelsFromFile("Utils/hostel.ser");
        if (hostels.size() == 0) {
            System.out.println("There is no hostel");
        } else {
            System.out.print("Enter hostel number: ");
            do {
                int hostelNo = isValidInt();
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
                referenceId = isValidInt();
                if (!checkIfHostelExists(referenceId)) {
                    System.out.println("Hostel does not exist.");
                    return false;
                }
                break;
            case 3: // Student
                System.out.print("Enter roll number: ");
                referenceId = isValidInt();
                if (!checkIfRollnoExists(referenceId)) {
                    System.out.println("Roll number does not exist.");
                    return false;
                }
                break;
            case 4: // Staff
                System.out.print("Enter staff ID: ");
                referenceId = isValidInt();
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
        try (FileWriter writer = new FileWriter("Utils/credentials.txt", true)) {
            writer.write(userName + DELIMITER + hashedPassword + DELIMITER + userType + DELIMITER + referenceId + "\n");
            System.out.println("User registered successfully.");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean checkIfHostelExists(int hostelNo) {
        List<Hostel> hostels = readHostelsFromFile("Utils/hostel.ser");
        for (Hostel hostel : hostels) {
            if (hostel.getHostelNo() == hostelNo) {
                return true;
            }
        }
        return false;
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

    public static boolean checkIfRollnoExists(int rollno) {
        List<Student> students = readStudentsFromFile("Utils/student.ser");
        for (Student room : students) {
            if (room.getRollNo() == rollno) {
                return true;
            }
        }
        return false;
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

    public static boolean checkIfStaffIdExist(int id) {
        List<Staff> staffs = readStaffsFromFile("Utils/staff.ser");
        for (Staff staff : staffs) {
            if (staff.getStaffId() == id) {
                return true;
            }
        }
        return false;
    }

    public static List<Staff> readStaffsFromFile(String filename) {

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
            BufferedReader reader = new BufferedReader(new FileReader("Utils/credentials.txt"));
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
        try (BufferedReader br = new BufferedReader(new FileReader("Utils/credentials.txt"))) {
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

        int option = isValidInt();
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

    public static int isValidInt() {
        int value;
        try {
            value = input.nextInt();
            input.nextLine();
            return value;
        } catch (InputMismatchException e) {
            input.nextLine();
            System.out.println("Only Integer value acceptable!!!\n Please enter an integer: ");
            value = isValidInt();
            return value;
        }
    }

}
