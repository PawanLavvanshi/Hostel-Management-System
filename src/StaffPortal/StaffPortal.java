package StaffPortal;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import Administration.Administration;
import HostelManagementSystem.HostelManagementSystem;

public class StaffPortal {
    static Scanner input = new Scanner(System.in);

    public static void staffPortal() throws IOException {
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
                    Administration.viewStaff();
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
                    HostelManagementSystem.contactWarden();
                    break;
                case 7:
                    System.out.println("Redirecting to Main Menu... \n");
                    break;
                case 8:
                    HostelManagementSystem.quit();
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
            BufferedReader in = new BufferedReader(new FileReader("Utils/studentRequestToStaff.csv"));
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

}
