import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

public class Complaint {

    // =====================================================
    // REPORT CYBERCRIME
    // =====================================================

    public static void reportCrime(Scanner sc, String username) {

        System.out.println("\n======================================");
        System.out.println("          REPORT CYBERCRIME");
        System.out.println("======================================");

        System.out.print("Enter crime type: ");
        String crimeType = sc.nextLine();

        System.out.print("Enter description: ");
        String description = sc.nextLine();

        LocalDate date = LocalDate.now();

        String sql = "INSERT INTO complaints " +
                     "(username, crime_type, description, complaint_date) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, crimeType);
            stmt.setString(3, description);
            stmt.setDate(4, java.sql.Date.valueOf(date));

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("\nComplaint submitted successfully!");
                System.out.println("Status: Pending");
            }

        } catch (SQLException e) {

            System.out.println("\nError submitting complaint!");
            e.printStackTrace();
        }
    }


    // =====================================================
    // VIEW COMPLAINTS
    // =====================================================

    public static void viewComplaints(String username, String role) {

        System.out.println("\n======================================");

        if (role.equalsIgnoreCase("citizen")) {
            System.out.println("        MY COMPLAINTS");
        } else {
            System.out.println("        ALL COMPLAINTS");
        }

        System.out.println("======================================");

        String sql;

        // Citizen sees only their own complaints
        if (role.equalsIgnoreCase("citizen")) {

            sql = "SELECT * FROM complaints WHERE username = ?";

        } else {

            // Officer and Administrator see all complaints
            sql = "SELECT * FROM complaints";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (role.equalsIgnoreCase("citizen")) {
                stmt.setString(1, username);
            }

            ResultSet rs = stmt.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("--------------------------------------");

                System.out.println("Complaint ID : "
                        + rs.getInt("complaint_id"));

                System.out.println("Username     : "
                        + rs.getString("username"));

                System.out.println("Crime Type   : "
                        + rs.getString("crime_type"));

                System.out.println("Description  : "
                        + rs.getString("description"));

                System.out.println("Date         : "
                        + rs.getDate("complaint_date"));

                System.out.println("Status       : "
                        + rs.getString("status"));

                System.out.println("Assigned To  : "
                        + rs.getString("assigned_officer"));
            }

            if (!found) {

                if (role.equalsIgnoreCase("citizen")) {
                    System.out.println(
                        "You have not submitted any complaints."
                    );
                } else {
                    System.out.println("No complaints found.");
                }
            }

            System.out.println("--------------------------------------");

        } catch (SQLException e) {

            System.out.println("\nError retrieving complaints!");
            e.printStackTrace();
        }
    }


    // =====================================================
    // SEARCH COMPLAINT
    // =====================================================

    public static void searchComplaint(
            Scanner sc,
            String username,
            String role) {

        System.out.println("\n======================================");
        System.out.println("         SEARCH COMPLAINT");
        System.out.println("======================================");

        System.out.print("Enter Complaint ID: ");
        int complaintId = sc.nextInt();
        sc.nextLine();

        String sql;

        // Citizen can search only their own complaint
        if (role.equalsIgnoreCase("citizen")) {

            sql = "SELECT * FROM complaints " +
                  "WHERE complaint_id = ? AND username = ?";

        } else {

            // Officer and Administrator can search any complaint
            sql = "SELECT * FROM complaints " +
                  "WHERE complaint_id = ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, complaintId);

            if (role.equalsIgnoreCase("citizen")) {
                stmt.setString(2, username);
            }

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                System.out.println("\nComplaint Found!");
                System.out.println("--------------------------------------");

                System.out.println("Complaint ID : "
                        + rs.getInt("complaint_id"));

                System.out.println("Username     : "
                        + rs.getString("username"));

                System.out.println("Crime Type   : "
                        + rs.getString("crime_type"));

                System.out.println("Description  : "
                        + rs.getString("description"));

                System.out.println("Date         : "
                        + rs.getDate("complaint_date"));

                System.out.println("Status       : "
                        + rs.getString("status"));

                System.out.println("Assigned To  : "
                        + rs.getString("assigned_officer"));

                System.out.println("--------------------------------------");

            } else {

                if (role.equalsIgnoreCase("citizen")) {

                    System.out.println(
                        "\nComplaint not found or it does not belong to you."
                    );

                } else {

                    System.out.println(
                        "\nNo complaint found with ID: " + complaintId
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println("\nError searching complaint!");
            e.printStackTrace();
        }
    }


    

    // =====================================================
// UPDATE COMPLAINT
// =====================================================

public static void updateComplaint(
        Scanner sc,
        String username,
        String role) {

    System.out.println("\n======================================");
    System.out.println("         UPDATE COMPLAINT");
    System.out.println("======================================");

    System.out.print("Enter Complaint ID: ");
    int complaintId = sc.nextInt();
    sc.nextLine();

    // Check whether officer is allowed to update this complaint
    if (role.equalsIgnoreCase("officer")) {

        String checkSql =
            "SELECT * FROM complaints " +
            "WHERE complaint_id = ? AND assigned_officer = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt =
                 conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, complaintId);
            checkStmt.setString(2, username);

            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {

                System.out.println(
                    "\nYou are not authorized to update this complaint."
                );

                System.out.println(
                    "This complaint is not assigned to you."
                );

                return;
            }

        } catch (SQLException e) {

            System.out.println(
                "\nError checking complaint assignment!"
            );

            e.printStackTrace();

            return;
        }
    }


    // Select new status
    System.out.println("\nSelect New Status:");
    System.out.println("1. Pending");
    System.out.println("2. Investigating");
    System.out.println("3. Resolved");

    System.out.print("Enter your choice: ");
    int choice = sc.nextInt();
    sc.nextLine();

    String newStatus;

    switch (choice) {

        case 1:
            newStatus = "Pending";
            break;

        case 2:
            newStatus = "Investigating";
            break;

        case 3:
            newStatus = "Resolved";
            break;

        default:
            System.out.println("Invalid status choice.");
            return;
    }


    // Update complaint
    String sql =
        "UPDATE complaints SET status = ? " +
        "WHERE complaint_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, newStatus);
        stmt.setInt(2, complaintId);

        int rows = stmt.executeUpdate();

        if (rows > 0) {

            System.out.println(
                "\nComplaint updated successfully!"
            );

            System.out.println(
                "Complaint ID : " + complaintId
            );

            System.out.println(
                "New Status   : " + newStatus
            );

        } else {

            System.out.println(
                "\nComplaint ID not found."
            );
        }

    } catch (SQLException e) {

        System.out.println(
            "\nError updating complaint!"
        );

        e.printStackTrace();
    }
}


    // =====================================================
    // ASSIGN COMPLAINT TO OFFICER
    // =====================================================

    public static void assignComplaint(Scanner sc) {

        System.out.println("\n======================================");
        System.out.println("        ASSIGN COMPLAINT");
        System.out.println("======================================");

        System.out.print("Enter Complaint ID: ");
        int complaintId = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Officer Username: ");
        String officerUsername = sc.nextLine();

        // Check whether the officer exists
        String checkOfficer =
            "SELECT * FROM users WHERE username = ? AND role = 'Officer'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement officerStmt =
                 conn.prepareStatement(checkOfficer)) {

            officerStmt.setString(1, officerUsername);

            ResultSet officerResult = officerStmt.executeQuery();

            if (!officerResult.next()) {

                System.out.println("\nOfficer not found!");
                System.out.println(
                    "Please enter a valid Officer username."
                );

                return;
            }

        } catch (SQLException e) {

            System.out.println("\nError checking officer!");
            e.printStackTrace();

            return;
        }


        // Assign complaint
        String sql =
            "UPDATE complaints SET assigned_officer = ? " +
            "WHERE complaint_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, officerUsername);
            stmt.setInt(2, complaintId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {

                System.out.println("\nComplaint assigned successfully!");
                System.out.println("Complaint ID : " + complaintId);
                System.out.println("Officer      : " + officerUsername);

            } else {

                System.out.println("\nComplaint ID not found.");
            }

        } catch (SQLException e) {

            System.out.println("\nError assigning complaint!");
            e.printStackTrace();
        }
    }


    // =====================================================
    // VIEW ASSIGNED COMPLAINTS
    // =====================================================

    public static void viewAssignedComplaints(String officerUsername) {

        System.out.println("\n======================================");
        System.out.println("       MY ASSIGNED COMPLAINTS");
        System.out.println("======================================");

        String sql =
            "SELECT * FROM complaints " +
            "WHERE assigned_officer = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, officerUsername);

            ResultSet rs = stmt.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("--------------------------------------");

                System.out.println("Complaint ID : "
                        + rs.getInt("complaint_id"));

                System.out.println("Citizen      : "
                        + rs.getString("username"));

                System.out.println("Crime Type   : "
                        + rs.getString("crime_type"));

                System.out.println("Description  : "
                        + rs.getString("description"));

                System.out.println("Date         : "
                        + rs.getDate("complaint_date"));

                System.out.println("Status       : "
                        + rs.getString("status"));

                System.out.println("Officer      : "
                        + rs.getString("assigned_officer"));
            }

            if (!found) {

                System.out.println(
                    "No complaints have been assigned to you."
                );
            }

            System.out.println("--------------------------------------");

        } catch (SQLException e) {

            System.out.println(
                "\nError retrieving assigned complaints!"
            );

            e.printStackTrace();
        }
    }
    

    public static void viewStatistics() {

        System.out.println("\n======================================");
        System.out.println("        COMPLAINT STATISTICS");
        System.out.println("======================================");

        String sql =
            "SELECT " +
            "COUNT(*) AS total, " +
            "SUM(CASE WHEN status = 'Pending' THEN 1 ELSE 0 END) AS pending, " +
            "SUM(CASE WHEN status = 'Investigating' THEN 1 ELSE 0 END) AS investigating, " +
            "SUM(CASE WHEN status = 'Resolved' THEN 1 ELSE 0 END) AS resolved, " +
            "SUM(CASE WHEN assigned_officer IS NOT NULL " +
            "AND assigned_officer <> '' THEN 1 ELSE 0 END) AS assigned, " +
            "SUM(CASE WHEN assigned_officer IS NULL " +
            "OR assigned_officer = '' THEN 1 ELSE 0 END) AS unassigned " +
            "FROM complaints";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {

                int total = rs.getInt("total");
                int pending = rs.getInt("pending");
                int investigating = rs.getInt("investigating");
                int resolved = rs.getInt("resolved");
                int assigned = rs.getInt("assigned");
                int unassigned = rs.getInt("unassigned");

                System.out.println("--------------------------------------");

                System.out.println(
                    "Total Complaints       : " + total
                );

                System.out.println(
                    "Pending Complaints     : " + pending
                );

                System.out.println(
                    "Investigating          : " + investigating
                );

                System.out.println(
                    "Resolved Complaints    : " + resolved
                );

                System.out.println(
                    "Assigned Complaints    : " + assigned
                );

                System.out.println(
                    "Unassigned Complaints  : " + unassigned
                );

                System.out.println("--------------------------------------");

            } else {

                System.out.println("No complaint data available.");
            }

        } catch (SQLException e) {

            System.out.println(
                "\nError retrieving complaint statistics!"
            );

            e.printStackTrace();
        }
    }
    

    // =====================================================
    // OFFICER-WISE COMPLAINT STATISTICS
    // =====================================================

    public static void viewOfficerStatistics() {

        System.out.println("\n======================================");
        System.out.println("     OFFICER-WISE STATISTICS");
        System.out.println("======================================");

        String sql =
            "SELECT u.username, u.full_name, " +
            "COUNT(c.complaint_id) AS complaint_count " +
            "FROM users u " +
            "LEFT JOIN complaints c " +
            "ON u.username = c.assigned_officer " +
            "WHERE u.role = 'Officer' " +
            "GROUP BY u.username, u.full_name " +
            "ORDER BY complaint_count DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("--------------------------------------");

                System.out.println(
                    "Officer Name : " + rs.getString("full_name")
                );

                System.out.println(
                    "Username     : " + rs.getString("username")
                );

                System.out.println(
                    "Complaints   : " + rs.getInt("complaint_count")
                );
            }

            if (!found) {
                System.out.println("No officers found.");
            }

            System.out.println("--------------------------------------");

        } catch (SQLException e) {

            System.out.println(
                "\nError retrieving officer statistics!"
            );

            e.printStackTrace();
        }
    }
}