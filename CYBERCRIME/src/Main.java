import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("======================================");
        System.out.println("     CYBERCRIME MANAGEMENT SYSTEM");
        System.out.println("======================================");

        System.out.print("Enter Username: ");
        String username = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        // Login
        String role = Login.login(username, password);

        // Check login result
        if (role == null) {

            System.out.println("\nLogin failed.");
            sc.close();
            return;
        }

        // ==========================================
        // ROLE-BASED DASHBOARD
        // ==========================================

        if (role.equalsIgnoreCase("Administrator")) {

            showAdministratorDashboard(sc, username);

        } else if (role.equalsIgnoreCase("Officer")) {

            showOfficerDashboard(sc, username);

        } else if (role.equalsIgnoreCase("Citizen")) {

            showCitizenDashboard(sc, username);

        } else {

            System.out.println(
                "\nUnknown user role."
            );
        }

        sc.close();
    }


    // =====================================================
    // ADMINISTRATOR DASHBOARD
    // =====================================================

    public static void showAdministratorDashboard(
            Scanner sc,
            String username) {

        int choice = 0;

        while (choice != 11) {

            System.out.println("\n======================================");
            System.out.println("       ADMINISTRATOR DASHBOARD");
            System.out.println("======================================");

            System.out.println("Welcome, " + username);

            System.out.println("--------------------------------------");
            System.out.println("1. Report Cybercrime");
            System.out.println("2. View All Complaints");
            System.out.println("3. Search Complaint");
            System.out.println("4. Update Complaint");
            System.out.println("5. Assign Complaint");
            System.out.println("6. View All Users");
            System.out.println("7. Add New User");
            System.out.println("8. Delete User");
            System.out.println("9. Complaint Statistics");
            System.out.println("10. Officer-wise Statistics");
            System.out.println("11. Logout");
            System.out.println("--------------------------------------");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                // --------------------------------------
                // REPORT CYBERCRIME
                // --------------------------------------

                case 1:

                    Complaint.reportCrime(
                        sc,
                        username
                    );

                    break;


                // --------------------------------------
                // VIEW ALL COMPLAINTS
                // --------------------------------------

                case 2:

                    Complaint.viewComplaints(
                        username,
                        "Administrator"
                    );

                    break;


                // --------------------------------------
                // SEARCH COMPLAINT
                // --------------------------------------

                case 3:

                    Complaint.searchComplaint(
                        sc,
                        username,
                        "Administrator"
                    );

                    break;


                // --------------------------------------
                // UPDATE COMPLAINT
                // --------------------------------------

                case 4:

                    Complaint.updateComplaint(
                        sc,
                        username,
                        "Administrator"
                    );

                    break;


                // --------------------------------------
                // ASSIGN COMPLAINT
                // --------------------------------------

                case 5:

                    Complaint.assignComplaint(sc);

                    break;


                // --------------------------------------
                // VIEW ALL USERS
                // --------------------------------------

                case 6:

                    User.viewUsers();

                    break;


                // --------------------------------------
                // ADD NEW USER
                // --------------------------------------

                case 7:

                    User.addUser(sc);

                    break;


                // --------------------------------------
                // DELETE USER
                // --------------------------------------

                case 8:

                    User.deleteUser(sc);

                    break;


                // --------------------------------------
                // COMPLAINT STATISTICS
                // --------------------------------------

                case 9:

                    Complaint.viewStatistics();

                    break;


                // --------------------------------------
                // OFFICER-WISE STATISTICS
                // --------------------------------------

                case 10:

                    Complaint.viewOfficerStatistics();

                    break;


                // --------------------------------------
                // LOGOUT
                // --------------------------------------

                case 11:

                    System.out.println(
                        "\nLogging out..."
                    );

                    System.out.println(
                        "Thank you for using the system!"
                    );

                    break;


                default:

                    System.out.println(
                        "\nInvalid choice. Please try again."
                    );
            }
        }
    }


    // =====================================================
    // OFFICER DASHBOARD
    // =====================================================

    public static void showOfficerDashboard(
            Scanner sc,
            String username) {

        int choice = 0;

        while (choice != 4) {

            System.out.println("\n======================================");
            System.out.println("           OFFICER DASHBOARD");
            System.out.println("======================================");

            System.out.println("Welcome, " + username);

            System.out.println("--------------------------------------");
            System.out.println("1. My Assigned Complaints");
            System.out.println("2. Search Complaint");
            System.out.println("3. Update Complaint");
            System.out.println("4. Logout");
            System.out.println("--------------------------------------");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                // --------------------------------------
                // VIEW ASSIGNED COMPLAINTS
                // --------------------------------------

                case 1:

                    Complaint.viewAssignedComplaints(
                        username
                    );

                    break;


                // --------------------------------------
                // SEARCH COMPLAINT
                // --------------------------------------

                case 2:

                    Complaint.searchComplaint(
                        sc,
                        username,
                        "Officer"
                    );

                    break;


                // --------------------------------------
                // UPDATE COMPLAINT
                // --------------------------------------

                case 3:

                    Complaint.updateComplaint(
                        sc,
                        username,
                        "Officer"
                    );

                    break;


                // --------------------------------------
                // LOGOUT
                // --------------------------------------

                case 4:

                    System.out.println(
                        "\nLogging out..."
                    );

                    System.out.println(
                        "Thank you for using the system!"
                    );

                    break;


                default:

                    System.out.println(
                        "\nInvalid choice. Please try again."
                    );
            }
        }
    }


    // =====================================================
    // CITIZEN DASHBOARD
    // =====================================================

    public static void showCitizenDashboard(
            Scanner sc,
            String username) {

        int choice = 0;

        while (choice != 4) {

            System.out.println("\n======================================");
            System.out.println("            CITIZEN DASHBOARD");
            System.out.println("======================================");

            System.out.println("Welcome, " + username);

            System.out.println("--------------------------------------");
            System.out.println("1. Report Cybercrime");
            System.out.println("2. View My Complaints");
            System.out.println("3. Search My Complaint");
            System.out.println("4. Logout");
            System.out.println("--------------------------------------");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                // --------------------------------------
                // REPORT CYBERCRIME
                // --------------------------------------

                case 1:

                    Complaint.reportCrime(
                        sc,
                        username
                    );

                    break;


                // --------------------------------------
                // VIEW MY COMPLAINTS
                // --------------------------------------

                case 2:

                    Complaint.viewComplaints(
                        username,
                        "Citizen"
                    );

                    break;


                // --------------------------------------
                // SEARCH MY COMPLAINT
                // --------------------------------------

                case 3:

                    Complaint.searchComplaint(
                        sc,
                        username,
                        "Citizen"
                    );

                    break;


                // --------------------------------------
                // LOGOUT
                // --------------------------------------

                case 4:

                    System.out.println(
                        "\nLogging out..."
                    );

                    System.out.println(
                        "Thank you for using the system!"
                    );

                    break;


                default:

                    System.out.println(
                        "\nInvalid choice. Please try again."
                    );
            }
        }
    }
}