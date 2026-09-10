import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

    public static String login(String username, String password) {

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                System.out.println("\nLogin successful!");
                System.out.println("Welcome, " + rs.getString("full_name"));

                String role = rs.getString("role");

                System.out.println("Role: " + role);

                return role;

            } else {

                System.out.println("Invalid username or password.");
                return null;
            }

        } catch (SQLException e) {

            System.out.println("Login error!");
            e.printStackTrace();
            return null;
        }
    }
}