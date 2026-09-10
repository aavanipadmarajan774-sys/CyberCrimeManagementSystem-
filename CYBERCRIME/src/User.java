import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

    // =====================================================
    // VIEW ALL USERS
    // =====================================================

    public static void viewUsers() {

        System.out.println("\n======================================");
        System.out.println("             ALL USERS");
        System.out.println("======================================");

        String sql =
            "SELECT user_id, full_name, username, email, role " +
            "FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println("--------------------------------------");

                System.out.println("User ID      : "
                        + rs.getInt("user_id"));

                System.out.println("Full Name    : "
                        + rs.getString("full_name"));

                System.out.println("Username     : "
                        + rs.getString("username"));

                System.out.println("Email        : "
                        + rs.getString("email"));

                System.out.println("Role         : "
                        + rs.getString("role"));
            }

            if (!found) {
                System.out.println("No users found.");
            }

            System.out.println("--------------------------------------");

        } catch (SQLException e) {

            System.out.println("\nError retrieving users!");
            e.printStackTrace();
        }
    }


    // =====================================================
    // ADD NEW USER
    // =====================================================

    public static void addUser(Scanner sc) {

        System.out.println("\n======================================");
        System.out.println("             ADD NEW USER");
        System.out.println("======================================");

        sc.nextLine();

        System.out.print("Enter Full Name: ");
        String fullName = sc.nextLine();

        System.out.print("Enter Username: ");
        String username = sc.nextLine();

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        System.out.println("\nSelect Role:");
        System.out.println("1. Citizen");
        System.out.println("2. Officer");
        System.out.println("3. Administrator");

        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();

        String role;

        switch (choice) {

            case 1:
                role = "Citizen";
                break;

            case 2:
                role = "Officer";
                break;

            case 3:
                role = "Administrator";
                break;

            default:
                System.out.println("\nInvalid role choice.");
                return;
        }

        // Check whether username already exists
        String checkSql =
            "SELECT username FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt =
                 conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, username);

            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {

                System.out.println(
                    "\nUsername already exists!"
                );

                return;
            }

        } catch (SQLException e) {

            System.out.println(
                "\nError checking username!"
            );

            e.printStackTrace();

            return;
        }


        // Insert new user
        String sql =
            "INSERT INTO users " +
            "(full_name, username, email, password, role) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt =
                 conn.prepareStatement(sql)) {

            stmt.setString(1, fullName);
            stmt.setString(2, username);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, role);

            int rows = stmt.executeUpdate();

            if (rows > 0) {

                System.out.println(
                    "\nUser added successfully!"
                );

                System.out.println(
                    "Username : " + username
                );

                System.out.println(
                    "Role     : " + role
                );

            } else {

                System.out.println(
                    "\nFailed to add user."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                "\nError adding user!"
            );

            e.printStackTrace();
        }
    }


    // =====================================================
    // DELETE USER
    // =====================================================

    public static void deleteUser(Scanner sc) {

        System.out.println("\n======================================");
        System.out.println("             DELETE USER");
        System.out.println("======================================");

        System.out.print("Enter User ID to delete: ");
        int userId = sc.nextInt();
        sc.nextLine();


        // Check whether user exists
        String checkSql =
            "SELECT username, full_name, role " +
            "FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt =
                 conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, userId);

            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {

                System.out.println("\nUser not found.");
                return;
            }

            String username = rs.getString("username");
            String fullName = rs.getString("full_name");
            String role = rs.getString("role");

            System.out.println("\nUser Details:");
            System.out.println("--------------------------------------");
            System.out.println("User ID   : " + userId);
            System.out.println("Full Name : " + fullName);
            System.out.println("Username  : " + username);
            System.out.println("Role      : " + role);
            System.out.println("--------------------------------------");


            // ==========================================
            // CHECK WHETHER USER HAS CYBER CASES
            // ==========================================

            String caseSql =
                "SELECT COUNT(*) AS case_count " +
                "FROM cyber_cases WHERE user_id = ?";

            try (PreparedStatement caseStmt =
                     conn.prepareStatement(caseSql)) {

                caseStmt.setInt(1, userId);

                ResultSet caseResult =
                    caseStmt.executeQuery();

                if (caseResult.next()) {

                    int caseCount =
                        caseResult.getInt("case_count");

                    if (caseCount > 0) {

                        System.out.println(
                            "\nCannot delete this user!"
                        );

                        System.out.println(
                            "This user has " +
                            caseCount +
                            " cybercrime case(s)."
                        );

                        System.out.println(
                            "Existing case records must be preserved."
                        );

                        return;
                    }
                }
            }


            // ==========================================
            // CONFIRM DELETION
            // ==========================================

            System.out.print(
                "Are you sure you want to delete this user? (yes/no): "
            );

            String confirmation = sc.nextLine();

            if (!confirmation.equalsIgnoreCase("yes")) {

                System.out.println("\nDeletion cancelled.");
                return;
            }

        } catch (SQLException e) {

            System.out.println("\nError checking user!");
            e.printStackTrace();
            return;
        }


        // =================================================
        // DELETE USER
        // =================================================

        String deleteSql =
            "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt =
                 conn.prepareStatement(deleteSql)) {

            stmt.setInt(1, userId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {

                System.out.println(
                    "\nUser deleted successfully!"
                );

            } else {

                System.out.println(
                    "\nUser could not be deleted."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                "\nError deleting user!"
            );

            e.printStackTrace();
        }
    }
}