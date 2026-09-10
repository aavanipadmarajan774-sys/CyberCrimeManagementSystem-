import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/cybercrime_db";

    private static final String USER = "root";
    private static final String PASSWORD = "MYSQLAAVANI";

    public static Connection getConnection() {

        try {

            Connection connection = DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
            );

            return connection;

        } catch (SQLException e) {

            System.out.println("Database connection failed!");
            e.printStackTrace();

            return null;
        }
    }
}