package StudentPortal;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import Administration.Administration;
import HostelManagementSystem.HostelManagementSystem;
import Model.Room;

public class StudentPortal {
    static Scanner input = new Scanner(System.in);

    public static void studentPortal() throws IOException {
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
                    HostelManagementSystem.printHostelsInTable();
                    break;
                case 2:
                    if (HostelManagementSystem.printHostelsInTable()) {
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
                    HostelManagementSystem.contactWarden();
                    break;
                case 8:
                    System.out.println("Redirecting to Main Menu.... \n");
                    break;
                case 9:
                    HostelManagementSystem.quit();
                    break;
                default:
                    System.out.println("Wrong choice!!!\n\n");
            }
        } while (studentchoice != 8);
    }

    public static void viewAvailableRooms(int hostelNo) {
        File f = new File("Utils/hostel" + hostelNo + ".ser");
        if (f.exists()) {
            List<Room> rooms = Administration.readRoomsFromFile(f);

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
                    Administration.viewStaff();
                    break;
                case 4:
                    System.out.println("Redirecting to Previous Menu...\n\n");
                    break;
                case 5:
                    HostelManagementSystem.quit();
                    break;
                default:
                    System.out.println("Wrong Choice!!!");
            }
        } while (studentRequestChoice != 4);
    }

    public static void makeRequest(int hostelNo, int roomNo, String type, String job, String description,
            LocalDateTime dateTime) throws IOException {
        int requestId;
        File requestFile = new File("Utils/studentRequestToStaff.csv");
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
                try (BufferedReader br2 = new BufferedReader(new FileReader("Utils/studentRequestToStaff.csv"))) {
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
        String requestFile = "Utils/studentRequestToStaff.csv";
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

}
